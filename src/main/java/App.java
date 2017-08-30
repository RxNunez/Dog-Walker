
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import dao.Sql2oWalkerDao;
import dao.Sql2oDogDao;
import models.Walker;
import models.Dog;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;


public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/dog-walkers.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2oWalkerDao walkerDao = new Sql2oWalkerDao(sql2o);
        Sql2oDogDao dogDao = new Sql2oDogDao(sql2o);

        // home page
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            List<Dog> dogs = dogDao.getAll();
            model.put("walkers", walkers);
            model.put("dogs",dogs);
            return new ModelAndView(model, "index.hbs");
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
            Walker walker = new Walker(walkerName);
            walkerDao.add(walker);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers", walkers);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/dogs/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            List<Dog> dogs = dogDao.getAll();
            model.put("walkers",walkers);
            model.put("dogs",dogs);
            return new ModelAndView(model, "dog-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/dogs/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String walkerPick = request.queryParams("walker");
            int walkerId = Integer.parseInt(walkerPick);
            Walker foundWalker = walkerDao.findById(walkerId);
            String dogName = request.queryParams("dogname");
            String breed = request.queryParams("breed");
            String color = request.queryParams("color");
            Dog newDog = new Dog(dogName, breed, color,foundWalker.getId());
            dogDao.add(newDog);
            List<Dog> dogs = dogDao.getAll();
            model.put("dogs", dogs);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers", walkers);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/walkers/:id/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            Walker editWalker = walkerDao.findById(Integer.parseInt(request.params("id")));
            model.put("editWalker", editWalker);
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/walkers/:id/update", (req,res) -> {
            Map<String, Object> model = new HashMap<>();
            String walkerName = req.queryParams("walkerName");
            int id = Integer.parseInt(req.params("id"));
            walkerDao.update(id, walkerName);
            List<Dog> dogs = dogDao.getAll();
            model.put("dogs", dogs);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers", walkers);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("walkers/:walkerId/dogs/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            Dog foundDog = dogDao.findById(Integer.parseInt(request.params("dogId")));
            model.put("dog", foundDog);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers", walkers);
            return new ModelAndView(model, "dog-detail.hbs");
        }, new HandlebarsTemplateEngine());

        get("/walkers/:walkerId/dogs/:id/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Dog editDog = dogDao.findById(Integer.parseInt(req.params("id")));
            Walker dogWalker = walkerDao.findById(editDog.getWalkerId());
            model.put("walkers", dogWalker);
            model.put("editDog", editDog);
            return new ModelAndView(model, "dog-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/walkers/:walkerId/dogs/:id/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String walker = req.queryParams("walker");
            String dogName = req.queryParams("dogname");
            String breed = req.queryParams("breed");
            String color = req.queryParams("color");
            int id = Integer.parseInt(req.params("id"));
            int walkerId = Integer.parseInt(req.params("walkerid"));
            dogDao.update(1, dogName, breed, color, 1);
            List<Dog> dogs = dogDao.getAll();
            model.put("dogs", dogs);
           return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("walkers/:walkerid/dogs/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            dogDao.deleteDogById(Integer.parseInt(req.params("id")));
            List<Dog> dogs = dogDao.getAll();
            List<Walker> walkers = walkerDao.getAll();
            model.put("dogs", dogs);
            model.put("walkers", walkers);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/walkers/:id", (req, res) ->  {
            Map<String, Object> model = new HashMap<>();
            Walker foundWalker = walkerDao.findById(Integer.parseInt(req.params("id")));
            List<Dog> allDogByWalker = walkerDao.getAllDogByWalker(foundWalker.getId());
            model.put("walkers", foundWalker);
            model.put("dogs", allDogByWalker);
            return new ModelAndView(model, "walker-detail.hbs");
        }, new HandlebarsTemplateEngine());

        get("walkers/:id/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            List<Dog> walkerDog =walkerDao.getAllDogByWalker(id);
            walkerDao.deleteWalkerById(id);
            List<Walker> walkers = walkerDao.getAll();
            List<Dog> dogs = dogDao.getAll();
            model.put("walkers", walkers);
            model.put("dogs", dogs);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());




    }
}