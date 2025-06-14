package br.com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record RespostaApi(@JsonAlias("count") Integer count,
                          @JsonAlias("next") String next,
                          @JsonAlias("previous") String previous,
                          @JsonAlias("results") List<DadosLivro> resultados) {
}
