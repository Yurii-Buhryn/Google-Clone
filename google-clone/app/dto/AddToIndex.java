package dto;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;

/**
 * Created by Yurii.Buhryn on 27.04.15.
 */
public class AddToIndex {
    public String url;
    public String title;
    public String content;
    public Date crawleDate;

    public AddToIndex(String url, String title, String content) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.crawleDate = DateTime.now(DateTimeZone.UTC).toDate();
    }
}
