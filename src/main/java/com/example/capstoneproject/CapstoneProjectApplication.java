package com.example.capstoneproject;

import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.repository.ExpertRepository;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@OpenAPIDefinition(
		servers = {
				@Server(url = "https://cvbuilder-api.monoinfinity.net", description = "Default Server URL"),
				@Server(url = "http://localhost:8080", description = "Environment Dev"),
				@Server(url = "http://localhost:3000", description = "Open Cors")
		}
)
@SpringBootApplication
public class CapstoneProjectApplication {

	public static void main(String[] args) {
		initializeStanfordCoreNLP();

		SpringApplication.run(CapstoneProjectApplication.class, args);
	}

	private static void initializeStanfordCoreNLP() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	}

}
