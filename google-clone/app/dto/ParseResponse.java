package dto;

import org.jsoup.select.Elements;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public class ParseResponse {
    public String pageId;
    public String title;
    public Elements links;
    public String content;

    public ParseResponse(String pageId, String title, String content, Elements links) {
        this.pageId = pageId;
        this.title = title;
        this.content = content;
        this.links = links;
    }
}
