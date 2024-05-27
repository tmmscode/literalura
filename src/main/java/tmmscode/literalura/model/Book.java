package tmmscode.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros", uniqueConstraints = {@UniqueConstraint(columnNames = "title")})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 1000)
    private String title;
    @ManyToOne
    private Author author;
    private String language;
    private Long download_count;
    private Long apiBookID;

    public Book () {}
    public Book (ResultsData book, Author autor) {
        setTitle(book.title());
        setLanguage(book.languages().getFirst());
        setDownload_count(book.download_count());
        setApiBookID(book.apiBookID());

        setAuthor(autor);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getDownload_count() {
        return download_count;
    }

    public void setDownload_count(Long download_count) {
        this.download_count = download_count;
    }

    public Long getApiBookID() {
        return apiBookID;
    }

    public void setApiBookID(Long apiBookID) {
        this.apiBookID = apiBookID;
    }

    @Override
    public String toString() {
        return "Título: " + getTitle() +
                " | Autor: " + getAuthor().getName() +
                " | Idioma: " + getLanguage() +
                " | Donwloads: " + getDownload_count() +
                " | ID Gutendex: " + getApiBookID()
                ;
    }
}
