package controllers;

import play.Logger;
import play.libs.F;
import play.libs.ws.WSResponse;
import play.mvc.*;

import services.ElasticSearchService;
import services.IElasticSearchService;
import views.html.*;

import java.io.IOException;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public class Application extends Controller {

    public static Result home() throws IOException {
        return ok(home.render());
    }

    public static Result index() throws IOException {
        return ok(index.render());
    }
}
