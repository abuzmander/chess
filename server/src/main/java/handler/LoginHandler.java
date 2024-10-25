package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.RegisterResult;
import service.AccountService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class LoginHandler {
    private final AccountService service;
    public LoginHandler(AuthDAO authDA, UserDAO userDA){
        this.service = new AccountService(authDA, userDA);
    }
    public Object handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        Gson gson = new Gson();
        LoginRequest request = (LoginRequest) gson.fromJson(req.body(), LoginRequest.class);
        try {
            res.status(200);
            LoginResult result = service.login(request);
            return gson.toJson(result);
        }catch(DataAccessException punk){
            res.status(500);
            res.body(gson.toJson(Map.of("message", punk.getMessage())));
            return gson.toJson(Map.of("message", punk.getMessage()));
        } catch (ResponseException punk) {
            res.status(punk.StatusCode());
            res.body(gson.toJson(Map.of("message", punk.getMessage())));
            return gson.toJson(Map.of("message", punk.getMessage()));
        }

    }
}
