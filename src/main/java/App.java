
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import dao.Sql2oWalkerDao;
import dao.Sql2oDogDao;
import models.Walker;
import models.Dog;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;


public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/dog-walker.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2oWalkerDao walkerDao = new Sql2oWalkerDao(sql2o);
        Sql2oDogDao dogDao = new Sql2oDogDao(sql2o);

        // home page
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> allWalkers = walkerDao.getAll();
            model.put("walkers", allWalkers);
            List<Dog> dogs = dogDao.getAll();
            model.put("dogs",dogs);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/walkers/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            dogDao.clearAllDog();
            walkerDao.clearAllWalker();
            List<Walker> allWalker = walkerDao.getAll();
            model.put("walkers", allWalker);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walkers/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers", walkers);
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/walkers/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String walkerName = request.queryParams("walkerName");
            Walker newWalker = new Walker(walkerName);
            walkerDao.add(newWalker);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers", walkers);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/walkers/:walkerid", (req, res) ->  {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToFind = Integer.parseInt(req.params("id"));
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers", walkers);
            Walker foundWalker = walkerDao.findById(idOfWalkerToFind);
            model.put("walker", foundWalker);
            List<Dog> AllDogByWalker = walkerDao.getAllDogByWalker(idOfWalkerToFind);
            model.put("dogs", AllDogByWalker);
            return new ModelAndView(model, "walker-detail.hbs");
        }, new HandlebarsTemplateEngine());

        get("/walkers/:walkerid/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int thisId = Integer.parseInt(req.params("walkerid"));
            model.put("editWalker", true);
            List<Walker> allWalkers = walkerDao.getAll();
            model.put("walkers", allWalkers);
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/walkers/update", (req,res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToEdit = Integer.parseInt(req.queryParams("editWalkerId"));
            String newName = req.queryParams("newWalkerName");
            walkerDao.update(walkerDao.findById(idOfWalkerToEdit).getId(), newName);
            List<Walker> allWalkers = walkerDao.getAll();
            List<Dog> dogs = dogDao.getAll();
            model.put("walkers", allWalkers);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("walkers/:walkerid/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToDelete = Integer.parseInt(req.params("walkerid"));
            Walker deleteWalker = walkerDao.findById(idOfWalkerToDelete);
            walkerDao.deleteWalkerById(idOfWalkerToDelete);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/dogs/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> allWalkers = walkerDao.getAll();
            List<Dog> dogs = dogDao.getAll();
            model.put("walkers",allWalkers);
            model.put("dogs", dogs);
            return new ModelAndView(model, "dog-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/dogs/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers", walkers);
            String walkerName= request.queryParams("walkerName");
            int walkerId = Integer.parseInt(request.queryParams("walkerId"));
            String dogName = request.queryParams("dogname");
            String breed = request.queryParams("breed");
            String color = request.queryParams("color");
            Dog newDog = new Dog(dogName, breed, color, walkerId);
            dogDao.add(newDog);
            model.put("dogs", newDog);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("walkers/:walkerid/dogs/:dogid", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfDogToFind = Integer.parseInt(req.params("dogId"));
            Dog foundDog = dogDao.findById(idOfDogToFind);
            model.put("dog", foundDog);
            return new ModelAndView(model, "dog-detail.hbs");
        }, new HandlebarsTemplateEngine());

        get("/walkers/:id/dogs/:dogid/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int walkeridOfDogToEdit = Integer.parseInt(req.params("dogid"));
            int idOfDogToEdit = Integer.parseInt(req.params("id"));
            Dog editDog = dogDao.findById(idOfDogToEdit);
            model.put("editDog", true);
            model.put("walkeridOfDogToEdit", walkeridOfDogToEdit);
            List<Dog> allDogs = dogDao.getAll();
            List<Walker> allWalkers = walkerDao.getAll();
            model.put("idOfDogToEdit", idOfDogToEdit);
            model.put("dogs", allDogs);
            model.put("walkers", allWalkers);
            return new ModelAndView(model, "dog-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/walkers/:id/dogs/:dogid/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String dogName = req.queryParams("dogname");
            String breed = req.queryParams("breed");
            String color = req.queryParams("color");
            int walkerId = Integer.parseInt(req.queryParams("walkerid"));
            int walkerIdOfDogToEdit = Integer.parseInt(req.params("id"));
            int idOfDogToEdit = Integer.parseInt(req.params("dogid"));
            Dog editDog = dogDao.findById(idOfDogToEdit);
            dogDao.update(idOfDogToEdit,dogName,breed,color, walkerId);
            model.put("idOfDogToEdit", idOfDogToEdit);
            model.put("walkerIdOfDogToEdit", walkerIdOfDogToEdit);
            return new ModelAndView(model, "success.hbs");
         }, new HandlebarsTemplateEngine());

        get("walkers/:id/dogs/:dogid/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int walkerIdOfDogToDelete = Integer.parseInt(req.params("id"));
            int idOfDogToDelete = Integer.parseInt(req.params("dogid"));
            Dog deleteDog = dogDao.findById(idOfDogToDelete);
            dogDao.deleteDogById(idOfDogToDelete);
            List<Dog> dogs = dogDao.getAll();
            model.put("dogs", dogs);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        // delete all dogs
        get("/dogs/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            dogDao.clearAllDog();
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
