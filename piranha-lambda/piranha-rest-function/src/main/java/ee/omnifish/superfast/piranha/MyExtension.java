package ee.omnifish.superfast.piranha;

import cloud.piranha.core.api.AnnotationInfo;
import cloud.piranha.core.api.AnnotationManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

public class MyExtension implements WebApplicationExtension {
    
    private class MyInitializer implements ServletContainerInitializer {

        private static final Class[] ANNOTATED_CLASSES = new Class[] { RestApplication.class, PiranhaCloudRestResource.class };
        
        private WebApplication webApplication;

        public MyInitializer(WebApplication webApplication) {
            this.webApplication = webApplication;
        }

        @Override
        public void onStartup(Set<Class<?>> set, ServletContext sc) throws ServletException {
            final AnnotationManager annotationManager = webApplication.getManager().getAnnotationManager();
            Stream.of(ANNOTATED_CLASSES).forEach(cls -> addAnnotationsForClass(annotationManager, cls));
        }

        private void addAnnotationsForClass(final AnnotationManager annotationManager, Class<?> cls) {
            Arrays.stream(cls.getAnnotations()).forEach(annotationInstance -> {
                //annotationManager.addAnnotatedClass(annotationInstance.annotationType(), cls);
                annotationManager.addAnnotation(new AnnotationInfo<Annotation>() {
                    @Override
                    public Annotation getInstance() {
                        return annotationInstance;
                    }

                    @Override
                    public AnnotatedElement getTarget() {
                        return cls;
                    }
                });
            });
        }
    }

    @Override
    public void configure(WebApplication webApplication) {
        webApplication.addInitializer(new MyInitializer(webApplication));
    }
    
}
