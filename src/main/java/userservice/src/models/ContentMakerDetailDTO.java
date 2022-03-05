package userservice.src.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentMakerDetailDTO {
    @JsonProperty("userContentMaker")
    private boolean userContentMaker;

    @JsonProperty("category")
    private String category;
}