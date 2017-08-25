
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

        // delete all walkers
        get("/walkers/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            walkerDao.clearAllWalker();
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        // delete all dogs
        get("/dogs/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            dogDao.clearAllDog();
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        // home page
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            model.put("walker", walkers);
            List<Dog> dogs = dogDao.getAll();
            model.put("dog",dogs);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walkers/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            model.put("walker", walkers);
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/walkers/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String walkerName = request.queryParams("walkerName");
            Walker newWalker = new Walker(walkerName, 1);
            walkerDao.add(newWalker);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walker", newWalker);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walkers/:walkerId/dogs/:dogid", (req, res) ->  {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToFind = Integer.parseInt(req.params("walkerid"));
            List<Walker> walkers = walkerDao.getAll();
            model.put("walker", walkers);
            Walker foundWalker = walkerDao.findById(idOfWalkerToFind);
            model.put("walker", foundWalker);
            List<Dog> allDogByWalker = walkerDao.getAllDogByWalker(idOfWalkerToFind);
            model.put("dog", allDogByWalker);
            return new ModelAndView(model, "walker-detail.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walkers/:walkerid/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToEdit = Integer.parseInt(req.params("walkerid"));
            Walker editWalker = walkerDao.findById(idOfWalkerToEdit);
            model.put("editWalker", editWalker);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walker", walkers);
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/walkers/update", (req,res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToEdit = Integer.parseInt(req.params("walkerid"));
            String newWalkerName = req.queryParams("walkerName");
            walkerDao.update(walkerDao.findById(idOfWalkerToEdit).getId(), newWalkerName);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walker", walkers);
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
            List<Dog> allDog = dogDao.getAll();
            model.put("dog", allDog);
            return new ModelAndView(model, "dog-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/dogs/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> allWalker = walkerDao.getAll();
            model.put("walker", allWalker);
            String dogName = request.queryParams("dogname");
            String breed = request.queryParams("breed");
            String color = request.queryParams("color");
            Dog newDog = new Dog(dogName, breed, color);
            dogDao.add(newDog);
            model.put("dog", newDog);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walkers/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            dogDao.clearAllDog();
            walkerDao.clearAllWalker();
            List<Walker> allWalker = walkerDao.getAll();
            model.put("walker", allWalker);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        get("walkers/:walkerid/dog/:dogid", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfDogToFind = Integer.parseInt(req.params("dogId"));
            Dog foundDog = dogDao.findById(idOfDogToFind);
            model.put("dog", foundDog);
            return new ModelAndView(model, "dog-detail.hbs");
        }, new HandlebarsTemplateEngine());


        get("/dogs/:dogid/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfDogToEdit = Integer.parseInt(req.params("dogid"));
            Dog editDog = dogDao.findById(idOfDogToEdit);
            model.put("editDog", editDog);
            return new ModelAndView(model, "dog-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/dogs/:dogid/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String dogName = request.queryParams("dogname");
            String breed = request.queryParams("breed");
            String color = request.queryParams("color");
            int idOfDogToEdit = Integer.parseInt(request.params("dogid"));
            Dog editDog = dogDao.findById(idOfDogToEdit);
            dogDao.update(idOfDogToEdit,dogName,breed,color);
            return new ModelAndView(model, "success.hbs");
         }, new HandlebarsTemplateEngine());


        get("walkers/:walkerid/dog/:dogid/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfDogToDelete = Integer.parseInt(req.params("dogid"));
            Dog deleteDog = dogDao.findById(idOfDogToDelete);
            dogDao.deleteDogById(idOfDogToDelete);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
