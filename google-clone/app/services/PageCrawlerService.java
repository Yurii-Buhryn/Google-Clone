package services;

import dto.ParseResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public class PageCrawlerService implements IPageCrawlerService{

    @Override
    public ParseResponse parsePage(String pageUrl) throws IOException {
        Document doc = Jsoup
                .connect(pageUrl)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                .timeout(10000)
                .get();

        URLEncoder.encode(doc.location(), "UTF-8");

        String pageId = URLEncoder.encode(doc.location(), "UTF-8");
        String title = doc.title();
        String content = Jsoup.parse(doc.html()).text();
        Elements links = doc.select("a[href]");

        return new ParseResponse(pageId, title, content, links);
    }
}
