package services;

import dto.ParseResponse;

import java.io.IOException;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public interface IPageCrawlerService {
    ParseResponse parsePage(String pageUrl) throws IOException;
}
