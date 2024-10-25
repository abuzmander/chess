package service;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.GamesRequest;
import response.CreateGameResult;
import response.Error;
import response.ListGamesResult;
import request.JoinGameRequest;
import response.JoinGameResult;

import java.util.Collection;


public class GameService {
    private final AuthDAO authDA;
    private final GameDAO gameDA;
    private int currID = 0;

    public GameService(AuthDAO authDA, UserDAO userDA, GameDAO gameDA) {
        this.authDA = authDA;
        this.gameDA = gameDA;
    }
    public ListGamesResult listGames(GamesRequest gamesRequest) throws ResponseException, DataAccessException {
        AuthData authData = authDA.getAuth(gamesRequest.authToken());
        if (authData == null){
            throw new ResponseException(401, new Error("Error: unauthorized"));
        }
        Collection<GameData> games = gameDA.getGames();
        return new ListGamesResult(games);
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException, DataAccessException {
        AuthData authData = authDA.getAuth(request.authToken());
        if (authData == null){
            throw new ResponseException(401, new Error("Error: unauthorized"));
        }
        GameData gameData =new GameData(currID++, null, null, request.gameName(), new ChessGame());
        gameDA.createGame(gameData);
        return new CreateGameResult(gameData.gameID());
    }

    public JoinGameResult joinGame(JoinGameRequest request) throws ResponseException, DataAccessException {
        AuthData authData = authDA.getAuth(request.authToken());
        if (authData == null){
            throw new ResponseException(401, new Error("Error: unauthorized"));
        }
        String username = authData.username();
        GameData gameData = gameDA.getGame(request.GameID());
        GameData newGameData = null;
        if (gameData == null){
            throw new ResponseException(400, new Error("Error: bad request"));
        }
        if (request.color() == ChessGame.TeamColor.WHITE){
            if(gameData.whiteUsername() != null) {
                throw new ResponseException(400, new Error("Error: bad request"));
            }
            else{
                newGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            }
        }
        if (request.color() == ChessGame.TeamColor.BLACK){
            if(gameData.blackUsername() != null){
                throw new ResponseException(400, new Error("Error: bad request"));
            }
            else{
                newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            }
        }
        gameDA.updateGame(gameData.gameID(), newGameData);
        return new JoinGameResult("");
    }


}
