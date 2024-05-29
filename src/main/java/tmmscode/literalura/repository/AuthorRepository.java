package tmmscode.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tmmscode.literalura.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findAuthorByName(String name);

    @Query("SELECT a FROM Author a WHERE :year BETWEEN a.birthYear AND a.deathYear ORDER BY a.birthYear")
    List<Author> findLivingAuthorsByYear(Integer year);
}
