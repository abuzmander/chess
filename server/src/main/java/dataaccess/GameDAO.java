package dataaccess;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    void createGame(GameData data) throws DataAccessException;
    GameData getGame(int gameId) throws DataAccessException;
    Collection<GameData> getGames() throws DataAccessException;
    void updateGame(int gameId, GameData data) throws DataAccessException;
    void clearGames();

}
