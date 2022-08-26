package com.juniorrodrigues.efoodapi;

import com.juniorrodrigues.efoodapi.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class) // mostra e ativa no spring nosso JPAReposotory customizado
public class EfoodApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EfoodApiApplication.class, args);
    }

}
