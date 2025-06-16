package br.com.alura.literAlura.service;

import br.com.alura.literAlura.model.Autor;
import br.com.alura.literAlura.model.DadosAutor;
import br.com.alura.literAlura.model.DadosLivro;
import br.com.alura.literAlura.model.Livro;
import br.com.alura.literAlura.model.RespostaApi;
import br.com.alura.literAlura.reposirory.AutorRepository;
import br.com.alura.literAlura.reposirory.LivroRepository;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GutenService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final ConversorDeDados conversorDeDados;
    private final ConverteDados converteDados;
    private final ConsumoApi consumoApi;

    private final String URL_BASE = "https://gutendex.com/books/?search=";

    public GutenService(LivroRepository livroRepository,
                        AutorRepository autorRepository,
                        ConversorDeDados conversorDeDados,
                        ConverteDados converteDados,
                        ConsumoApi consumoApi) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.conversorDeDados = conversorDeDados;
        this.converteDados = converteDados;
        this.consumoApi = consumoApi;
    }

    @Transactional // Garante que a transação abranja toda a lógica de persistência
    public Optional<Livro> buscarLivroPorTitulo(String titulo) {
        String url = URL_BASE + titulo.replace(" ", "%20");
        String json = consumoApi.obterDados(url);

        RespostaApi resposta = converteDados.obterDados(json, RespostaApi.class);

        // O bloco 'if' principal para verificar se a API retornou resultados
        if (resposta != null && resposta.resultados() != null && !resposta.resultados().isEmpty()) {
            DadosLivro dadosLivro = resposta.resultados().get(0); // Pega o primeiro livro encontrado

            // PRIMEIRO: Verificar se o livro já existe no banco de dados pelo título
            Optional<Livro> livroExistente = livroRepository.findByTituloIgnoreCase(dadosLivro.titulo());
            if (livroExistente.isPresent()) {
                System.out.println("Livro já registrado no banco de dados: " + livroExistente.get().getTitulo());
                // Força a inicialização da coleção de autores enquanto a sessão ainda está ativa
                livroExistente.get().getAutores().size();
                return livroExistente;
            }

            // AQUI: O método 'converter' do ConversorDeDados já é responsável por criar o objeto Livro
            // com todas as propriedades (título, autores, idioma, downloads, e SUMÁRIO).
            // Não há necessidade de setar estas propriedades novamente aqui no GutenService.
            Livro livro = conversorDeDados.converter(dadosLivro);

            // Salva o livro (e os novos autores serão cascateados, se a configuração estiver correta no modelo Livro)
            Livro livroSalvo = livroRepository.save(livro);
            System.out.println("Livro salvo com sucesso: " + livroSalvo.getTitulo());

            // Força a inicialização da coleção de autores para o livro recém-salvo, se necessário
            livroSalvo.getAutores().size();

            return Optional.of(livroSalvo);

        } else { // Este 'else' agora é a contraparte correta do 'if' principal
            System.out.println("Livro não encontrado na API ou erro no processamento.");
            return Optional.empty();
        }
    }

    @Transactional
    public List<Livro> listarTodosLivros() {
        List<Livro> livros = livroRepository.findAll();
        // Para cada livro, força a inicialização da coleção de autores
        livros.forEach(livro -> {
            if (livro.getAutores() != null) {
                livro.getAutores().size(); // Força a inicialização da coleção
            }
        });
        return livros;
    }

    public List<Livro> encontrarTop10LivrosMaisBaixados() {
        return livroRepository.findAll().stream()
                .filter(livro -> livro.getNumeroDownloads() != null) // Garante que não haja NPE
                .sorted((l1, l2) -> l2.getNumeroDownloads().compareTo(l1.getNumeroDownloads())) // Ordena do maior para o menor
                .limit(10) // Pega os 10 primeiros
                .collect(Collectors.toList());
    }
    @Transactional
    public List<Autor> buscarAutorPorNomeNoDB(String nome) {
        List<Autor> autores = autorRepository.findByNameContainingIgnoreCase(nome);
        // Opcional: para cada autor encontrado, inicialize a lista de livros se for exibi-los
        autores.forEach(autor -> {
            if (autor.getLivros() != null) {
                autor.getLivros().size(); // Força a inicialização da coleção lazy de livros
            }
        });
        return autores;
    }

    @Transactional
    public List<Livro> listarLivrosPorIdioma(String idioma) {
        List<Livro> livros = livroRepository.findByIdiomaContainsIgnoreCase(idioma);
        livros.forEach(livro -> {
            if (livro.getAutores() != null) { // Adicionado verificação de null
                livro.getAutores().size(); // Força a inicialização lazy
            }
        });
        return livros;
    }
    public void gerarEstatisticasDeDownloads() {
        List<Livro> livros = livroRepository.findAll(); // Acesso ao repositório aqui é OK, pois é no service.

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado para gerar estatísticas.");
            return;
        }

        DoubleSummaryStatistics stats = livros.stream()
                .filter(livro -> livro.getNumeroDownloads() != null)
                .mapToDouble(Livro::getNumeroDownloads)
                .summaryStatistics();
        System.out.println("\n--- Estatísticas de Downloads de Livros ---");
        System.out.println("Total de livros com downloads: " + stats.getCount());
        System.out.println("Soma total de downloads: " + stats.getSum());
        System.out.println("Média de downloads: " + String.format("%.2f", stats.getAverage()));
        System.out.println("Maior número de downloads: " + stats.getMax());
        System.out.println("Menor número de downloads: " + stats.getMin());
        System.out.println("-------------------------------------------\n");
    }
}