package br.com.alura.literAlura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )

    private List<Autor> autores;
    private String idioma;
    private Integer numeroDownloads;

    @Column(columnDefinition = "TEXT")
    private String resumo;

    public Livro() {}

    public Livro(String titulo, List<Autor> autores, String idioma, Integer numeroDownloads, String resumo) {
        this.titulo = titulo;
        this.autores = autores;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
        this.resumo = resumo;
        setAutores(autores);
    }
    public Livro(String titulo, List<Autor> autores, String idioma, Integer numeroDownloads){
        this(titulo, autores, idioma, numeroDownloads, null);
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {

        if(autores == null){
            this.autores = new  ArrayList<>();
            return;
        }
        if(this.autores == null) {
            this.autores = new ArrayList<>();
        }
        this.autores.clear();
        autores.forEach(autor -> {
            if (autor.getLivros() == null){
                autor.setLivros(new ArrayList<>());
            }
            if (!autor.getLivros().contains(this)){
                autor.getLivros().add(this);
            }
            this.autores.add(autor);
        });
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(Integer numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }



    @Override
    public String toString() {
        // Prepara a string de nomes de autores
        String nomesAutores = autores != null && !autores.isEmpty() ?
                autores.stream()
                        .map(Autor::getName) // Supondo que Autor tem um método getName()
                        .collect(Collectors.joining(", ")) :
                "N/A";

        // Prepara a string do resumo, com tratamento para nulo ou vazio
        String resumoFormatado = (resumo != null && !resumo.trim().isEmpty()) ? resumo : "Sumário não disponível.";

        // Retorna a string formatada usando Text Blocks e o método .formatted()
        return """
                ----- 📘 LIVRO -----
                Título: %s
                📚 Autor(es): %s
                🌐 Idioma: %s
                ⬇️ Downloads: %d
                📝 Sumário: %s
                ---------------------\n
                """.formatted(titulo, nomesAutores, idioma, numeroDownloads, resumoFormatado);
    }
}

