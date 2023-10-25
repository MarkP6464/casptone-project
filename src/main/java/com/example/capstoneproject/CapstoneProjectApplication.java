package com.example.capstoneproject;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


import java.util.Properties;

@OpenAPIDefinition(
        servers = {
                @Server(url = "https://api-cvbuilder.monoinfinity.net", description = "Default Server URL"),
                @Server(url = "http://localhost:8082", description = "Environment Dev"),
                @Server(url = "http://localhost:3000", description = "Open Cors")
        }
)
@SpringBootApplication
@EnableDiscoveryClient
public class CapstoneProjectApplication {

    public static void main(String[] args) {
        initializeStanfordCoreNLP();
//    String fullString = "Participating in MoMo Talent Program 2022. Contributing to the gamification campaign, MoMo Barista, with a target of achieving 4,000,000 MAU. Defining data tracking in collaboration with developers. Collaborating with marketing, agencies, and developers to manage 800 recipes and 8000 categories.";
//    String substring = "achieving 4,000,000 MAU. Defining";
//
//    if (isSubstringInString(fullString, substring)) {
//       System.out.println("Substring exists in the full string and matches.");
//    } else {
//       System.out.println("Substring does not exist in the full string or does not match.");
//    }
//    String input = "• Participating in <comment content='That so good'>MoMo Talent Program</comment> 2022. • Contributing to the <comment content='this is test 2'>gamification campaign</comment>, MoMo Barista, with a target of achieving 4,000,000 MAU. • Defining <comment>data tracking in collaboration</comment> with developers. • Collaborating with marketing, agencies, and <comment id='7fad381c-433a-42f6-90cf-20df9f0bf36c' content='this is test 5'>developers to manage 800</comment> recipes and 8000 categories.";
//
//    String output = removeCommentTagsWithoutIdAndContent(input);
//    System.out.println(output);

        SpringApplication.run(CapstoneProjectApplication.class, args);
    }

    private static void initializeStanfordCoreNLP() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    }
}
