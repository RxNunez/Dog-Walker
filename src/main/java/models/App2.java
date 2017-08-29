package models;
import dao.Sql2oDogDao;
import dao.Sql2oWalkerDao;
import models.Dog;
import models.Walker;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;



public class App {
    public static void main(String[] args) {
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/dog-walker.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "","");
        Sql2oDogDao dogDao = new Sql2oDogDao(sql2o);
        Sql2oWalkerDao walkerDao = new Sql2oWalkerDao(sql2o);

        //get: display walker form
        get("/walkers/new", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers",walkers);
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process new walker form
        post("/walkers/new", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            String walkerName = req.queryParams("walkername");
            Walker team = new Walker(walkerName);
            walkerDao.add(team);
            List<Walker> walkers = walkerDao.getAll();
            model.put("walkers",walkers);
            return new ModelAndView(model,"success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: display reset confirmation
        get("/walkers/reset", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "reset.hbs");
        }, new HandlebarsTemplateEngine());

        //get: reset all data
        get("/walkers/reset/success", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            walkerDao.clearAllWalker();
            dogDao.clearAllDog();
            List<Walker> walkers = walkerDao.getAll();
            List<Dog> dogs = dogDao.getAll();
            model.put("walkers",walkers);
            model.put("dogs",dogs);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        //get: display dog form
        get("/dogs/new", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            List<Dog> dogs = dogDao.getAll();
            model.put("walkers",walkers);
            model.put("dogs",dogs);
            return new ModelAndView(model, "dog-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: process dog walker form
        post("/dogs/new", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            String walkerChoice= req.queryParams("walker");
            int walkerId = Integer.parseInt(teamChoice);
            Walker foundWalker = walkerDao.findById(walkerId);
            String dogName = req.queryParams("dogname");
            Dog newDog = new Dog(dogName, foundWalker.getId());
            dogDao.add(newDog);
            List<Dog> dogs = dogDao.getAll();
            List<Walker> walkers = walkerDao.getAll();
            model.put("dogs", dogs);
            model.put("walkers", walkers);
            return new ModelAndView(model,"success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: list all walker listed
        get("/", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            List<Walker> walkers = walkerDao.getAll();
            List<Dog> dogs = dogDao.getAll();
            model.put("walkers",walkers);
            model.put("dogs",dogs);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //get: list all dog listed per walker
        get("/walkers/:id", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            Walker foundWalker = walkerDao.findById(Integer.parseInt(req.params("id")));
            List<Dog> dogs = walkerDao.getAllDogsByWalker(foundWalker.getId());
            model.put("walkers", foundWalker);
            model.put("dogs", dogs);
            return new ModelAndView(model, "team-detail.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete a team and its dogs
        get("/walkers/:id/delete", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params("id"));
            List<Dog> teamDogs =teamDao.getAllDogsByWalker(id);
            teamDao.deleteById(id);
            teamDogs.clear();
            List<Walker> walkers = teamDao.getAll();
            List<Dog> dogs = memberDao.getAll();
            model.put("walkers", walkers);
            model.put("dogs", dogs);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: display team form
        get("/walkers/:id/edit", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            Walker editWalker = teamDao.findById(Integer.parseInt(req.params("id")));
            model.put("editWalker", editWalker);
            return new ModelAndView(model, "team-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: update the walker
        post("/walkers/:id/edit", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            String walkerName = req.queryParams("team-name");
            int id = Integer.parseInt(req.params("id"));
            teamDao.update(id,walkerName);
            List<Walker> walkers = teamDao.getAll();
            List<Dog> dogs = memberDao.getAll();
            model.put("walkers", walkers);
            model.put("dogs", dogs);
            return new ModelAndView(model,"success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: display member form
        get("/walkers/:teamid/dogs/:id/edit", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            Dog editDog = memberDao.findById(Integer.parseInt(req.params("id")));
            Walker memberWalker = teamDao.findById(editDog.getWalkerId());
            model.put("editDog", editDog);
            model.put("memberWalker", memberWalker);
            return new ModelAndView(model, "member-form.hbs");
        }, new HandlebarsTemplateEngine());

        //post: update the member
        post("/walkers/:teamid/dogs/:id/edit", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            String memberName = req.queryParams("member-name");
            int id = Integer.parseInt(req.params("id"));
            int teamId = Integer.parseInt(req.params("teamid"));
            memberDao.update(id, memberName, teamId);
            List<Dog> dogs = memberDao.getAll();
            model.put("dogs", dogs);
            return new ModelAndView(model,"success.hbs");
        }, new HandlebarsTemplateEngine());

        //get: delete a member
        get("/walkers/:teamid/dogs/:id/delete", (req,res)->{
            Map<String, Object> model = new HashMap<>();
            memberDao.deleteById(Integer.parseInt(req.params("id")));
            List<Walker> walkers = teamDao.getAll();
            List<Dog> dogs = memberDao.getAll();
            model.put("walkers", walkers);
            model.put("dogs", dogs);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());
    }
}