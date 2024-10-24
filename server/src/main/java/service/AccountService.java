package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import exceptions.ResponseException;
import java.util.UUID;
import response.Error;
import response.RegisterResult;
import request.RegisterRequest;


public class AccountService {
    private final UserDAO userDA;
    private final AuthDAO authDA;

    public AccountService(AuthDAO authDA, UserDAO userDA) {
        this.userDA = userDA;
        this.authDA = authDA;
    }

    private String generateAuthToken(){
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException, ResponseException {
//        Need to add some Errors
        UserData userData = userDA.getUser(request.username());
        if(userData.password() != null){
            throw new ResponseException(403, new Error("Error: already taken"));
        }
        userData = new UserData(request.username(), request.password(), request.email());
        userDA.insertUser(userData);
        String authToken = generateAuthToken();
        AuthData authData = new AuthData(authToken, userData.username());
        authDA.insertAuth(authData);
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public AuthData login(UserData user) throws DataAccessException, ResponseException {
        UserData dbUser = userDA.getUser(user.username());
        if(dbUser == null){
            
        }
        return null;
    }

    public void logout(AuthData auth) {}
}
