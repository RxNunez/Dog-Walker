
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import dao.Sql2oWalkerDao;
import models.Walker;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;


public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");
        String connectionString = "jdbc:h2:~/dog-walker.db;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        Sql2oWalkerDao walkerDao = new Sql2oWalkerDao(sql2o);


        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            List<Walker> walker = walkerDao.getAll();
            model.put("walker", walker);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walker/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            walkerDao.clearAllWalker();
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walker/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/walker/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String walkerName = request.queryParams("walkerName");
            Walker newWalker = new Walker(walkerName, 1);
            walkerDao.add(newWalker);
            model.put("walker", newWalker);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        get("/dog/:dogId/walker/:walkerid", (req, res) ->  {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToFind = Integer.parseInt(req.params("walkerId"));
            Walker foundWalker = walkerDao.findById(idOfWalkerToFind);
            model.put("walker", foundWalker);
            return new ModelAndView(model, "walker-detail.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walker/update", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToEdit = Integer.parseInt(req.params("id"));
            Walker editWalker = walkerDao.findById(idOfWalkerToEdit);
            model.put("editWalker", editWalker);
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/walker/update", (req,res) -> {
            Map<String, Object> model = new HashMap<>();
            String newWalkerName = req.queryParams("walkerName");
            int idOfWalkerToEdit = Integer.parseInt(req.params("id"));
            Walker editWalker = walkerDao.findById(idOfWalkerToEdit);
            walkerDao.update(idOfWalkerToEdit, newWalkerName);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("dog/:dogId/walker/:walkerid/delete", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToDelete = Integer.parseInt(req.params("walkerid"));
            Walker deleteWalker = walkerDao.findById(idOfWalkerToDelete);
            walkerDao.deleteWalkerById(idOfWalkerToDelete);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
