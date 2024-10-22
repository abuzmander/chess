package dataaccess;
import model.UserData;

import java.util.Collection;
public interface UserDAO {
    void findNoUser(String username) throws DataAccessException;
    void findUserData(String username) throws DataAccessException;

    void AddUserData(UserData data) throws DataAccessException;

}
