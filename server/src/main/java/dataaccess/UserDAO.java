package dataaccess;
import model.UserData;

import java.util.Collection;
public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;
    void CreateUser(UserData data) throws DataAccessException;
    void clearUsers();
}
