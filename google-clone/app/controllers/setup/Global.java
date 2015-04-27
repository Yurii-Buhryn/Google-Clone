package controllers.setup;

import actors.CrawlerActor;
import actors.GuiceInjectedActor;
import akka.actor.Props;
import com.google.inject.Injector;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

/**
 * Created by Yurii.Buhryn on 16.04.15.
 */
public class Global extends GlobalSettings {
    private Injector injector;

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
        return injector.getInstance(controllerClass);
    }

    public Global(Injector injector) {
        this.injector = injector;
    }

    public Injector getInjector() {
        return injector;
    }

    public void onStart(Application app) {
        CrawlerActor.CRAWLER_ACTOR =  Akka.system()
                .actorOf(Props.create(GuiceInjectedActor.class, injector, CrawlerActor.class));
    }
}
