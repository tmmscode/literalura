package tmmscode.literalura.dto;

public record AuthorsData (
        String name,
        int birth_year,
        int death_year
) {
    @Override
    public String toString() {
        return "Nome: " + name +
                " (Ano Nascimento: " + birth_year +
                "| Ano Falecimento: " + death_year +")";
    }
}
