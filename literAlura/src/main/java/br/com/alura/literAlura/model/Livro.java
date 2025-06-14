package br.com.alura.literAlura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )

    private List<Autor> autores;
    private String idioma;
    private Integer numeroDownloads;

    public Livro() {}

    public Livro(String titulo, List<Autor> autores, String idioma, Integer numeroDownloads) {
        this.titulo = titulo;
        this.autores = autores;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
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
        this.autores = autores;
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
        return "\n📘 Livro: " + titulo +
                "\n📚 Autor(es): " + (autores != null ? autores.toString() : "N/A") +
                "\n🌐 Idioma: " + idioma +
                "\n⬇️ Downloads: " + numeroDownloads;
    }
}
