package userservice.src.dao.DynamoDb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static userservice.src.constant.userDetailsTableName;

import java.util.Iterator;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import userservice.src.entity.UserDetailEntity;
import userservice.src.exceptions.ConflictException;
import userservice.src.exceptions.DALException;
import userservice.src.exceptions.UnauthorizedException;
import userservice.src.models.UserDetailDTO;

@Slf4j
@Repository
public class UserDetailDAO {

    @Autowired
    DynamoDBMapper dBMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DynamoDB dynamoDB;

    //save user detail for first time
    public void saveUser(@NonNull UserDetailDTO userDetailDto) throws DALException {

        try {
            UserDetailEntity userDetailEntity = objectMapper.convertValue(userDetailDto, UserDetailEntity.class);
            dBMapper.save(userDetailEntity);
        } catch (Exception e) {
            log.info (String.format("not able to save profile for userid %s", userDetailDto.getUserId()));

            log.error (String.format("Error %s occurred while saving new user profile in DAO", e));

            throw new DALException(e.getMessage());
        }
    }

    //update details of user
    public void updateUserDetails (@NonNull UserDetailDTO userDetailDto) throws DALException {

        try {
            UserDetailEntity userDetailEntity = objectMapper.convertValue(userDetailDto, UserDetailEntity.class);
            dBMapper.save(userDetailEntity, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));
        } catch(Exception e) {
            log.info (String.format("not able to update profile for userid %s", userDetailDto.getUserId()));

            log.error (String.format("Error %s occurred while updating user profile in DAO", e));

            throw new DALException(e.getMessage());
        }
    }

    //get user details
    public UserDetailDTO getUserDetails (@NonNull String userId, @NonNull String userName) throws DALException {
        UserDetailDTO userDetailDTO = null;

        try {
            UserDetailEntity userDetailEntity = dBMapper.load(UserDetailEntity.class, userId, userName);
            userDetailDTO =  objectMapper.convertValue(userDetailEntity, UserDetailDTO.class);
        } catch (Exception e) {

            log.info (String.format("not able to get user details for userid %s", userId));

            log.error (String.format("Error %s occurred while getting user profile in DAO", e));

            throw new DALException(e.getMessage());
        }

        return userDetailDTO;
    }

    //delete user from db
    public void deleteUser(@NonNull UserDetailDTO userDetailDTO) throws DALException {
        
        try {
            UserDetailEntity userDetailEntity = objectMapper.convertValue(userDetailDTO, UserDetailEntity.class);
            dBMapper.delete(userDetailEntity);
        } catch (Exception e) {
            throw new DALException(e.getMessage());
        }
    }

    public void isUserExistOrNot (@NonNull String userId, boolean isUserExistCheck) throws DALException,
                                                                        ConflictException, UnauthorizedException {

        Iterator<Item> iterator;
        try {
            Table table = dynamoDB.getTable(userDetailsTableName);
            
            QuerySpec spec = new QuerySpec().withKeyConditionExpression("UserId = :user_id")
                .withValueMap(new ValueMap().withString(":user_id", userId));

            ItemCollection<QueryOutcome> items = table.query(spec);

            iterator = items.iterator();
    
        } catch (Exception e) {

            log.info (String.format("not able to query for userid %s", userId));

            log.error (String.format("Error %s occurred while query for user profile in DAO", e));

            throw new DALException (e.getMessage());
        }

        if (isUserExistCheck) {
            if (iterator.hasNext()) {
                throw new ConflictException();
            }
        } else {
            if (!iterator.hasNext()) {
                throw new UnauthorizedException();
            }
        }
    }
}
