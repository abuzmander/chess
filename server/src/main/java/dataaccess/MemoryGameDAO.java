package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.List;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void createGame(GameData data) throws DataAccessException {
        games.put(nextId++, data);
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return games.get(gameId);
    }

    @Override
    public Collection<GameData> getGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(int gameId, GameData data) throws DataAccessException {
        games.put(gameId, data);
    }

    @Override
    public void clearGames() {
        games.clear();
    }
}
