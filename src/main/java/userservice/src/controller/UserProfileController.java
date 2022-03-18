package userservice.src.controller;

import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import userservice.src.exceptions.ConflictException;
import userservice.src.exceptions.DALException;
import userservice.src.exceptions.UnauthorizedException;
import userservice.src.models.UserDetailDTO;
import userservice.src.security.Secure;
import userservice.src.service.UserProfileService;


@Slf4j
@RestController
@RequestMapping("user-detail")
public class UserProfileController {

    @Autowired
    private UserProfileService userDetailService;

    @Autowired
    private Secure secure;
    
    /**
     * Get user details from persisentce datastore
     * @param id
     * @param userName
     * @return UserDetailDTO
     */
    @GetMapping(value = "/getUserDetails/{id}/{userName}", 
    produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDetailDTO getUserDetails(@NonNull @PathVariable("id") String id, 
                                        @NonNull @PathVariable("userName") String userName) {
        log.info("get user details for userid %s", id);
        
        try {
            //encryption
            return userDetailService.getUserDetailsFor(id, userName);
        } catch (DALException e) {
            throw new ResponseStatusException(HttpStatus.SC_INTERNAL_SERVER_ERROR, 
                String.format("not able to get details, please try again %s", id), new DALException());
        }
    }
    
    /**
     * Create new user in persistent datastore
     * @param userDetailDTO
     * @return Response
     */
    @PostMapping(value = "/signup", 
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response signUp (@NonNull @RequestBody UserDetailDTO userDetailDTO) {
        log.info("Signup acitvity started");
        
        try {
            //generate key for encryption
            String userIdForClient = userDetailDTO.getUserId();

            userDetailDTO.setUserId(secure.encode(userDetailDTO.getUserId() + "artlok_@_user_&_secure"));
            
            log.info(userDetailDTO.getUserId(), "encoded userid");
            
            userDetailService.signUpUser(userDetailDTO);

            //@Todo add data to neptune
            userDetailDTO.setUserId(userIdForClient);
        
            return Response.ok(userDetailDTO).build();
        } catch (ConflictException e) {
            throw new ResponseStatusException(HttpStatus.SC_CONFLICT, 
                String.format("userId %s is already exist, please choose new one", userDetailDTO.getUserId()), new ConflictException());
        } catch (DALException e) {
            throw new ResponseStatusException(HttpStatus.SC_INTERNAL_SERVER_ERROR, 
                String.format("not able to signup userid %s, please try again", userDetailDTO.getUserId()), new DALException());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.SC_UNAUTHORIZED, 
                String.format("not authorized userid %s, please try again", userDetailDTO.getUserId()), new UnauthorizedException());
        }
    }

    /**
     * Update user profile in datastore
     * @param userDetailDTO
     * @return Response
     */
    @PutMapping(value = "/updateProfile")
    public Response updateProfile (@RequestBody UserDetailDTO userDetailDTO) {
        log.info("update profile acitivity started");

        try {
            userDetailService.updateProfile(userDetailDTO);

            return Response.ok("Profile updated").build();
        } catch (ConflictException e) {
            throw new ResponseStatusException(HttpStatus.SC_CONFLICT, 
                String.format("userId %s is already exist, please choose new one", userDetailDTO.getUserId()), new ConflictException());
        } catch (DALException e) {
            throw new ResponseStatusException(HttpStatus.SC_INTERNAL_SERVER_ERROR, 
                String.format("not able to signup userid %s, please try again", userDetailDTO.getUserId()), new DALException());
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.SC_UNAUTHORIZED, 
                String.format("not authorized userid %s, please try again", userDetailDTO.getUserId()), new UnauthorizedException());
        }
    }
}