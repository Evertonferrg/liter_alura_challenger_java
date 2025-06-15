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
import java.util.List;
import java.util.Optional;

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

        if (resposta != null && resposta.resultados() != null && !resposta.resultados().isEmpty()) {
            DadosLivro dadosLivro = resposta.resultados().get(0); // Pega o primeiro livro encontrado

            // PRIMEIRO: Verificar se o livro já existe no banco de dados pelo título
            Optional<Livro> livroExistente = livroRepository.findByTituloIgnoreCase(dadosLivro.titulo());
            if (livroExistente.isPresent()) {
                System.out.println("Livro já registrado no banco de dados: " + livroExistente.get().getTitulo());
                // Força a inicialização da coleção de autores enquanto a sessão ainda está ativa
                livroExistente.get().getAutores().size(); // LINHA ADICIONADA/ALTERADA
                return livroExistente;
            }

            // Crie o objeto Livro, mas ainda sem a lista de Autores gerenciados
            Livro livro = new Livro();
            livro.setTitulo(dadosLivro.titulo());

            if (dadosLivro.idiomas() != null && !dadosLivro.idiomas().isEmpty()) {
                livro.setIdioma(dadosLivro.idiomas().get(0));
            } else {
                livro.setIdioma("Idioma Desconhecido");
                System.out.println("Atenção: Idioma não encontrado para o livro '" + dadosLivro.titulo() + "', definido como 'Idioma Desconhecido'.");
            }
            livro.setNumeroDownloads(dadosLivro.numeroDownloads());

            // Processar os autores para garantir que sejam gerenciados ou novos
            List<Autor> autoresGerenciados = new ArrayList<>();
            for (DadosAutor dadosAutor : dadosLivro.autores()) {
                Optional<Autor> autorNoBanco = autorRepository.findByNameContainsIgnoreCase(dadosAutor.nome());

                if (autorNoBanco.isPresent()) {
                    autoresGerenciados.add(autorNoBanco.get());
                } else {
                    Autor novoAutor = new Autor(dadosAutor.nome(), dadosAutor.anoNascimento(), dadosAutor.anoFalecimento());
                    autoresGerenciados.add(novoAutor);
                }
            }
            livro.setAutores(autoresGerenciados);

            // Salva o livro (e os novos autores serão cascateados)
            Livro livroSalvo = livroRepository.save(livro);
            System.out.println("Livro salvo com sucesso: " + livroSalvo.getTitulo());
            // Força a inicialização da coleção de autores para o livro recém-salvo também
            livroSalvo.getAutores().size(); // LINHA ADICIONADA/ALTERADA
            return Optional.of(livroSalvo);

        } else {
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
}