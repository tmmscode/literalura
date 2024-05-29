package tmmscode.literalura.model;

import jakarta.persistence.*;
import tmmscode.literalura.dto.ResultsData;

@Entity
@Table(name = "livros")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 1000)
    private String title;
    @ManyToOne
    private Author author;
    private String language;
    private Long downloadCount;
    private Long apiBookID;

    public Book () {}
    public Book (ResultsData book, Author autor) {
        setTitle(book.title());
        setLanguage(book.languages().getFirst());
        setDownloadCount(book.download_count());
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

    public Long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount) {
        this.downloadCount = downloadCount;
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
                " | Donwloads: " + getDownloadCount() +
                " | ID Gutendex: " + getApiBookID()
                ;
    }
}
