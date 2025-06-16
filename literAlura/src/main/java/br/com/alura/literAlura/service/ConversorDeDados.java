package br.com.alura.literAlura.service;

import br.com.alura.literAlura.model.Autor;
import br.com.alura.literAlura.model.DadosAutor;
import br.com.alura.literAlura.model.DadosLivro;
import br.com.alura.literAlura.model.Livro;
import br.com.alura.literAlura.reposirory.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConversorDeDados {

    private final AutorRepository autorRepository;

    public ConversorDeDados(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public Livro converter(DadosLivro dadosLivro) {
        List<Autor> autoresConvertidos = new ArrayList<>();

        for (DadosAutor dadosAutor : dadosLivro.autores()) {
            // Verifica se o autor já existe pelo nome
            Optional<Autor> autorExistente = autorRepository
                    .findByNameContainingIgnoreCase(dadosAutor.nome())
                    .stream()
                    .filter(a -> a.getName().equalsIgnoreCase(dadosAutor.nome()))
                    .findFirst();

            Autor autor;
            if (autorExistente.isPresent()) {
                autor = autorExistente.get();
            } else {
                autor = new Autor(
                        dadosAutor.nome(),
                        dadosAutor.anoNascimento(),
                        dadosAutor.anoFalecimento()
                );
                autor = autorRepository.save(autor);
            }

            autoresConvertidos.add(autor);
        }

        // Pega o primeiro idioma da lista (padrão da API)
        String idioma = dadosLivro.idiomas() != null && !dadosLivro.idiomas().isEmpty()
                ? dadosLivro.idiomas().get(0)
                : "N/A";

        String resumo = null;
        if (dadosLivro.sumarios() != null && !dadosLivro.sumarios().isEmpty()) {
            resumo = String.join("\n\n", dadosLivro.sumarios());
        }
        return new Livro(
                dadosLivro.titulo(),
                autoresConvertidos,
                idioma,
                dadosLivro.numeroDownloads(),
                resumo
        );
    }
}