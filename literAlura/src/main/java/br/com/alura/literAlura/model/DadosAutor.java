package br.com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutor(@JsonAlias("name") String nome,
                         @JsonAlias("birth_year") Integer anoNascimento,
                         @JsonAlias("death_year") Integer anoFalecimento) {

    @Override
    public String toString () {
        return "Nome: " + nome +
                ", Ano de Nascimento: " + (anoNascimento != null ? anoNascimento : "N/A") +
                ", Ano de Falecimento: " + (anoFalecimento != null ? anoFalecimento : "N/A");
    }
}
