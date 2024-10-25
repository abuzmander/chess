package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handler.*;
import spark.*;


public class Server {
    private final MemoryAuthDAO authDA = new MemoryAuthDAO();
    private final MemoryUserDAO userDA = new MemoryUserDAO();
    private final MemoryGameDAO gameDA = new MemoryGameDAO();
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) ->
                (new ClearHandler(authDA, userDA, gameDA)).handleRequest(req,
                        res));

        Spark.post("/user", (req, res) ->
                (new RegisterHandler(authDA, userDA)).handleRequest(req,
                        res));




        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
