package br.com.alura.literAlura.service;

import br.com.alura.literAlura.model.DadosLivro;
import br.com.alura.literAlura.model.Livro;
import br.com.alura.literAlura.reposirory.LivroRepository;

import java.util.Optional;

public class GutenService {

    private final LivroRepository livroRepository;
    private final ConversorDeDados conversorDeDados;
    private final ConverteDados converteDados;
    private final ConsumoApi consumoApi;

    private final String URL_BASE = "https://gutendex.com/books/?searc=";

    public GutenService(LivroRepository livroRepository,
                        ConversorDeDados conversorDeDados,
                        ConverteDados converteDados,
                        ConsumoApi consumoApi) {
        this.livroRepository = livroRepository;
        this.conversorDeDados = conversorDeDados;
        this.converteDados = converteDados;
        this.consumoApi = consumoApi;
    }

    public Optional<Livro> buscarLivroPorTitulo(String titulo) {
        String url = URL_BASE + titulo.replace(" ", "%20");
        String json = consumoApi.obterDados(url);

        var resultado = converteDados.obterDados(json, ResultadoBusca.class);


        if (resultado != null && !resultado.results().isEmpty()) {
            DadosLivro dadosLivro = resultado.results().get(0);
            Livro livro = conversorDeDados.converter(dadosLivro);
            Livro livroSalvo = livroRepository.save(livro);
            return Optional.of(livroSalvo);
        } else {
            System.out.println("Livro não encontrado na API.");
            return Optional.empty();
        }

    }
}
