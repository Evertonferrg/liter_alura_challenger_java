package br.com.alura.literAlura.reposirory;

import br.com.alura.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    List<Autor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(Integer ano1, Integer ano2);

    List<Autor> findByNameContainingIgnoreCase(String nome);


}
