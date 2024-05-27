package tmmscode.literalura.helpers;

public class MenuPrinter {
    public static void showTitle () {
        System.out.print("""
            =========================================================================
            |                              LiterAlura                               |
            =========================================================================
            """);
    }

    public static void showEndLine () {
        System.out.println("=========================================================================");
    }

    public static void showMainMenuOptions () {
        System.out.print("""
            ♦♦♦♦♦ MENU PRINCIPAL ♦♦♦♦♦
            Escolha uma opção:
            
            1 - Buscar livros pelo título
            2 - Listar livros registrados
            3 - Listar autores registrados
            4 - Listar autores vivos em um determinado ano
            5 - Listar livros em um determinado idioma
            
            9 - Testar função (remover depois)
            
            0 - Sair
            """);
    }

    public static void showMultipleBooksOptions() {
        System.out.print("""
            1 - Ver a lista dos livros retornados na busca (Encontrar livro por ID)
            
            0 - Sair
            """);
    }
}
