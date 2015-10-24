/**
 * Created by atul on 27/09/15.
 */
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import play.*;
import play.mvc.*;
import play.mvc.Http.Request;
import java.lang.reflect.Method;

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

    @Override
    public Action onRequest(Request request, Method actionMethod) {
        System.out.println("before each request..." + request.toString());
        return super.onRequest(request, actionMethod);
    }
}