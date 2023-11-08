package com.example.capstoneproject;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

//@OpenAPIDefinition(
//        servers = {
//                @Server(url = "https://api-cvbuilder.monoinfinity.net", description = "Default Server URL"),
//                @Server(url = "http://localhost:8080", description = "Environment Dev"),
//                @Server(url = "http://localhost:3000", description = "Open Cors")
//        }
//)
@SpringBootApplication
//@EnableScheduling
public class CapstoneProjectApplication {

    public static void main(String[] args) {
//        initializeStanfordCoreNLP();
        SpringApplication.run(CapstoneProjectApplication.class, args);
    }

    private static void initializeStanfordCoreNLP() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    }
}
