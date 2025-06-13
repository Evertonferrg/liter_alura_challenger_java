package br.com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("title")String titulo,
                         @JsonAlias("authors")String autor,
                         @JsonAlias("languages")String linguagem,
                         @JsonAlias("downloaaad_count") Integer numeroDownloads){
                        // @JsonAlias("id") Integer id) {
}
