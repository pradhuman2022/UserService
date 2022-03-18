package userservice.src.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailDTO {
    
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("createdAt")
    private int createdAt;

    @JsonProperty("lastModified")
    private int lastModified;

    @JsonProperty("contentMakerDetail")
    private ContentMakerDetailDTO contentMakerDetailDTO;

    @JsonProperty("profilePicS3")
    private String profilePicS3;

    @JsonProperty("email")
    private String email;
}
