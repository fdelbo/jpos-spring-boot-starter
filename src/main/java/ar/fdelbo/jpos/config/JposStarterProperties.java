package ar.fdelbo.jpos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "spring.jpos")
public class JposStarterProperties {

    private Boolean enabled;
    private String deployPath;
    private NetworkManagementProperties networkManagement;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDeployPath() {
        return deployPath;
    }

    public void setDeployPath(String deployPath) {
        this.deployPath = deployPath;
    }

    public NetworkManagementProperties getNetworkManagement() {
        return networkManagement;
    }

    public void setNetworkManagement(NetworkManagementProperties networkManagement) {
        this.networkManagement = networkManagement;
    }




    public static class NetworkManagementProperties {
        private String acquiringInstitutionCode;
        private Long networkTimeoutMs;
        private EchoTestProperties echoTest;

        public String getAcquiringInstitutionCode() {
            return acquiringInstitutionCode;
        }

        public void setAcquiringInstitutionCode(String acquiringInstitutionCode) {
            this.acquiringInstitutionCode = acquiringInstitutionCode;
        }

        public Long getNetworkTimeoutMs() {
            return networkTimeoutMs;
        }

        public void setNetworkTimeoutMs(Long networkTimeoutMs) {
            this.networkTimeoutMs = networkTimeoutMs;
        }

        public EchoTestProperties getEchoTest() {
            return echoTest;
        }

        public void setEchoTest(EchoTestProperties echoTest) {
            this.echoTest = echoTest;
        }
    }



    public static class EchoTestProperties {
        private Boolean enabled;
        private Duration initialDelay;
        private Duration interval;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public void setInterval(Duration interval) {
            this.interval = interval;
        }

        public Duration getInitialDelay() {
            return initialDelay;
        }

        public void setInitialDelay(Duration initialDelay) {
            this.initialDelay = initialDelay;
        }

        public Duration getInterval() {
            return interval;
        }

        public void setIntervalMs(Duration interval) {
            this.interval = interval;
        }
    }

}
