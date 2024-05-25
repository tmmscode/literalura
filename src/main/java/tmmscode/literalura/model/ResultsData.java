package tmmscode.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultsData(
        @JsonAlias("id") Long apiBookID,
        String title,
        List<AuthorsData> authors,
        List<String> languages,
        int download_count
) {
    @Override
    public String toString() {
        return "API ID: " + apiBookID +
                " | Título: " + title +
                " | Autor: " + authors +
                " | Idioma: " + languages +
                " | Downloads: " + download_count;
    }
}
