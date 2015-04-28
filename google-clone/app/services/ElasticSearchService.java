package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dto.AddToIndex;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.F.Promise;
import play.libs.ws.WSResponse;

import java.io.IOException;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public class ElasticSearchService implements IElasticSearchService {

    private static final Integer ELASTIC_API_TIMEOUT = 10000;
    private static final String ELASTIC_SEARCH_URL = "http://dypwfyg8:pfny6csir5yul8l4@ash-6537249.eu-west-1.bonsai.io/";

    public Promise<WSResponse> getById(String indexName, String id) {
        String getByIdUrl = ELASTIC_SEARCH_URL + indexName + "/pageContent/" + id;
        return WS.url(getByIdUrl).setTimeout(ELASTIC_API_TIMEOUT).get();
    }

    @Override
    public Promise<WSResponse> createIndexAsync(String indexName) {
        return WS.url(ELASTIC_SEARCH_URL + indexName).setTimeout(ELASTIC_API_TIMEOUT).post("");
    }

    @Override
    public Promise<WSResponse> searchAsync(String indexName, String searchTerms) {
        String searchQuery = ELASTIC_SEARCH_URL + indexName + "/_search?size=200";

        ObjectNode contentJson = Json.newObject();
        contentJson.put("content", searchTerms);

        ObjectNode queryJson = Json.newObject();
        queryJson.put("match", contentJson);

        ObjectNode searchJson = Json.newObject();
        searchJson.put("_source", Boolean.TRUE);
        searchJson.put("query", queryJson);


        return WS.url(searchQuery).setTimeout(ELASTIC_API_TIMEOUT).post(searchJson);
    }

    @Override
    public Promise<JsonNode> addToIndexAsync(String id, String indexName, AddToIndex addToIndexObj) throws IOException {
        F.Promise<WSResponse> getById = getById(indexName, id);

        F.Promise<JsonNode> addToIndexAsyncResult = getById.map(getByIdResponse -> {
            JsonNode getByIdResponseNode = getByIdResponse.asJson();

            JsonNode source = getByIdResponseNode.get("_source");
            Boolean isAddToIndex = Boolean.TRUE;

            if (source != null && source.get("crawleDate") != null) {
                Long crawleDateTm = source.get("crawleDate").asLong();
                DateTime crawleDate = new DateTime(crawleDateTm);

                Integer diffHours = Hours.hoursBetween(crawleDate, DateTime.now(DateTimeZone.UTC)).getHours();

                isAddToIndex = diffHours >= 5;
            }

            if (isAddToIndex) {
                String addToIndexUrl = ELASTIC_SEARCH_URL + indexName + "/pageContent/" + id;
                F.Promise<WSResponse> addToIndex = WS.url(addToIndexUrl).setTimeout(ELASTIC_API_TIMEOUT).post(Json.toJson(addToIndexObj));

                addToIndex.map(addToIndexResponse -> {
                    JsonNode addToIndexNode = addToIndexResponse.asJson();

                    if (addToIndexNode.get("error").asText().contains("IndexMissingException")) {
                        F.Promise<WSResponse> createIndex = createIndexAsync(indexName);
                        createIndex.map(createIndexResponse -> WS.url(addToIndexUrl).setTimeout(ELASTIC_API_TIMEOUT).post(Json.toJson(addToIndexObj)));
                    }

                    return Json.toJson(addToIndexObj);
                });

                return Json.toJson(addToIndexObj);
            }

            return null;
        });

        return addToIndexAsyncResult;
    }
}
