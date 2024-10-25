package handler;

import dataaccess.*;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import service.ClearService;




public class ClearHandler{
    private final ClearService service;
    public ClearHandler(AuthDAO authDA, UserDAO userDA, GameDAO gameDA){
        this.service = new ClearService(authDA, userDA, gameDA);
    }

    public Object handleRequest(Request req, Response res){
        service.clear();
        res.status(200);
        return "";
    }
}