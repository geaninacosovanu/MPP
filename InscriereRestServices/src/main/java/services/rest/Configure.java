package services.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configure {
    @Bean
    public String propFile()
    {
        return "InscriereServer/src/main/resources/server.properties";
    }
}

