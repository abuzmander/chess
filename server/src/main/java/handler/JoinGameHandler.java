package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import request.JoinGameRequest;
import response.JoinGameResult;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class JoinGameHandler {
    private final GameService service;

    public JoinGameHandler(AuthDAO authDA, UserDAO userDA, GameDAO gameDA) {
        this.service = new GameService(authDA, userDA, gameDA);
    }

    public Object handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        Gson gson = new Gson();
        JoinGameRequest partialRequest = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameRequest request = new JoinGameRequest(req.headers("authorization"), partialRequest.playerColor(), partialRequest.gameID());
        if (!(request.gameID() > 0) || request.playerColor() == null) {
            System.out.println(request.playerColor());
            res.status(400);
            res.body(gson.toJson(Map.of("message", "Error: bad request")));
            return gson.toJson(Map.of("message", "Error: bad request"));
        }
        try {
            res.status(200);
            JoinGameResult result = service.joinGame(request);
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
