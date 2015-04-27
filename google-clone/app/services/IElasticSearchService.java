package services;

import com.fasterxml.jackson.databind.JsonNode;
import dto.AddToIndex;
import play.libs.F.Promise;
import play.libs.ws.WSResponse;

import java.io.IOException;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public interface IElasticSearchService {
    Promise<WSResponse> createIndexAsync(String indexName);
    Promise<WSResponse> searchAsync(String indexName, String searchTerms);
    Promise<JsonNode> addToIndexAsync(String id, String indexName, AddToIndex addToIndex) throws IOException;
}
