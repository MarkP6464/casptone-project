package com.example.capstoneproject;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.log4j.Log4j2;
import static java.util.Arrays.stream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.Properties;

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
//        String text = "The lion was killed by the hunter.";
//
//        // Tạo đối tượng StanfordCoreNLP với các property cần thiết
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//
//        // Tạo annotation cho đoạn văn bản
//        Annotation document = new Annotation(text);
//
//        // Chạy pipeline để xử lý văn bản
//        pipeline.annotate(document);
//
//        // Lấy danh sách câu từ annotation
//        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//
//        // Phân tích mỗi câu để phát hiện thì bị động
//        for (CoreMap sentence : sentences) {
//            boolean isPassive = isPassiveVoice(sentence);
//            System.out.println("Passive voice detected in sentence: " + sentence.toString());
//            System.out.println("Is passive voice? " + isPassive);
//            System.out.println();
//        }
        dotEnvSafeCheck();
        SpringApplication.run(CapstoneProjectApplication.class, args);
    }

    private static void dotEnvSafeCheck() {
        final var dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .filename(".env")
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

//    private static boolean isPassiveVoice(CoreMap sentence) {
//        // Lấy cây cú pháp (semantic graph) từ câu
//        SemanticGraph semanticGraph = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
//
//        // Kiểm tra xem động từ ở vị trí đầu tiên có phải là động từ bị động không
//        if (semanticGraph != null && !semanticGraph.isEmpty()) {
//            String pos = semanticGraph.getFirstRoot().tag();
//            String lemma = semanticGraph.getFirstRoot().lemma();
//
//            // Kiểm tra xem động từ là dạng VBN (past participle) và không phải là "be"
//            return pos.equals("VBN") && !lemma.equalsIgnoreCase("be");
//        }
//
//        return false;
//    }
}
