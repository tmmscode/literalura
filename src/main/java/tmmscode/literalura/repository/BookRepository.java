package tmmscode.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tmmscode.literalura.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
//    List<Book>
}
