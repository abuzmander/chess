package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import request.LogoutRequest;
import request.RegisterRequest;
import response.LogoutResult;
import response.RegisterResult;
import service.AccountService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class LogoutHandler {
    private final AccountService service;
    public LogoutHandler(AuthDAO authDA, UserDAO userDA){
        this.service = new AccountService(authDA, userDA);
    }
    public Object handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        Gson gson = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        try {
            res.status(200);
            LogoutResult result = service.logout(request);
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
