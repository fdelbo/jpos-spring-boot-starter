package ar.fdelbo.jpos.config;

import ar.fdelbo.jpos.service.JposService;
import ar.fdelbo.jpos.service.NetworkManagementService;
import org.jpos.q2.iso.QMUX;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring", value = "jpos.enabled", havingValue = "true")
public class ServicesConfig {

    @Bean
    public JposService jposService(final JposStarterProperties jposStarterProperties,
                                   final QMUX qmux) {

        return new JposService(jposStarterProperties.getNetworkManagement().getNetworkTimeoutMs(), qmux);
    }

    @Bean
    public NetworkManagementService networkManagementService(final JposService jposService) {
        return new NetworkManagementService(jposService);
    }

}
