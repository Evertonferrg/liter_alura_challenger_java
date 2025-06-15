package br.com.alura.literAlura.reposirory;

import br.com.alura.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNameContainsIgnoreCase(String nome);
    List<Autor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(Integer ano1, Integer ano2);

    List<Autor> findByNameContainingIgnoreCase(String nome);
    List<Autor> findByNameIgnoreCase(String nome);

    @Query("SELECT a FROM Autor a WHERE a.anoFalecimento IS NULL OR a.anoFalecimento >= :anoAtual")
    List<Autor> findAutoresVivosOuComFalecimentoFuturo(Integer anoAtual);


    // NOVA CONSULTA: Listar autores nascidos em um determinado ano
    List<Autor> findByAnoNascimento(Integer ano);

    // NOVA CONSULTA: Listar autores falecidos em um determinado ano
    List<Autor> findByAnoFalecimento(Integer ano);

    // Opcional, para complementar:
    // Listar autores nascidos antes de um certo ano
    List<Autor> findByAnoNascimentoBefore(Integer ano);

    // Listar autores falecidos depois de um certo ano
    List<Autor> findByAnoFalecimentoAfter(Integer ano);
}
