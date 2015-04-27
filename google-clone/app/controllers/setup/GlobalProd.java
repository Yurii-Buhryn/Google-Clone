package controllers.setup;

import com.google.inject.Guice;

/**
 * Created by Yurii.Buhryn on 24.04.15.
 */
public class GlobalProd extends Global {


    public GlobalProd() {
        super(Guice.createInjector(new ProdModule()));
    }

}
