package hu.bearmaster.minecraftstarter.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import hu.bearmaster.minecraftstarter.dashboard.domain.formatter.MapNameFormatter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatter(mapNameFormatter());
    }
    
    @Bean
    public MapNameFormatter mapNameFormatter() {
        return new MapNameFormatter();
    }

}
