package tmmscode.literalura.main;

import tmmscode.literalura.dto.APIResponseData;
import tmmscode.literalura.dto.ResultsData;
import tmmscode.literalura.helpers.MenuPrinter;
import tmmscode.literalura.model.Author;
import tmmscode.literalura.model.Book;
import tmmscode.literalura.repository.AuthorRepository;
import tmmscode.literalura.repository.BookRepository;
import tmmscode.literalura.service.APIRequest;
import tmmscode.literalura.service.ConvertData;

import java.time.LocalDate;
import java.util.*;

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
                case "4" : showLivingAuthorsByYear();
                    break;
                case "5" : showBooksByLanguage();
                    break;
                case "6" : searchInRegistry();
                    break;
                case "7" : getTop10Donwloads();
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

    private void getTop10Donwloads() {
        List<Book> top10downloads = bookRepository.findTop10ByOrderByDownloadCountDesc();
        top10downloads.forEach(b -> {
            System.out.print("Total downloads: " + b.getDownloadCount() + " | Título: " + b.getTitle() + " | Autor: " + b.getAuthor().getName() +"\n");
        });
        MenuPrinter.showEndLine();
    }

    private void searchInRegistry() {
        System.out.println("Digite um trecho do livro ou do nome do autor");
        String anySearch = keyboardInput.nextLine();
        List<Book> mixedList = bookRepository.findBookByTitleOrAuthorLike(anySearch);

        mixedList.forEach(System.out::println);
        MenuPrinter.showEndLine();
    }

    private void searchBookByTitle() {
        searchingBooks = true;
        while(searchingBooks) {

            System.out.println("Digite o nome do livro");
            String userBook = keyboardInput.nextLine();
            String searchAddress = requestBooksAddress + userBook.replace(" ", "+").toLowerCase();
            String bookJson = apiRequest.getData(searchAddress);

            APIResponseData booksSearched = dataConverter.getData(bookJson, APIResponseData.class);

            if (booksSearched.count() == 0) {
                System.out.println("A busca não obteve resultados");
                MenuPrinter.showEndLine();
                searchingBooks = false;
            } else if (booksSearched.count() == 1) {
                ResultsData book = booksSearched.bookAndAuthorInfo().getFirst();
                verifyAndIncludeNewBook(book);
            } else if (booksSearched.count() <= 30) {
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

                            if (userBookId.equals("0")) {
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
                                MenuPrinter.showEndLine();
                            }
                            break;
                        case "0":
                            System.out.println("Busca cancelada. Retornando ao menu inicial...");
                            MenuPrinter.showEndLine();
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
                MenuPrinter.showEndLine();
            }
        }
    }

    private void showAllRegisteredBooks() {
        List<Book> allBooks = bookRepository.findAll();

        if(allBooks.isEmpty()) {
            System.out.println("Nenhum livro registrado");
            MenuPrinter.showEndLine();
        } else {
            allBooks.forEach(System.out::println);
            MenuPrinter.showEndLine();
        }
    }

    private void showAllRegisteredAuthors() {
        List<Author> allAuthors = authorRepository.findAll();

        if(allAuthors.isEmpty()) {
            System.out.println("Nenhum autor registrado");
            MenuPrinter.showEndLine();
        } else {
            allAuthors.forEach(System.out::println);
            MenuPrinter.showEndLine();
        }
    }

    private void showLivingAuthorsByYear() {
        while(true){
            try {
                boolean searching = true;
                while(searching) {
                    int thisYear = LocalDate.now().getYear();
                    System.out.println("Digite o ano para busca");
                    int typedYear = Integer.parseInt(keyboardInput.nextLine());

                    if(typedYear > thisYear){
                        System.out.println("O ano informado é maior do que o ano atual");
                        MenuPrinter.showEndLine();
                    } else {
                        List<Author> allLivingAuthors = authorRepository.findLivingAuthorsByYear(typedYear);
                        if(!allLivingAuthors.isEmpty()){
                            System.out.println("Autores vivos nesse período:\n");
                            allLivingAuthors.forEach(System.out::println);
                            MenuPrinter.showEndLine();
                            searching = false;
                        } else {
                            System.out.println("Dentre os autores registrados, nenhum estava vivo nesse período");
                            MenuPrinter.showEndLine();
                            searching = false;
                        }
                    }
                }
                return;
            } catch (NumberFormatException e) {
                System.out.println("Digite apenas números para o ano (sem letras ou símbolos)");
            }


        }

    }

    private void showTotalBooksByLanguage(){
        List<String> languages = bookRepository.findRegisteredLanguages();

        if(!languages.isEmpty()) {
            Map<String, Long> languageMap = new HashMap<>();

            languages.forEach(lang -> {
                Long qnt = bookRepository.findNumberOfBooksByLanguage(lang);
                languageMap.put(lang, qnt);
                });

            List<Map.Entry<String, Long>> langEntry = new ArrayList<>(languageMap.entrySet());

            langEntry.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

            System.out.println("Idiomas disponíveis:\n");
            langEntry.forEach(entry -> {
                String lang = entry.getKey();
                Long qnt = entry.getValue();
                String txt = qnt > 1 ? "livros registrados" : "livro registrado";
                System.out.printf("[%s] - %s %s.%n", lang, qnt, txt);
            });
        } else {
            System.out.println("Não há livros registrados");
            MenuPrinter.showEndLine();
        }
    }

    private void showBooksByLanguage() {
        showTotalBooksByLanguage();
        System.out.println("\nDigite as iniciais do idioma para listar os livros (ou digite \"0\" para sair)");

        String selectedLanguage = keyboardInput.nextLine().toLowerCase();
        if(selectedLanguage.equals("0")){
            MenuPrinter.showEndLine();
            return;
        }

        List<String> languages = bookRepository.findRegisteredLanguages();
        List<String> filtered = languages.stream().filter(l -> l.equals(selectedLanguage)).toList();

        if(!filtered.isEmpty()) {
            List<Book> languageBooks = bookRepository.findBooksByLanguage(selectedLanguage);
            languageBooks.forEach(System.out::println);
            MenuPrinter.showEndLine();
        } else {
            System.out.println("Não foram encontrados livros desse idioma");
            MenuPrinter.showEndLine();
        }
    }

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
        if(!isRegistered(searchedBook)){
            Book newBook;

            if (!searchedBook.authors().isEmpty()) {
                Optional<Author> dbAuthor = authorRepository.findAuthorByName(searchedBook.authors().getFirst().name());

                if (dbAuthor.isPresent()) {
                    newBook = new Book(searchedBook, dbAuthor.get());
                    bookRepository.save(newBook);
                    System.out.printf("Novo livro registrado: %s | Autor: %s\n", newBook.getTitle(), dbAuthor.get().getName());
                } else {
                    Author newAuthor = new Author(searchedBook.authors().getFirst());
                    authorRepository.save(newAuthor);
                    System.out.println("Novo autor adicionado: " + newAuthor.getName());

                    Optional<Author> newDBAuthor = authorRepository.findAuthorByName(newAuthor.getName());
                    newBook = new Book(searchedBook, newDBAuthor.get());
                    bookRepository.save(newBook);
                    System.out.printf("Novo livro adicionado: %s | Autor: %s\n", newBook.getTitle(), newDBAuthor.get().getName());
                }
            } else {
                Author newUnknownAuthor = new Author("");

                Optional<Author> unknownAuthor = authorRepository.findAuthorByName(newUnknownAuthor.getName());

                if(unknownAuthor.isPresent()) {
                    newBook = new Book(searchedBook, unknownAuthor.get());
                    bookRepository.save(newBook);
                    System.out.printf("Novo livro adicionado: %s | Autor: %s\n", newBook.getTitle(), unknownAuthor.get().getName());
                } else {
                    authorRepository.save(newUnknownAuthor);

                    Optional<Author> newDBUnknownAuthor = authorRepository.findAuthorByName(newUnknownAuthor.getName());
                    newBook = new Book(searchedBook, newDBUnknownAuthor.get());
                    bookRepository.save(newBook);
                    System.out.printf("Novo livro adicionado: %s | Autor: %s\n", newBook.getTitle(), newDBUnknownAuthor.get().getName());
                }
            }
            MenuPrinter.showEndLine();
        }
    }

    private boolean isRegistered(ResultsData searchedBook) {
        Optional<Book> dbBook = bookRepository.findBookByTitle(searchedBook.title());

        if(dbBook.isPresent()) {
            if (!searchedBook.authors().isEmpty()) {
                Optional<Author> dbAuthor = authorRepository.findAuthorByName(searchedBook.authors().getFirst().name());
                if(dbAuthor.isPresent()){
                    Optional<Book> foundBook = bookRepository.findBookByTitleAndAuthor(searchedBook.title(), searchedBook.authors().getFirst().name());
                    if (foundBook.isPresent()){
                        System.out.printf("Este livro já está registrado: (Título: %s | Autor: %s)\n", foundBook.get().getTitle(), foundBook.get().getAuthor().getName());
                        MenuPrinter.showEndLine();
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                Author namelessAuthor = new Author("");
                Optional<Author> dbNamelessAuthor = authorRepository.findAuthorByName(namelessAuthor.getName());
                if(dbNamelessAuthor.isPresent()){
                    Optional<Book> foundBook = bookRepository.findBookByTitleAndAuthor(searchedBook.title(), namelessAuthor.getName());
                    if (foundBook.isPresent()){
                        System.out.printf("Este livro já está registrado: (Título: %s | Autor: %s)\n", foundBook.get().getTitle(), foundBook.get().getAuthor().getName());
                        MenuPrinter.showEndLine();
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
