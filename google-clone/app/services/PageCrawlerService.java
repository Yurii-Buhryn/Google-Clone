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
        Document doc = Jsoup.connect(pageUrl).timeout(10000).get();
        URLEncoder.encode(doc.location(), "UTF-8");

        String pageId = URLEncoder.encode(doc.location(), "UTF-8");
        String title = doc.title();
        String content = Jsoup.parse(doc.html()).text();
        Elements imports = doc.select("link[href]");
        Elements links = doc.select("a[href]");

        Elements allLinks = new Elements();
        allLinks.addAll(imports);
        allLinks.addAll(links);

        return new ParseResponse(pageId, title, content, allLinks);
    }
}
