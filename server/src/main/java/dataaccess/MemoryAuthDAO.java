package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> authDataFrame = new HashMap<>();
    @Override
    public void insertAuth(AuthData data) {
        authDataFrame.put(data.authToken(), data);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authDataFrame.remove(authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDataFrame.get(authToken);
    }

    @Override
    public void clearAuth() {
        authDataFrame.clear();
    }
}
