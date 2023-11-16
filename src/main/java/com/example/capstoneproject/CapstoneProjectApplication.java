package com.example.capstoneproject;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.log4j.Log4j2;
import static java.util.Arrays.stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Properties;

//@OpenAPIDefinition(
//        servers = {
//                @Server(url = "https://api-cvbuilder.monoinfinity.net", description = "Default Server URL"),
//                @Server(url = "http://localhost:8080", description = "Environment Dev"),
//                @Server(url = "http://localhost:3000", description = "Open Cors")
//        }
//)
@SpringBootApplication
@Log4j2
@ConfigurationPropertiesScan
@EnableScheduling
public class CapstoneProjectApplication {

    enum DotEnv {
        PORT,
        CLIENT_ORIGIN_URL,
        OKTA_OAUTH2_ISSUER,
        OKTA_OAUTH2_AUDIENCE
    }


    public static void main(String[] args) {
//        initializeStanfordCoreNLP();
        dotEnvSafeCheck();
        SpringApplication.run(CapstoneProjectApplication.class, args);
    }

//    private static void initializeStanfordCoreNLP() {
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
////        props.setProperty("annotators", "tokenize");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//    }

    private static void dotEnvSafeCheck() {
        final var dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .filename("casptone-project/src/.env")
                .load();

        stream(DotEnv.values())
                .map(DotEnv::name)
                .filter(varName -> dotenv.get(varName, "").isEmpty())
                .findFirst()
                .ifPresent(varName -> {
                    log.error("[Fatal] Missing or empty environment variable: {}", varName);

                    System.exit(1);
                });
    }
}
