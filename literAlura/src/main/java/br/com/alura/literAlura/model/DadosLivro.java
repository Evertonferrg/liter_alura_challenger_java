package br.com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("title")String titulo,
                         @JsonAlias("authors") List<DadosAutor> autores,
                         @JsonAlias("languages")List<String> idiomas,
                         @JsonAlias("download_count") Integer numeroDownloads,
                         @JsonAlias("summaries") List<String> sumarios){
                        // @JsonAlias("id") Integer id) {

    @Override
    public String toString () {
        String autoresFormatados = "Desconhecido";
        if (autores != null && !autores.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < autores.size(); i++) {
                sb.append(autores.get(i).nome()); // Pega apenas o nome do autor para a exibição principal
                if (i < autores.size() - 1) {
                    sb.append(", ");
                }
            }
            autoresFormatados = sb.toString();
        }
        String idiomasFormatados = "Desconhecido";
        if (idiomas != null && !idiomas.isEmpty()) {
            idiomasFormatados = String.join(", ", idiomas);
        }

        return "-------------Livro-------------" +
                "\nTitulo: " + titulo +
                "\nAutor(es): " + autoresFormatados +
                "\nIdiomas(s): " +idiomasFormatados +
                "\nNúmero de Downloads: " + (numeroDownloads != null ? numeroDownloads : "N/A") +
                "\n-----------------------------";
    }

}
