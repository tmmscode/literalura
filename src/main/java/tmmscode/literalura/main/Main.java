package tmmscode.literalura.main;

import tmmscode.literalura.model.APIResponseData;
import tmmscode.literalura.service.APIRequest;
import tmmscode.literalura.service.ConvertData;

import java.util.Scanner;

public class Main {
    private final String requestBooksAddress = "https://gutendex.com/books/?search=";
    private final Scanner keyboardInput = new Scanner(System.in);
    private final APIRequest apiRequest = new APIRequest();
    private final ConvertData dataConverter = new ConvertData();

    public void start() {
        System.out.println("Digite o nome do livro");
        String userBook = keyboardInput.nextLine();
        String searchBook = requestBooksAddress + userBook.replace(" ", "+").toLowerCase();
        String bookJson = apiRequest.getData(searchBook);
        System.out.println(bookJson);

        APIResponseData fullInfo = dataConverter.getData(bookJson, APIResponseData.class);
        System.out.println("Tem isso de livros: " + fullInfo.count());
        if (fullInfo.count() > 1) {
            System.out.printf("""
                    A busca retornou com mais de um resultado (%s)
                    """
                    , fullInfo.count());

            System.out.println(fullInfo);
        } else {
            System.out.println("Só tem um resultado (ou nenhum)");
            System.out.println(fullInfo);
        }

    }
}
