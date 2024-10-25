package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import request.CreateGameRequest;
import response.CreateGameResult;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class CreateGameHandler {
    private final GameService service;

    public CreateGameHandler(AuthDAO authDA, UserDAO userDA, GameDAO gameDA) {
        this.service = new GameService(authDA, userDA, gameDA);
    }

    public Object handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        Gson gson = new Gson();
        CreateGameRequest request = new CreateGameRequest(req.headers("authorization"), gson.fromJson(req.body(), CreateGameRequest.class).gameName());
        if (request.gameName() == null) {
            res.status(400);
            res.body(gson.toJson(Map.of("message", "Error: bad request")));
            return gson.toJson(Map.of("message", "Error: bad request"));
        }
        try {
            res.status(200);
            CreateGameResult result = service.createGame(request);
            return gson.toJson(result);
        } catch (DataAccessException punk) {
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