package tmmscode.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record APIResponseData(
        int count,
        @JsonAlias("results") List<ResultsData> bookAuthorInfo

) {
}
