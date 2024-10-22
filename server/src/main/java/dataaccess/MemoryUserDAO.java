package dataaccess;

import model.UserData;
import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> userDataFrame = new HashMap<>();
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return userDataFrame.get(username);
    }

    @Override
    public void CreateUser(UserData data) throws DataAccessException {
        
    }

    @Override
    public void clearUsers() {
        userDataFrame.clear();
    }
}
