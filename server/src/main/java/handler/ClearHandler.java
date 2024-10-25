package handler;

import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class ClearHandler
{
    public Object handlerRequest(Request req, Response res){


        res.status(200);
        return "{}";
    }
}