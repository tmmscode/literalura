package tmmscode.literalura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tmmscode.literalura.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByTitle(String title);

    @Query("SELECT b FROM Book b JOIN b.author a WHERE b.title ILIKE :title AND a.name ILIKE :author")
    Optional<Book> findBookByTitleAndAuthor(String title, String author);

    @Query("SELECT b FROM Book b JOIN b.author a WHERE b.title ILIKE %:stringLike% OR a.name ILIKE %:stringLike% ORDER BY a.name")
    List<Book> findBookByTitleOrAuthorLike(String stringLike);

    @Query("SELECT b FROM Book b WHERE b.language ILIKE :language ORDER BY b.title")
    List<Book> findBooksByLanguage(String language);

    @Query("SELECT COUNT(b) FROM Book b WHERE b.language ILIKE :language")
    Long findNumberOfBooksByLanguage(String language);

    @Query("SELECT DISTINCT b.language FROM Book b ORDER BY b.language")
    List<String> findRegisteredLanguages();

    List<Book> findTop10ByOrderByDownloadCountDesc();
}
