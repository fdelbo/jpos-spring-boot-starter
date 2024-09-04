# jPOS Spring Boot Starter
[![JAVA 21](https://img.shields.io/badge/JAVA%20-21-blue.svg)](https://www.java.com/es/)
[![SPRING](https://img.shields.io/badge/SPRING_BOOT%20-3.3.2-green.svg)](https://spring.io/)
[![JPOS](https://img.shields.io/badge/jPOS%20-2.1.9-red.svg)](https://jpos.org/)

Integrate this starter in your project and start sending/receiving ISO-8583 messages to/from any card network


## Documentation
Here you can download the [jPOS Developer Manual](https://jpos.org/doc/proguide.pdf)

## Configuration
In order to use this starter you must:

1. Add this spring config into `application.properties`
```yaml
spring:
  jpos:
    enabled: true
    deploy-path: "/build/resources/main/deploy"
    network-management:
      acquiring-institution-code: "123456"
      network-timeout-ms: 8000 
      echo-test:
        enabled: true
        initial-delay: 30s #waiting time until begin sending echos
        interval: 30s #interval between echos
```

2. Configure jpos (use the example in [/documentation/config-example](documentation/config-example))
   * jpos deployment: folder `deploy` must be located as `spring.jpos.deploy-path` points
   * jpos properties: `jpos.properties` must be located in `/resources` path

## Available services/utils
Once the starter is integrated, you can make use of the services/utils listed above

### JposService
Gives an interface to perform requests to some a network

### NetworkManagementService
- Gives an interface to perform:
  - SignOn requests
  - SignOff requests
  - Echo requests

You can enable/disable a scheduled task for periodic echo requests making use of 
`spring.jpos.network-management.echo-test.enabled` property

### JPOSChannelLoggingFilter
This is a filter you can make use in order to log requests/responses performed to a network.
To make use of it, you need to add it to a channel configuration (see 
[10_channel_jpos.xml](documentation/config-example/deploy/10_channel_jpos.xml) for more details)

### JPOSLogListener
This is a LogListener you can make use in order to log jpos framework logs.
To make use of it, you need to add it to a logger configuration (see 
[00_logger.xml](documentation/config-example/deploy/00_logger.xml) for more details)
