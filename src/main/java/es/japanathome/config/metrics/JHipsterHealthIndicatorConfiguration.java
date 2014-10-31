package es.japanathome.config.metrics;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
public class JHipsterHealthIndicatorConfiguration {

    @Inject
    private MandrillApi mandrillApi;

    @Inject
    private DataSource dataSource;

    @Bean
    public HealthIndicator dbHealthIndicator() {
        return new DatabaseHealthIndicator(dataSource);
    }

    @Bean
    public HealthIndicator mailHealthIndicator() {
        return new JavaMailHealthIndicator(mandrillApi);
    }
}
