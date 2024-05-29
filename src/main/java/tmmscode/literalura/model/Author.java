package tmmscode.literalura.model;

import jakarta.persistence.*;
import tmmscode.literalura.dto.AuthorsData;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int birthYear;
    private int deathYear;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Author() {}
    public Author(String name){
        setName(name);
    }
    public Author(AuthorsData author) {
        setName(author.name());
        setBirthYear(author.birth_year());
        setDeathYear(author.death_year());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isBlank()) {
            this.name = "Autor Desconhecido";
        } else {
            this.name = name;
        }
    }

    public int getBirthYear() {
        return this.birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getDeathYear() {
        return this.deathYear;
    }

    public void setDeathYear(int deathYear) {
        this.deathYear = deathYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    public void setBook(Book books) {
        this.books.add(books);
    }

    @Override
    public String toString() {
        return "Nome: " + this.getName() +
                " (Ano de Nascimento: " + this.getBirthYear() +
                " | Ano de Falecimento: " + this.getDeathYear() + ")"
                ;
    }
}
