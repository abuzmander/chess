package handler;

import dataaccess.*;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import service.ClearService;

import java.util.Map;


public class ClearHandler{
    private final ClearService service;
    public ClearHandler(AuthDAO authDA, UserDAO userDA, GameDAO gameDA){
        this.service = new ClearService(authDA, userDA, gameDA);
    }

    public Object handleRequest(Request req, Response res){
        Gson gson = new Gson();
        service.clear();
        res.status(200);
        return "{}";
    }
}