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
    }
}
