package service;

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

public class GameServiceTests {
    @Test
    @DisplayName("Valid Creation")
    public void goodCreate() throws Exception{
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
        Assertions.assertTrue(createGameResult.gameID() > 0, "Result returned invalid game ID");
    }

    @Test
    @DisplayName("Create with Bad Authentication")
    public void badAuthCreate() throws Exception{
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        MemoryGameDAO gameDA = new MemoryGameDAO();
        AccountService userService = new AccountService(authDA, userDA);
        RegisterRequest registerRequest = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult registerResponse = userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        LoginResult loginResponse = userService.login(loginRequest);
        GameService gameService = new GameService(authDA, userDA, gameDA);
        CreateGameRequest createGameRequest = new CreateGameRequest("bad", "GameName");
        ResponseException thrown =  Assertions.assertThrows(
                ResponseException.class,
                () -> gameService.createGame(createGameRequest),
                "Expected to throw since Wrong username"
        );
    }

    @Test
    @DisplayName("Join Created Game")
    public void goodJoin() throws Exception{
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
        Assertions.assertEquals("", joinResult.empty(), "Error was thrown when shouldn't");

    }

    @Test
    @Order(11)
    @DisplayName("Join Bad Authentication")
    public void badAuthJoin() throws Exception{
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
        JoinGameRequest joinRequest = new JoinGameRequest("Bad Auth", ChessGame.TeamColor.WHITE, createGameResult.gameID());
        ResponseException thrown =  Assertions.assertThrows(
                ResponseException.class,
                () -> gameService.joinGame(joinRequest),
                "Expected to throw since bad auth"
        );
    }


    @Test
    @Order(11)
    @DisplayName("Join Steal Team Color")
    public void stealColorJoin() throws Exception{
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
        ResponseException thrown =  Assertions.assertThrows(
                ResponseException.class,
                () -> gameService.joinGame(joinRequest),
                "Color is already takenshould throw"
        );
    }

    @Test
    @Order(11)
    @DisplayName("Join Bad Game ID")
    public void badGameIDJoin() throws Exception{
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
        JoinGameRequest joinRequest = new JoinGameRequest(loginResponse.authToken(), ChessGame.TeamColor.WHITE, 500);
        ResponseException thrown =  Assertions.assertThrows(
                ResponseException.class,
                () -> gameService.joinGame(joinRequest),
                "ID is invalid should throw"
        );
    }

    @Test
    @DisplayName("List No Games")
    public void noGamesList() throws Exception{
        MemoryAuthDAO authDA = new MemoryAuthDAO();
        MemoryUserDAO userDA = new MemoryUserDAO();
        MemoryGameDAO gameDA = new MemoryGameDAO();
        AccountService userService = new AccountService(authDA, userDA);
        RegisterRequest registerRequest = new RegisterRequest("Bob", "help", "bob@bob.com");
        RegisterResult registerResponse = userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(registerRequest.username(), registerRequest.password());
        LoginResult loginResponse = userService.login(loginRequest);
        GameService gameService = new GameService(authDA, userDA, gameDA);
        GamesRequest gamesRequest = new GamesRequest(loginResponse.authToken());
        ListGamesResult gamesResult = gameService.listGames(gamesRequest);
        Assertions.assertTrue(gamesResult.games().isEmpty(),
                "Found games when none should be there");
    }
}
