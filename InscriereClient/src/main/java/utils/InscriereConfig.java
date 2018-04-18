package utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InscriereConfig {
   /* @Bean(name = "userVal")
    public Validator<User> validatorUser(){
        return new UserValidator();
    }

    @Bean(name="userRepo")
    public UserDBRepository userRepository(){
        return new UserDBRepository(validatorUser(),"src/main/resources/bd.properties");
    }

    @Bean(name = "probaVal")
    public Validator<Proba> validatorProba(){
        return new ProbaValidator();
    }

    @Bean(name="probaRepo")
    public ProbaDBRepository probaRepository(){
        return new ProbaDBRepository(validatorProba(),"src/main/resources/bd.properties");
    }*/
}