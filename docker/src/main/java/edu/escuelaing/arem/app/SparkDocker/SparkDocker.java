package edu.escuelaing.arem.app.SparkDocker;

import static spark.Spark.*;

import edu.escuelaing.arem.app.Services.DataBase;
/**
 * Server http spark
 */
public class SparkDocker{

    public static void main(String... args) {
        DataBase db = new DataBase();
        port(getPort());
        get("/hello", (req,res) -> "Hello Docker!");
        get("/descripciones", (req,res) -> {
            System.out.println(db.getDescription());
            return db.getDescription();
        });
        post("/add", (request, response) -> {
            String req = request.body(); //String en formato json
            db.addPalabra(req);
            return "{\"confirm\":" + "ok" + "}";
        });
    }
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}