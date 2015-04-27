package controllers;

import com.google.inject.Inject;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import services.IElasticSearchService;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public class Searcher extends Controller {

    @Inject
    IElasticSearchService elasticSearchService;

    public F.Promise<Result> search(String searchTerms) {
        F.Promise<Result> searchRequestResult = elasticSearchService.searchAsync("index_pages", searchTerms)
                .map(response -> ok(response.asJson()));
        return searchRequestResult;
    }
}
