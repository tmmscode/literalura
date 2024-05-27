package tmmscode.literalura.main;

import tmmscode.literalura.helpers.MenuPrinter;
import tmmscode.literalura.model.APIResponseData;
import tmmscode.literalura.model.Author;
import tmmscode.literalura.model.Book;
import tmmscode.literalura.model.ResultsData;
import tmmscode.literalura.repository.AuthorRepository;
import tmmscode.literalura.repository.BookRepository;
import tmmscode.literalura.service.APIRequest;
import tmmscode.literalura.service.ConvertData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private final String requestBooksAddress = "https://gutendex.com/books/?search=";
    private final String requestBooksByIdAddress = "https://gutendex.com/books/?ids=";
    private final Scanner keyboardInput = new Scanner(System.in);
    private final APIRequest apiRequest = new APIRequest();
    private final ConvertData dataConverter = new ConvertData();

    boolean searchingBooks;

    public Main(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


    public void start() {
        MenuPrinter.showTitle();

        while (true){
            MenuPrinter.showMainMenuOptions();

            String selectedOption = keyboardInput.nextLine();

            switch (selectedOption) {
                case "1" : searchBookByTitle();
                    break;
                case "2" : showAllRegisteredBooks();
                    break;
                case "3" : showAllRegisteredAuthors();
                    break;
                case "4" : showAliveAuthorsByYear();
                    break;
                case "5" : showBooksByLanguage();
                    break;
                case "9" :
                    testFunction();
                    break;
                case "0" :
                    System.out.println("Aplicação feita por Thiago de Melo Marçal da Silva, como desafio da formação \"Oracle Next Education\" (Especialização Back-End)");
                    System.out.println("Obrigado por usar esta aplicação! ♥");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void testFunction() {
        System.out.println("Nothing to test");
    }

    private void searchBookByTitle() {
        searchingBooks = true;
        while(searchingBooks) {

            System.out.println("Digite o nome do livro");
            String userBook = keyboardInput.nextLine();
            String searchAddress = requestBooksAddress + userBook.replace(" ", "+").toLowerCase();
            String bookJson = apiRequest.getData(searchAddress);

            APIResponseData booksSearched = dataConverter.getData(bookJson, APIResponseData.class);

            if (booksSearched.count() == 1) {
                ResultsData book = booksSearched.bookAndAuthorInfo().getFirst();
                verifyAndIncludeNewBook(book);
            } else if (booksSearched.count() <= 20) {
                System.out.printf("""
                    A busca retornou com %s resultados
                    
                    """, booksSearched.count());

                while (searchingBooks) {
                    MenuPrinter.showMultipleBooksOptions();
                    String selectedOption = keyboardInput.nextLine();

                    switch (selectedOption) {
                        case "1":
                            System.out.println("Livros encontrados: \n");
                            booksSearched.bookAndAuthorInfo().forEach(System.out::println);

                            System.out.println("\nDigite o ID do livro que deseja selecionar (Ou digite \"0\" para sair)");
                            String userBookId = keyboardInput.nextLine();

                            if(userBookId.equals("0")) {
                                searchingBooks = false;
                                break;
                            }

                            try {
                                Long bookIdLong = Long.parseLong(userBookId);

                                List<ResultsData> selectedBookTry = booksSearched.bookAndAuthorInfo().stream()
                                        .filter(b -> b.apiBookID().equals(bookIdLong)).toList();

                                if (selectedBookTry.isEmpty()) {
                                    System.out.println("O ID informado não consta na lista exibida");
                                } else {
                                    String searchAddressId = requestBooksByIdAddress + userBookId.trim();
                                    String specificBook = apiRequest.getData(searchAddressId);

                                    APIResponseData selectedBookResponse = dataConverter.getData(specificBook, APIResponseData.class);
                                    ResultsData selectedBook = selectedBookResponse.bookAndAuthorInfo().getFirst();

                                    verifyAndIncludeNewBook(selectedBook);
                                }
                            } catch (Exception e) {
                                System.out.println("Não foi possível selecionar o livro pelo ID informado" +
                                        "\nVerifique a lista novamente ou realize uma nova busca");
                            }
                            break;
                        case "0":
                            System.out.println("Busca cancelada. Retornando ao menu inicial...");
                            searchingBooks = false;
                            break;
                        default:
                            System.out.println("Opção inválida!");
                    }
                }
            } else {
                System.out.printf("""
                    A busca retornou com %s resultados e não será possível auxiliar na filtragem.
                    Realize uma nova busca com mais detalhes, como nome do autor ou título completo do livro.
                    """, booksSearched.count());
            }
        }
    }

    private void showAllRegisteredBooks() {
        List<Book> allBooks = bookRepository.findAll();

        if(allBooks.isEmpty()) {
            System.out.println("Nenhum livro registrado");
        } else {
            allBooks.forEach(System.out::println);
        }
    }

    private void showAllRegisteredAuthors() {
        List<Author> allAuthors = authorRepository.findAll();

        if(allAuthors.isEmpty()) {
            System.out.println("Nenhum autor registrado");
        } else {
            allAuthors.forEach(System.out::println);
        }
    }

    private void showAliveAuthorsByYear() {}

    private void showBooksByLanguage() {}


    private void verifyAndIncludeNewBook(ResultsData bookFound) {
        boolean userCheck = true;

        while(userCheck) {
            System.out.println(bookFound);
            System.out.println("Esse livro está correto? (S/N)");
            String userVerification = keyboardInput.nextLine().toLowerCase();

            switch (userVerification){
                case "s" :
                    registerNewBook(bookFound);
                    userCheck = false;
                    searchingBooks = false;
                    break;
                case "n":
                    System.out.println("Realize uma nova busca");
                    userCheck = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void registerNewBook(ResultsData searchedBook) {
        Optional<Book> dbBook = bookRepository.findByTitle(searchedBook.title());

        if(dbBook.isPresent()){
            System.out.println("Esse livro já foi registrado! " + dbBook.get().getTitle());
        } else {
            Book newBook;

            if (!searchedBook.authors().isEmpty()) {
                Optional<Author> dbAuthor = authorRepository.findByName(searchedBook.authors().getFirst().name());

                if (dbAuthor.isPresent()) {
                    newBook = new Book(searchedBook, dbAuthor.get());
                    bookRepository.save(newBook);
                    System.out.printf("Novo livro registrado: %s | Autor: %s\n", newBook.getTitle(), dbAuthor.get().getName());
                } else {
                    Author newAuthor = new Author(searchedBook.authors().getFirst());
                    authorRepository.save(newAuthor);
                    System.out.println("Novo autor adicionado: " + newAuthor.getName());

                    Optional<Author> newDBAuthor = authorRepository.findByName(newAuthor.getName());
                    newBook = new Book(searchedBook, newDBAuthor.get());
                    bookRepository.save(newBook);
                    System.out.printf("Novo livro adicionado: %s | Autor: %s\n", newBook.getTitle(), newDBAuthor.get().getName());
                }
            } else {
                Author newUnknownAuthor = new Author();
                newUnknownAuthor.setName("");

                Optional<Author> unknownAuthor = authorRepository.findByName(newUnknownAuthor.getName());

                if(unknownAuthor.isPresent()) {
                    newBook = new Book(searchedBook, unknownAuthor.get());
                    bookRepository.save(newBook);
                    System.out.printf("Novo livro adicionado: %s | Autor: %s\n", newBook.getTitle(), unknownAuthor.get().getName());
                } else {
                    authorRepository.save(newUnknownAuthor);

                    Optional<Author> newDBUnknownAuthor = authorRepository.findByName(newUnknownAuthor.getName());
                    newBook = new Book(searchedBook, newDBUnknownAuthor.get());
                    bookRepository.save(newBook);
                    System.out.printf("Novo livro adicionado: %s | Autor: %s\n", newBook.getTitle(), newDBUnknownAuthor.get().getName());
                }
            }
        }
    }
}
