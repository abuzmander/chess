package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import exceptions.ResponseException;
import java.util.Objects;
import java.util.UUID;
import request.LogoutRequest;
import response.Error;
import response.LoginResult;
import response.RegisterResult;
import request.RegisterRequest;
import request.LoginRequest;
import response.LogoutResult;


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
        if(userData != null){
            throw new ResponseException(403, new Error("Error: already taken"));
        }
        userData = new UserData(request.username(), request.password(), request.email());
        userDA.insertUser(userData);
        String authToken = generateAuthToken();
        AuthData authData = new AuthData(authToken, userData.username());
        authDA.insertAuth(authData);
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public LoginResult login(LoginRequest request) throws DataAccessException, ResponseException {
        UserData dbUser = userDA.getUser(request.username());
        if(dbUser == null){
            throw new ResponseException(401, new Error("Error: unauthorized"));
        }
        if (!Objects.equals(dbUser.password(), request.password())){
            throw new ResponseException(401, new Error("Error: unauthorized"));
        }
        String authToken = generateAuthToken();
        AuthData authData = new AuthData(authToken, request.username());
        authDA.insertAuth(authData);
        return new LoginResult(authData.username(), authData.authToken());
    }

    public LogoutResult logout(LogoutRequest request) throws DataAccessException, ResponseException {
        AuthData authData = authDA.getAuth(request.authToken());
        if (authData == null){
            throw new ResponseException(401, new Error("Error: unauthorized"));
        }
        authDA.deleteAuth(authData.authToken());
        return new LogoutResult("");
    }
}
