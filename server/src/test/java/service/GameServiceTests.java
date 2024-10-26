package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import passoff.model.*;
import request.CreateGameRequest;
import service.GameService;
import model.GameData;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class GameServiceTests {
//    @Test
//    @Order(8)
//    @DisplayName("Valid Creation")
//    public void goodCreate() {
//        MemoryAuthDAO authDA = new MemoryAuthDAO();
//        MemoryUserDAO userDA = new MemoryUserDAO();
//        MemoryGameDAO gameDA = new MemoryGameDAO();
//        GameService service = new GameService(authDA, userDA, gameDA);
//        CreateGameRequest request = service.;
//        Assertions.assertNotNull(service.createGame(), "Result did not return a game ID");
//        Assertions.assertTrue(createResult.getGameID() > 0, "Result returned invalid game ID");
//    }
//
//    @Test
//    @Order(9)
//    @DisplayName("Create with Bad Authentication")
//    public void badAuthCreate() {
//        //log out user so auth is invalid
//        serverFacade.logout(existingAuth);
//
//        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
//
//        assertHttpUnauthorized(createResult);
//        Assertions.assertNull(createResult.getGameID(), "Bad result returned a game ID");
//    }
//
//    @Test
//    @Order(10)
//    @DisplayName("Join Created Game")
//    public void goodJoin() {
//        //create game
//        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
//
//        //join as white
//        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.WHITE, createResult.getGameID());
//
//        //try join
//        TestResult joinResult = serverFacade.joinPlayer(joinRequest, existingAuth);
//
//        //check
//        assertHttpOk(joinResult);
//
//        TestListResult listResult = serverFacade.listGames(existingAuth);
//
//        Assertions.assertEquals(1, listResult.getGames().length);
//        Assertions.assertEquals(existingUser.getUsername(), listResult.getGames()[0].getWhiteUsername());
//        Assertions.assertNull(listResult.getGames()[0].getBlackUsername());
//    }
//
//    @Test
//    @Order(11)
//    @DisplayName("Join Bad Authentication")
//    public void badAuthJoin() {
//        //create game
//        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
//
//        //try join as white
//        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.WHITE, createResult.getGameID());
//        TestResult joinResult = serverFacade.joinPlayer(joinRequest, existingAuth + "bad stuff");
//
//        //check
//        assertHttpUnauthorized(joinResult);
//    }
//
//    @Test
//    @Order(11)
//    @DisplayName("Join Bad Team Color")
//    public void badColorJoin() {
//        //create game
//        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
//
//        //try join as white
//        TestJoinRequest joinRequest = new TestJoinRequest(null, createResult.getGameID());
//        TestResult joinResult = serverFacade.joinPlayer(joinRequest, existingAuth);
//
//        //check
//        assertHttpBadRequest(joinResult);
//    }
//
//    @Test
//    @Order(11)
//    @DisplayName("Join Steal Team Color")
//    public void stealColorJoin() {
//        //create game
//        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
//
//        //add existing user as black
//        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.BLACK, createResult.getGameID());
//        serverFacade.joinPlayer(joinRequest, existingAuth);
//
//        //register second user
//        TestAuthResult registerResult = serverFacade.register(newUser);
//
//        //join request trying to also join  as black
//        TestResult joinResult = serverFacade.joinPlayer(joinRequest, registerResult.getAuthToken());
//
//        //check failed
//        assertHttpForbidden(joinResult);
//    }
//
//    @Test
//    @Order(11)
//    @DisplayName("Join Bad Game ID")
//    public void badGameIDJoin() {
//        //create game
//        createRequest = new TestCreateRequest("Bad Join");
//        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
//
//        //try join as white
//        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.WHITE, null);
//        TestResult joinResult = serverFacade.joinPlayer(joinRequest, existingAuth);
//
//        //check
//        assertHttpBadRequest(joinResult);
//    }
//
//    @Test
//    @Order(12)
//    @DisplayName("List No Games")
//    public void noGamesList() {
//        TestListResult result = serverFacade.listGames(existingAuth);
//
//        assertHttpOk(result);
//        Assertions.assertTrue(result.getGames() == null || result.getGames().length == 0,
//                "Found games when none should be there");
//    }
//
//    @Test
//    @Order(12)
//    @DisplayName("List Multiple Games")
//    public void gamesList() {
//        //register a few users to create games
//        TestUser userA = new TestUser("a", "A", "a.A");
//        TestUser userB = new TestUser("b", "B", "b.B");
//        TestUser userC = new TestUser("c", "C", "c.C");
//
//        TestAuthResult authA = serverFacade.register(userA);
//        TestAuthResult authB = serverFacade.register(userB);
//        TestAuthResult authC = serverFacade.register(userC);
//
//        //create games
//        Collection<TestListEntry> expectedList = new HashSet<>();
//
//        //1 as black from A
//        String game1Name = "I'm numbah one!";
//        TestCreateResult game1 = serverFacade.createGame(new TestCreateRequest(game1Name), authA.getAuthToken());
//        serverFacade.joinPlayer(new TestJoinRequest(ChessGame.TeamColor.BLACK, game1.getGameID()), authA.getAuthToken());
//        expectedList.add(new TestListEntry(game1.getGameID(), game1Name, null, authA.getUsername()));
//
//
//        //1 as white from B
//        String game2Name = "Lonely";
//        TestCreateResult game2 = serverFacade.createGame(new TestCreateRequest(game2Name), authB.getAuthToken());
//        serverFacade.joinPlayer(new TestJoinRequest(ChessGame.TeamColor.WHITE, game2.getGameID()), authB.getAuthToken());
//        expectedList.add(new TestListEntry(game2.getGameID(), game2Name, authB.getUsername(), null));
//
//
//        //1 of each from C
//        String game3Name = "GG";
//        TestCreateResult game3 = serverFacade.createGame(new TestCreateRequest(game3Name), authC.getAuthToken());
//        serverFacade.joinPlayer(new TestJoinRequest(ChessGame.TeamColor.WHITE, game3.getGameID()), authC.getAuthToken());
//        serverFacade.joinPlayer(new TestJoinRequest(ChessGame.TeamColor.BLACK, game3.getGameID()), authA.getAuthToken());
//        expectedList.add(new TestListEntry(game3.getGameID(), game3Name, authC.getUsername(), authA.getUsername()));
//
//
//        //C play self
//        String game4Name = "All by myself";
//        TestCreateResult game4 = serverFacade.createGame(new TestCreateRequest(game4Name), authC.getAuthToken());
//        serverFacade.joinPlayer(new TestJoinRequest(ChessGame.TeamColor.WHITE, game4.getGameID()), authC.getAuthToken());
//        serverFacade.joinPlayer(new TestJoinRequest(ChessGame.TeamColor.BLACK, game4.getGameID()), authC.getAuthToken());
//        expectedList.add(new TestListEntry(game4.getGameID(), game4Name, authC.getUsername(), authC.getUsername()));
//
//
//        //list games
//        TestListResult listResult = serverFacade.listGames(existingAuth);
//        assertHttpOk(listResult);
//        Collection<TestListEntry> returnedList = new HashSet<>(Arrays.asList(listResult.getGames()));
//
//        //check
//        Assertions.assertEquals(expectedList, returnedList, "Returned Games list was incorrect");
//    }
}
