package dataaccess;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    Collection<GameData> getGames() throws DataAccessException;
    GameData getGame(int gameId) throws DataAccessException;
    void updateGame(ChessGame.TeamColor color, int gameId, String username) throws DataAccessException;
    void CreateGame() throws DataAccessException;
    void clear();

}
