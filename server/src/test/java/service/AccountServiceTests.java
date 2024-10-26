package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.LogoutResult;
import response.RegisterResult;

public class AccountServiceTests {
    @Test
    @DisplayName("Normal User Login")
    public void successLogin() throws ResponseException, DataAccessException {
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        AccountService service = new AccountService(authDA, userDA);
        RegisterRequest registerRequest = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult registerResponse = service.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        LoginResult loginResponse = service.login(loginRequest);
        Assertions.assertEquals(loginRequest.username(), loginResponse.username(), "Response did not give the same username as user");
        Assertions.assertNotNull(registerResponse.authToken(), "Response did not return authentication String");
    }

    @Test
    @DisplayName("Login Invalid User")
    public void loginInvalidUser() throws Exception{
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        AccountService service = new AccountService(authDA, userDA);
        RegisterRequest registerRequest = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult registerResponse = service.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("Wrong Username", registerRequest.password());
        ResponseException thrown =  Assertions.assertThrows(
                ResponseException.class,
                () -> service.login(loginRequest),
                "Expected to throw since Wrong username"
        );
    }

    @Test
    @Order(3)
    @DisplayName("Login Wrong Password")
    public void loginWrongPassword() throws Exception{
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        AccountService service = new AccountService(authDA, userDA);
        RegisterRequest registerRequest = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult registerResponse = service.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), "Wrong Password");
        ResponseException thrown =  Assertions.assertThrows(
                ResponseException.class,
                () -> service.login(loginRequest),
                "Expected to throw since Wrong Password"
        );
    }

    @Test
    @DisplayName("Normal User Registration")
    public void successRegister() throws ResponseException, DataAccessException {
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        MemoryGameDAO gameDA = new MemoryGameDAO();
        AccountService service = new AccountService(authDA, userDA);
        RegisterRequest request = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult response = service.register(request);
        Assertions.assertEquals(request.username(), response.username(), "Response did not give the same username as user");
        Assertions.assertNotNull(response.authToken(), "Response did not return authentication String");
    }

    @Test
    @Order(5)
    @DisplayName("Re-Register User")
    public void registerTwice() throws Exception{
        //submit register request trying to register existing user
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        AccountService service = new AccountService(authDA, userDA);
        RegisterRequest request = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult response = service.register(request);
        ResponseException thrown =  Assertions.assertThrows(
                ResponseException.class,
                () -> service.register(request),
                "Expected to throw since we already user registered"
        );
    }


    @Test
    @DisplayName("Normal Logout")
    public void successLogout() throws Exception{
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        AccountService service = new AccountService(authDA, userDA);
        RegisterRequest registerRequest = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult registerResponse = service.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        LoginResult loginResponse = service.login(loginRequest);
        LogoutRequest logoutRequest = new LogoutRequest(loginResponse.authToken());
        LogoutResult logoutResponse = service.logout(logoutRequest);
        Assertions.assertEquals("", logoutResponse.empty(), "Error was thrown when shouldn't");
    }

    @Test
    @DisplayName("Invalid Auth Logout")
    public void failLogout() throws Exception{
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        AccountService service = new AccountService(authDA, userDA);
        RegisterRequest registerRequest = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult registerResponse = service.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        LoginResult loginResponse = service.login(loginRequest);
        LogoutRequest logoutRequest = new LogoutRequest(loginResponse.authToken());
        LogoutResult logoutResponse = service.logout(logoutRequest);
        ResponseException thrown =  Assertions.assertThrows(
                ResponseException.class,
                () -> service.logout(logoutRequest),
                "Expected to throw since we already user logged out"
        );
    }
}
