package controllers;

import actors.CrawlerActor;
import actors.messages.CrawlePageMessage;
import com.google.inject.Inject;
import dto.AddToIndex;
import dto.ParseResponse;
import play.mvc.Controller;
import play.mvc.Result;
import services.IElasticSearchService;
import services.IPageCrawlerService;

import java.io.IOException;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public class Crawler extends Controller {

    @Inject
    IPageCrawlerService pageCrawlerService;

    @Inject
    IElasticSearchService elasticSearchService;

    public Result index(String url) throws IOException {
        ParseResponse response = pageCrawlerService.parsePage(url);
        AddToIndex addToIndexObj = new AddToIndex(url, response.title, response.content);
        elasticSearchService.addToIndexAsync(response.pageId, "index_pages", addToIndexObj);

        if(response.links.size() > 0) {
            CrawlePageMessage newCrawlePageMessage1 = new CrawlePageMessage();
            newCrawlePageMessage1.deepSize = 1;
            newCrawlePageMessage1.links = response.links;
            CrawlerActor.CRAWLER_ACTOR.tell(newCrawlePageMessage1, null);
        }

        return ok();
    }
}
