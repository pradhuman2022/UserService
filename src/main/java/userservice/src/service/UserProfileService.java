package userservice.src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import userservice.src.dao.DynamoDb.UserDetailDAO;
import userservice.src.exceptions.ConflictException;
import userservice.src.exceptions.DALException;
import userservice.src.exceptions.UnauthorizedException;
import userservice.src.models.UserDetailDTO;

@Slf4j
@Service
public class UserProfileService {

    @Autowired
    private UserDetailDAO userDetailDAO;

    public UserDetailDTO getUserDetailsFor (@NonNull String userId, @NonNull String userName) throws DALException {
        log.info("user profile service: getUserDetailsFor()");

        UserDetailDTO userDetailDTO = userDetailDAO.getUserDetails(userId, userName);
            
            if (userDetailDTO == null) {
                return null;
            }
        
        log.info(String.format("userdetails %s for userid %s", userDetailDTO, userId));
        return userDetailDTO;
    }

    public void signUpUser(@NonNull UserDetailDTO userDetailDTO) throws DALException, 
                                                    ConflictException, UnauthorizedException {
        log.info("user profile service: signUpUser()");

        
        userDetailDAO.isUserExistOrNot(userDetailDTO.getUserId(), true);
        userDetailDAO.saveUser(userDetailDTO);
        //create node in neptune
    }

    public void updateProfile (@NonNull UserDetailDTO userDetailDTO) throws DALException, 
                                                    ConflictException, UnauthorizedException {
        log.info("user profile service: updateProfile");
        
        userDetailDAO.isUserExistOrNot(userDetailDTO.getUserId(), false);
        userDetailDAO.updateUserDetails(userDetailDTO);
    }

}
