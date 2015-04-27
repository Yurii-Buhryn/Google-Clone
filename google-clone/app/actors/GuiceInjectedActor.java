package actors;

import akka.actor.IndirectActorProducer;
import akka.actor.UntypedActor;
import com.google.inject.Injector;

/**
 * Created by yurii on 27.04.15.
 */
public class GuiceInjectedActor implements IndirectActorProducer {

    public Injector injector;
    public Class<? extends UntypedActor> actorClass;

    public GuiceInjectedActor(Injector injector, Class<? extends UntypedActor> actorClass) {
        this.injector = injector;
        this.actorClass = actorClass;
    }

    public GuiceInjectedActor() {
    }

    @Override
    public Class<? extends UntypedActor> actorClass() {
        return actorClass;
    }

    @Override
    public UntypedActor produce() {
        return injector.getInstance(actorClass);
    }
}
