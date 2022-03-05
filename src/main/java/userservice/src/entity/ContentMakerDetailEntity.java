package userservice.src.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamoDBDocument
public class ContentMakerDetailEntity {
    
    @DynamoDBAttribute(attributeName = "UserContentMaker")
    private boolean userContentMaker;
    
    @DynamoDBAttribute(attributeName = "Category")
    private String category;
}