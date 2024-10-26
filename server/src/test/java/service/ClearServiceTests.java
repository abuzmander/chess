package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import org.junit.jupiter.api.Assertions;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.CreateGameResult;
import response.LoginResult;
import response.RegisterResult;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exceptions.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import passoff.model.*;
import request.*;
import response.*;
import service.GameService;
import model.GameData;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class ClearServiceTests{
    @Test
    @DisplayName("Clear Test")
    public void clearData() throws Exception{
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        MemoryGameDAO gameDA = new MemoryGameDAO();
        AccountService userService = new AccountService(authDA, userDA);
        RegisterRequest registerRequest = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult registerResponse = userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        LoginResult loginResponse = userService.login(loginRequest);
        GameService gameService = new GameService(authDA, userDA, gameDA);
        CreateGameRequest createGameRequest = new CreateGameRequest(loginResponse.authToken(), "GameName");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        JoinGameRequest joinRequest = new JoinGameRequest(loginResponse.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID());
        JoinGameResult joinResult = gameService.joinGame(joinRequest);
        ClearService clearService = new ClearService(authDA, userDA, gameDA);
        clearService.clear();
        Assertions.assertEquals("", joinResult.empty(), "Error was thrown when shouldn't");
    }
}
