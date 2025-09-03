package de.uniks.stp24.dagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.uniks.stp24.App;
import de.uniks.stp24.service.PrefService;
import org.fulib.fx.FulibFxApp;

import javax.inject.Singleton;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@Module
public class MainModule {

    // TODO provide resources
    @Provides
    ResourceBundle bundle(PrefService prefService) {
        return ResourceBundle.getBundle("de/uniks/stp24/lang/main", prefService.getLocale());
    }


    @Provides
    @Singleton
    ObjectMapper mapper() {
        return new ObjectMapper()
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
                .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
    }

    @Provides
    @Singleton
    DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    }

    @Provides
    FulibFxApp fulibFxApp(App app) {
        return app;
    }

}
