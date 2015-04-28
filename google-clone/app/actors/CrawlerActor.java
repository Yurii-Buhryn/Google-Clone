package actors;

import actors.messages.CrawlePageMessage;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import dto.AddToIndex;
import dto.ParseResponse;
import play.Logger;
import play.libs.F;
import services.IElasticSearchService;
import services.IPageCrawlerService;

import java.io.IOException;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public class CrawlerActor extends UntypedActor {

    public static ActorRef CRAWLER_ACTOR;

    public static final int MAX_CRAWLE_DEEP_SIZE = 3;

    @Inject
    IPageCrawlerService pageCrawlerService;

    @Inject
    IElasticSearchService elasticSearchService;

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof CrawlePageMessage){
            CrawlePageMessage crawlePageMessage = (CrawlePageMessage) message;
            addPageToIndex(crawlePageMessage);
        } else {
            unhandled(message);
        }
    }

    private void addPageToIndex(CrawlePageMessage crawlePageMessage) throws IOException {
        final Integer crawleLinksDeepSize = crawlePageMessage.deepSize + 1;

        if(crawleLinksDeepSize <= MAX_CRAWLE_DEEP_SIZE) {
            crawlePageMessage.links.forEach(link -> {
                String url = link.attr("abs:href");
                try {
                    ParseResponse response = pageCrawlerService.parsePage(url);
                    AddToIndex addToIndexObj = new AddToIndex(url, response.title, response.content);
                    F.Promise<JsonNode> addToIndexAsync = elasticSearchService.addToIndexAsync(response.pageId, "index_pages", addToIndexObj);

                    addToIndexAsync.map(addToIndexAsyncResponse -> {
                        if(addToIndexAsyncResponse != null) {
                            if(response.links.size() > 0) {
                                CrawlePageMessage newCrawlePageMessage1 = new CrawlePageMessage();
                                newCrawlePageMessage1.deepSize = crawleLinksDeepSize;
                                newCrawlePageMessage1.links = response.links;
                                CrawlerActor.CRAWLER_ACTOR.tell(newCrawlePageMessage1, null);
                            }
                        }

                        return "addToIndexAsync";
                    });
                } catch (Exception e) {
                    Logger.error("Can't connect to url : " + url + ". Details: " + e.getMessage());
                }
            });
        }
    }

}
