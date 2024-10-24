package dataaccess;
import model.AuthData;

public interface AuthDAO {
    void insertAuth(AuthData data);
    void deleteAuth(String authToken) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void clearAuth();
}
