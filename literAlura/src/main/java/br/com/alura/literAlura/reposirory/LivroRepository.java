package br.com.alura.literAlura.reposirory;

import br.com.alura.literAlura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByIdiomaIgnoreCase(String idioma);
    List<Livro> findByTituloContainingIgnoreCase(String titulo);

   List<Livro> findByIdiomasContains(String idioma);
}
