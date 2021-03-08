package edu.escuelaing.arem.app.Services;

import java.util.Date;
import java.util.HashMap;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.escuelaing.arem.app.Palabra.Palabra;

public class DataBase {
    private MongoCollection<org.bson.Document> columnas;

    public DataBase() {
        // localhost mongo-db
        MongoClientURI uri = new MongoClientURI(
                "mongodb://johanrueda:johanrueda@0.0.0.0:27017/?serverSelectionTimeoutMS=5000&connectTimeoutMS=10000&authSource=docker&authMechanism=SCRAM-SHA-1&3t.uriVersion=3");

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("docker");
        columnas = database.getCollection("palabra");
    }

    public String getDescription() {
        String descripciones = "[";
        Palabra palabra;
        for (Document d : columnas.find()) {
            palabra = new Palabra(d.get("descripcion").toString(), d.get("fecha").toString());
            descripciones += palabra.toString() + ",";
        }

        if (columnas.countDocuments() == 0) descripciones = "[ ";
        descripciones = descripciones.substring(0, descripciones.length() - 1);
        descripciones += "]";
        return descripciones;
    }

    public void addPalabra(String palabra) {
        Palabra pal = this.toPalabra(palabra);
        HashMap<String, Object> hashmap = new HashMap<>();
        String descripcion = pal.getDescription();
        String fecha = pal.getDate();
        hashmap.put("descripcion", descripcion);
        Date newfecha = new Date();
        String fechanueva = newfecha.toString();
        hashmap.put("fecha", fechanueva);
        Document newpalabra = new Document(hashmap);
        columnas.insertOne(newpalabra);
    }

    public Palabra toPalabra(String string) {
        /*
         * {
         * 		"descripcion" : "hola",
         * 		"fecha" : "2020/09/17"
         * }
         */
        String json = string.substring(1, string.length() - 1);
        json = json.replace("\"", "");
        String[] json1 = json.split(",");
        HashMap<String, String> data = new HashMap<>();

        for (String value: json1) {
            String[] dic = value.split(":");
            data.put(dic[0].trim(), dic[1].trim());
        }

        Palabra palabra = new Palabra(data.get("descripcion"),data.get("fecha"));

        return palabra;
    }
}