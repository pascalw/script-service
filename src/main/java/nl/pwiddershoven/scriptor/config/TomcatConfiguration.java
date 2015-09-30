package nl.pwiddershoven.scriptor.config;

import org.apache.catalina.*;
import org.apache.catalina.valves.rewrite.RewriteValve;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfiguration implements EmbeddedServletContainerCustomizer, LifecycleListener {
    private RewriteValve rewriteValve = new RewriteValve();

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
        tomcat.addContextLifecycleListeners(this);
        tomcat.addContextValves(rewriteValve);
    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if (event.getType().equals(Lifecycle.BEFORE_START_EVENT)) {
            /**
             * Workaround to be able to define the rewrite rules in code. By default the RewriteValve expects
             * a rewrite.config file in WEB-INF, but RewriteValve has problems locating the rewrite.config file when
             * running in a jar.
             * 
             * Secondly, we can't immediately call rewriteValve.setConfiguration in #customize because when Tomcat is not
             * fully initialized yet calling rewriteValve.setConfiguration throws an NPE when trying to log.
             */
            try {
                rewriteValve.setConfiguration("RewriteRule ^/admin/.*\\.(.*)$ - [L]\n" +
                                              "RewriteRule ^/admin/?(.*)$ /admin/index.html [L]\n" +
                                              "RewriteRule ^/$ /admin/ [R]");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
