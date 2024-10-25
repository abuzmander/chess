package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import request.RegisterRequest;
import response.Error;
import response.RegisterResult;
import service.AccountService;
import exceptions.ResponseException;
import spark.Request;
import spark.Response;
import java.util.Map;

public class RegisterHandler {
    private final AccountService service;
    public RegisterHandler(AuthDAO authDA, UserDAO userDA){
        this.service = new AccountService(authDA, userDA);
    }
    public Object handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        Gson gson = new Gson();
        RegisterRequest request = (RegisterRequest)gson.fromJson(req.body(), RegisterRequest.class);
        if (request.username() == null || request.password() == null || request.email() == null){
            System.out.println("test");
            res.status(400);
            res.body(gson.toJson(Map.of("message", "Error: bad request")));
            return gson.toJson(Map.of("message", "Error: bad request"));
        }
        try {
            res.status(200);
            RegisterResult result = service.register(request);
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
