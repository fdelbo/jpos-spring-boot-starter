package ar.fdelbo.jpos.config;

import ar.fdelbo.jpos.service.NetworkManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Configuration
@ConditionalOnProperty(prefix = "spring", value = "jpos.enabled", havingValue = "true")
public class JposScheduling {

    private static final Logger LOGGER = LoggerFactory.getLogger(JposScheduling.class);
    private static final int SCHEDULER_POOL_SIZE = 5;

    @Bean
    public ThreadPoolTaskScheduler jposThreadPoolTaskScheduler(final NetworkManagementService networkManagementService,
                                                               final JposStarterProperties jposStarterProperties) {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(SCHEDULER_POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("JPOS-TASK");
        threadPoolTaskScheduler.initialize();

        final var echoEnabled = Optional.ofNullable(jposStarterProperties.getNetworkManagement().getEchoTest())
                .map(JposStarterProperties.EchoTestProperties::getEnabled)
                .orElse(false);
        if(echoEnabled) {
            final Duration initialDelay = jposStarterProperties.getNetworkManagement().getEchoTest().getInitialDelay();
            final Duration interval = jposStarterProperties.getNetworkManagement().getEchoTest().getInterval();
            LOGGER.info("Scheduling echo-test along with [initial-delay: {} - interval: {}]", initialDelay, interval);

            threadPoolTaskScheduler.scheduleWithFixedDelay(networkManagementService::sendEchoTest,
                    Instant.now().plus(initialDelay.getSeconds(), ChronoUnit.SECONDS),
                    interval);
        }

        return threadPoolTaskScheduler;
    }
}
