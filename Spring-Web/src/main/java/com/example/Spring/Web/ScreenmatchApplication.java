package com.example.Spring.Web;


import com.example.Spring.Web.principal.Principal;
import com.example.Spring.Web.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication {


    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

}
