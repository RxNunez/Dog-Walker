import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import models.Walker;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;


public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");

        get("/walker/new", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());


        post("/walker/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String content = request.queryParams("walkerName");
            Walker newWalker = new Walker(content);
            model.put("walker", newWalker);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());


        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            ArrayList<Walker> walker = Walker.getAll();
            model.put("walker", walker);
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walker/:id", (request, response) ->  {
            Map<String, Object> model = new HashMap<>();
            int idOfPostToFind = Integer.parseInt(request.params("id"));
            Walker foundWalker = Walker.findById(idOfPostToFind);
            model.put("walker", foundWalker);
            return new ModelAndView(model, "walker-detail.hbs");
        }, new HandlebarsTemplateEngine());


        get("/walker/:id/update", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfWalkerToEdit = Integer.parseInt(request.params("id"));
            Walker editWalker = Walker.findById(idOfWalkerToEdit);
            model.put("editWalker", editWalker);
            return new ModelAndView(model, "walker-form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/walker/:id/update", (request,response) -> {
            Map<String, Object> model = new HashMap<>();
            String newContent = request.queryParams("walkerName");
            int idOfWalkerToEdit = Integer.parseInt(request.params("id"));
            Walker editWalker = Walker.findById(idOfWalkerToEdit);
            editWalker.update(newContent);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

    }
}
