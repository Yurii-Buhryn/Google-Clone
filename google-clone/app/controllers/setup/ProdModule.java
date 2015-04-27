package controllers.setup;

import com.google.inject.AbstractModule;
import services.ElasticSearchService;
import services.IElasticSearchService;
import services.IPageCrawlerService;
import services.PageCrawlerService;

/**
 * Created by Yurii.Buhryn on 24.04.15.
 */
public class ProdModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IElasticSearchService.class).to(ElasticSearchService.class);
        bind(IPageCrawlerService.class).to(PageCrawlerService.class);
    }

}
