package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import response.deleteDBResult;

public class ClearService {
    private final UserDAO userDA;
    private final AuthDAO authDA;
    private final GameDAO gameDA;
    public ClearService(AuthDAO authDA, UserDAO userDA, GameDAO gameDA) {
        this.userDA = userDA;
        this.authDA = authDA;
        this.gameDA = gameDA;
    }
    public deleteDBResult clear(){
        userDA.clearUsers();
        gameDA.clearGames();
        authDA.clearAuth();
        return new deleteDBResult("");
    }
}
