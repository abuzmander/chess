package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import request.GamesRequest;
import request.LogoutRequest;
import response.ListGamesResult;
import response.LogoutResult;
import service.AccountService;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ListGamesHandler {
    private final GameService service;
    public ListGamesHandler(AuthDAO authDA, UserDAO userDA, GameDAO gameDAO){
        this.service = new GameService(authDA, userDA, gameDAO);
    }
    public Object handleRequest(Request req, Response res) throws ResponseException, DataAccessException {
        Gson gson = new Gson();
        GamesRequest request = new GamesRequest(req.headers("authorization"));
        try {
            res.status(200);
            ListGamesResult result = service.listGames(request);
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
