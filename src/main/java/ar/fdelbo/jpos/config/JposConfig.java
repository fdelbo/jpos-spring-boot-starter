package ar.fdelbo.jpos.config;

import jakarta.annotation.PreDestroy;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

import static ar.fdelbo.jpos.config.JposConfig.JPOS_PROPERTIES_KEY;

@Configuration
@PropertySource(value = "classpath:jpos.properties", name = JPOS_PROPERTIES_KEY)
@ConditionalOnProperty(prefix = "spring", value = "jpos.enabled", havingValue = "true")
public class JposConfig {

    public static final String JPOS_PROPERTIES_KEY = "JposProperties";
    private static final String QMUX_NAME = "jpos-multiplexer";

    private Q2 q2Server;

    @Bean
    public Q2 q2(final Environment environment, final JposStarterProperties jposStarterProperties) {
        configureJposProperties(environment);

        final var deployPath = new ClassPathResource(jposStarterProperties.getDeployPath()).getPath();
        q2Server = new Q2(deployPath);
        q2Server.start();

        //blocks up to 10 seconds until q2 server is ready
        if(!q2Server.ready(10000)) {
            throw new RuntimeException("Q2 Server is not ready");
        }

        return q2Server;
    }

    @Bean
    public QMUX qmux(final Q2 q2Server) throws NameRegistrar.NotFoundException {
        return NameRegistrar.get("mux." + QMUX_NAME);
    }

    @PreDestroy
    public void preDestroy() {
        if(q2Server.running()) {
            q2Server.shutdown(true);
        }
    }

    private void configureJposProperties(Environment environment) {
        final var jposConfig = ((ConfigurableEnvironment) environment)
                .getPropertySources()
                .get(JPOS_PROPERTIES_KEY);

        final var properties = (Properties) jposConfig.getSource();
        properties.forEach((k, v) -> System.setProperty(k.toString(), v.toString()));
    }
}
