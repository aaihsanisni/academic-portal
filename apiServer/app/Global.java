/**
 * Created by atul on 27/09/15.
 */
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import play.Application;
import play.GlobalSettings;
import play.Play;

public class Global extends GlobalSettings{

    private ApplicationContext applicationContext;

    @Override
    public void onStart(Application arg0) {
        String configLocation = Play.application().configuration().getString("spring.context.location");
        applicationContext = new ClassPathXmlApplicationContext(configLocation);
    }

    @Override
    public <A> A getControllerInstance(Class<A> type) throws Exception {
        return applicationContext.getBean(type);
    }

}
