package tmmscode.literalura.main;

import tmmscode.literalura.service.APIRequest;

import java.util.Scanner;

public class Main {
    private final String requestBooksAddress = "https://gutendex.com/books/?search=";
    private final Scanner keyboardInput = new Scanner(System.in);
    private APIRequest apiRequest = new APIRequest();

    public void start() {
        System.out.println("Digite o nome do livro");
        String userBook = keyboardInput.nextLine();
        String searchBook = requestBooksAddress + userBook.replace(" ", "+").toLowerCase();
        String bookJson = apiRequest.getData(searchBook);
        System.out.println(bookJson);
    }
}
