package dataaccess;
import model.AuthData;

public interface AuthDAO {
    int createAuthData();
    void removeAuthData() throws DataAccessException;
    AuthData findAuthData(int authToken) throws DataAccessException;
    void removeAuthDatas() throws DataAccessException;
}
