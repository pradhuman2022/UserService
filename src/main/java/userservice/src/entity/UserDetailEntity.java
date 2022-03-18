package userservice.src.entity;

import static userservice.src.constant.userDetailsTableName;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@DynamoDBTable(tableName= userDetailsTableName)
public class UserDetailEntity {

    @NonNull
    @DynamoDBHashKey(attributeName="UserId")
    private String userId;

    @DynamoDBRangeKey(attributeName="UserName")
    private String userName;

    @DynamoDBAttribute(attributeName = "Bio")
    private String bio;

    @DynamoDBAttribute(attributeName = "CreatedAt")
    private int createdAt;

    @DynamoDBAttribute(attributeName = "LastModified")
    private int lastModified;

    @DynamoDBAttribute(attributeName = "ContentMakerDetail")
    private ContentMakerDetailEntity contentMakerDetail;

    @DynamoDBAttribute(attributeName = "ProfilePicS3")
    private String profilePicS3;

    @DynamoDBAttribute(attributeName = "Email")
    private String email;
}