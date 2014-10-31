package es.japanathome.config.metrics;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.util.Assert;

/**
 * SpringBoot Actuator HealthIndicator check for JavaMail.
 */
public class JavaMailHealthIndicator extends AbstractHealthIndicator {

    private final Logger log = LoggerFactory.getLogger(JavaMailHealthIndicator.class);

    public JavaMailHealthIndicator(MandrillApi mandrillApi) {
        Assert.notNull(mandrillApi, "javaMailSender must not be null");
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        log.debug("Initializing JavaMail health indicator");
    }
}
