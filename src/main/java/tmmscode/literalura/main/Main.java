package tmmscode.literalura.main;

import tmmscode.literalura.helpers.MenuPrinter;
import tmmscode.literalura.model.APIResponseData;
import tmmscode.literalura.model.ResultsData;
import tmmscode.literalura.service.APIRequest;
import tmmscode.literalura.service.ConvertData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final String requestBooksAddress = "https://gutendex.com/books/?search=";
    private final String requestBooksByIdAddress = "https://gutendex.com/books/?ids=";
    private final Scanner keyboardInput = new Scanner(System.in);
    private final APIRequest apiRequest = new APIRequest();
    private final ConvertData dataConverter = new ConvertData();

    private List<ResultsData> searchedBooks = new ArrayList<>();
    boolean searchingBooks;


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
        if (!searchedBooks.isEmpty()) {
            MenuPrinter.showEndLine();
            System.out.println("Lista de livros registrados");
            searchedBooks.forEach(System.out::println);
        } else {
            MenuPrinter.showEndLine();
            System.out.println("Não há livros registrados");

        }

    }

    private void showAllRegisteredAuthors() {}

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
                    searchedBooks.add(bookFound);
                    System.out.println("Livro adicionado!");
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
}
