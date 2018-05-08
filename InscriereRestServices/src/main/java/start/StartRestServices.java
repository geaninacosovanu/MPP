package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("validator")
@ComponentScan("services.rest")
@ComponentScan("repository")

public class StartRestServices {
    public static void main(String[] args) {

        SpringApplication.run(StartRestServices.class, args);
    }
}
