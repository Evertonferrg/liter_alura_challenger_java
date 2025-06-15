package br.com.alura.literAlura.reposirory;

import br.com.alura.literAlura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByIdiomaIgnoreCase(String idioma);
    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    Optional<Livro> findByTituloIgnoreCase(String titulo);

    List<Livro> findByIdiomaContainsIgnoreCase(String idioma);
}
