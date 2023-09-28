package com.example.capstoneproject.service.impl;
import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.ChatRequest;
import com.example.capstoneproject.entity.Evaluate;
import com.example.capstoneproject.repository.EvaluateRepository;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import com.example.capstoneproject.Dto.BulletPointDto;
import com.example.capstoneproject.service.EvaluateService;
import edu.stanford.nlp.pipeline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    @Autowired
    EvaluateRepository evaluateRepository;
    @Override
    public List<BulletPointDto> checkSentences(String text) {
        List<String> sentences = splitText(text);
//        ResultDto result = new ResultDto();
//        List<BulletPointDto> shortBulletPoints = new ArrayList<>();
//        List<BulletPointDto> punctuatedBulletPoints = new ArrayList<>();
//        List<BulletPointDto> numberBulletPoints = new ArrayList<>();
//        List<BulletPointDto> personalPronounsBulletPoints = new ArrayList<>();
//        List<BulletPointDto> fillerBulletPoints = new ArrayList<>();
//        List<BulletPointDto> quantifiedBulletPoints = new ArrayList<>();
//        List<BulletPointDto> buzzwordBulletPoints = new ArrayList<>();

        List<BulletPointDto> allBulletPoints = new ArrayList<>();

        List<Integer> shortBulletErrors = new ArrayList<>();
        List<Integer> punctuatedBulletErrors = new ArrayList<>();
        int validShortBulletCount = 0;

        for (int i = 0; i < sentences.size(); i++) {
            String sentence = sentences.get(i);
            if (sentence.length() < 20) {
                shortBulletErrors.add(i + 1);
            }

            if (!sentence.endsWith(".")) {
                punctuatedBulletErrors.add(i + 1);
            }

            validShortBulletCount++;
        }

        if (!shortBulletErrors.isEmpty()) {
            BulletPointDto errorBulletShort = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(3);
            errorBulletShort.setTitle(evaluate.getTitle());
            errorBulletShort.setDescription(evaluate.getDescription());
            errorBulletShort.setResult("Take a look at bullet " + shortBulletErrors.toString() + ".");
            errorBulletShort.setStatus("Error");
            allBulletPoints.add(errorBulletShort);
        }else {
            BulletPointDto errorBulletShort = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(3);
            errorBulletShort.setTitle(evaluate.getTitle());
            errorBulletShort.setDescription(evaluate.getDescription());
            errorBulletShort.setStatus("Pass");
            allBulletPoints.add(errorBulletShort);
        }

        if (!punctuatedBulletErrors.isEmpty()) {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(1);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setResult("Take a look at bullet " + punctuatedBulletErrors.toString() + ".");
            errorBulletPunctuated.setStatus("Error");
            allBulletPoints.add(errorBulletPunctuated);
        }else{
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(1);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setStatus("Pass");
            allBulletPoints.add(errorBulletPunctuated);
        }

        if (validShortBulletCount < 3 || validShortBulletCount > 6) {
            BulletPointDto errorBulletCount = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(2);
            errorBulletCount.setTitle(evaluate.getTitle());
            errorBulletCount.setDescription(evaluate.getDescription());
            errorBulletCount.setResult("Only " + validShortBulletCount + " found in this section.");
            errorBulletCount.setStatus("Warning");
            allBulletPoints.add(errorBulletCount);
        } else {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(2);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setStatus("Pass");
            allBulletPoints.add(errorBulletPunctuated);
        }

        // Check for Personal Pronouns in the sentences
        String personalPronouns = checkPersonalPronouns(sentences);
        if (!personalPronouns.isEmpty()) {
            BulletPointDto errorBulletPersonalPronouns = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(3);
            errorBulletPersonalPronouns.setTitle(evaluate.getTitle());
            errorBulletPersonalPronouns.setDescription(evaluate.getDescription());
            errorBulletPersonalPronouns.setResult("Take a look at bullet " + personalPronouns + ".");
            errorBulletPersonalPronouns.setStatus("Warning");
            allBulletPoints.add(errorBulletPersonalPronouns);
        }else{
            BulletPointDto errorBulletPersonalPronouns = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(3);
            errorBulletPersonalPronouns.setTitle(evaluate.getTitle());
            errorBulletPersonalPronouns.setDescription(evaluate.getDescription());
            errorBulletPersonalPronouns.setStatus("Pass");
            allBulletPoints.add(errorBulletPersonalPronouns);
        }

        // Check for Filler Words in the sentences
        String fillerWord = checkFiller(sentences);
        if (!fillerWord.isEmpty()) {
            BulletPointDto errorBulletFillers = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(1);
            errorBulletFillers.setTitle(evaluate.getTitle());
            errorBulletFillers.setDescription(evaluate.getDescription());
            errorBulletFillers.setResult("Take a look at bullet " + fillerWord + ".");
            errorBulletFillers.setStatus("Warning");
            allBulletPoints.add(errorBulletFillers);
        }else {
            BulletPointDto errorBulletFillers = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(1);
            errorBulletFillers.setTitle(evaluate.getTitle());
            errorBulletFillers.setDescription(evaluate.getDescription());
            errorBulletFillers.setStatus("Pass");
            allBulletPoints.add(errorBulletFillers);
        }

        // Check for Quantified in the sentences
        String quantified = containsNumber(sentences);
        if (!quantified.isEmpty()) {
            BulletPointDto errorBulletQuantified = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(2);
            errorBulletQuantified.setTitle(evaluate.getTitle());
            errorBulletQuantified.setDescription(evaluate.getDescription());
            errorBulletQuantified.setResult("Take a look at bullet " + quantified + ".");
            errorBulletQuantified.setStatus("Warning");
            allBulletPoints.add(errorBulletQuantified);
        }else {
            BulletPointDto errorBulletQuantified = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(2);
            errorBulletQuantified.setTitle(evaluate.getTitle());
            errorBulletQuantified.setDescription(evaluate.getDescription());
            errorBulletQuantified.setStatus("Pass");
            allBulletPoints.add(errorBulletQuantified);
        }

        // Check for Buzzwords in the sentences
//        String buzzwords = checkBuzzword(sentences);
//        if (!buzzwords.isEmpty()) {
//            BulletPointDto errorBulletBuzzword = new BulletPointDto();
//            Evaluate evaluate = evaluateRepository.findById(3);
//            errorBulletBuzzword.setTitle(evaluate.getTitle());
//            errorBulletBuzzword.setDescription(evaluate.getDescription());
//            errorBulletBuzzword.setResult("Take a look at bullet " + quantified + ".");
//            errorBulletBuzzword.setStatus("Warning");
//            buzzwordBulletPoints.add(errorBulletBuzzword);
//        }else {
//            BulletPointDto errorBulletBuzzword = new BulletPointDto();
//            Evaluate evaluate = evaluateRepository.findById(3);
//            errorBulletBuzzword.setTitle(evaluate.getTitle());
//            errorBulletBuzzword.setDescription(evaluate.getDescription());
//            errorBulletBuzzword.setStatus("Pass");
//            buzzwordBulletPoints.add(errorBulletBuzzword);
//        }

//        result.setEvaluate(shortBulletPoints);
//        result.setEvaluate(punctuatedBulletPoints);
//        result.setEvaluate(numberBulletPoints);
//        result.setEvaluate(personalPronounsBulletPoints);
//        result.setEvaluate(fillerBulletPoints);
//        result.setEvaluate(quantifiedBulletPoints);
//        result.setEvaluate(buzzwordBulletPoints);

//        result.setEvaluate(allBulletPoints);

        return allBulletPoints;
    }

    @Override
    public List<AtsDto> ListAts(ChatRequest chatRequest) {
        String message1 = "find to keywords: JOB TITLE: " +chatRequest.getTitle()+", JOB DESCRIPTION: "+chatRequest.getMessage();
        String response = chatGPTService.chatWithGPT(message1);
        List<AtsDto> atsDtoList = extractKeywords(response);
        return atsDtoList;
    }

//    @Override
//    public List<AtsDto> ListBuzzword() {
//        String message1 = "Provide list of Buzzwords (no explanation needed)";
//        String response = chatGPTService.chatWithGPT(message1);
//        List<AtsDto> buzzwordList = extractBuzzwords(response);
//        return buzzwordList;
//    }

    public List<String> splitText(String text) {
        List<String> resultList = new ArrayList<>();
        String[] splitValues = text.split("• ");
        for (String value : splitValues) {
            String trimmedValue = value.trim();
            if (!trimmedValue.isEmpty()) {
                resultList.add(trimmedValue);
            }
        }

        return resultList;
    }

    private String checkPersonalPronouns(List<String> sentences1) {
        StringBuilder result = new StringBuilder();
        boolean firstMatch = true;
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        for (int i = 0; i < sentences1.size(); i++) {
            String sentenceText = sentences1.get(i);
            Annotation document = new Annotation(sentenceText);
            pipeline.annotate(document);
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            boolean hasPersonalPronoun = false;
            for (CoreMap sentence : sentences) {
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    //String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    if (pos.equals("PRP") || pos.equals("PRP$")) {
                        hasPersonalPronoun = true;
                        break;
                    }
                }
            }
            if (hasPersonalPronoun) {
                if (!firstMatch) {
                    result.append(", ");
                }
                result.append(i + 1);
                firstMatch = false;
            }
        }

        return result.toString();
    }

    private String checkFiller(List<String> sentences1) {
        StringBuilder result = new StringBuilder();
        boolean firstMatch = true;
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        for (int i = 0; i < sentences1.size(); i++) {
            String sentenceText = sentences1.get(i);
            Annotation document = new Annotation(sentenceText);
            pipeline.annotate(document);
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            boolean hasPersonalPronoun = false;
            for (CoreMap sentence : sentences) {
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    //String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    if (pos.equals("RB") || pos.equals("RB$")) {
                        hasPersonalPronoun = true;
                        break;
                    }
                }
            }
            if (hasPersonalPronoun) {
                if (!firstMatch) {
                    result.append(", ");
                }
                result.append(i + 1);
                firstMatch = false;
            }
        }
        return result.toString();
    }

    private String checkBuzzword(List<String> sentences1) {
        List<AtsDto> buzzwordList = new ArrayList<>();

        while (buzzwordList.isEmpty()) {
            String message1 = "Provide list of Buzzwords (no explanation needed)";
            String response = chatGPTService.chatWithGPT(message1);
            buzzwordList = extractBuzzwords(response);
        }
        StringBuilder resultBuilder = new StringBuilder();

        for (String sentence : sentences1) {
            for (AtsDto buzzword : buzzwordList) {
                String buzzwordText = buzzword.getAst();
                if (sentence.contains(buzzwordText)) {
                    resultBuilder.append(sentence).append("\n");
                    break;
                }
            }
        }

        return resultBuilder.toString();
    }

    private String containsNumber(List<String> sentences1) {
        List<Integer> errorIndices = new ArrayList<>();

        for (int i = 0; i < sentences1.size(); i++) {
            String sentence = sentences1.get(i);
            if (!sentence.matches(".*\\d+.*")) {
                errorIndices.add(i + 1); // Lưu chỉ số của phần tử bị lỗi (1-based index).
            }
        }

        if (!errorIndices.isEmpty()) {
            // Chuyển danh sách chỉ số lỗi thành chuỗi kết quả.
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < errorIndices.size(); i++) {
                result.append(errorIndices.get(i));
                if (i < errorIndices.size() - 1) {
                    result.append(", ");
                }
            }
            return result.toString();
        } else {
            return "";
        }
    }

    public static List<AtsDto> extractKeywords(String response) {
        List<AtsDto> keywordsList = new ArrayList<>();
        Pattern pattern = Pattern.compile(":\\s*(.*?)\\s*$", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);

        while (matcher.find()) {
            String[] keywords = matcher.group(1).split(",\\s*|\n\\d+\\.\\s*");
            for (String keyword : keywords) {
                AtsDto atsDto = new AtsDto();
                atsDto.setAst(keyword.trim());
                keywordsList.add(atsDto);
            }
        }

        return keywordsList;
    }

    private static List<AtsDto> extractBuzzwords(String input) {
        List<AtsDto> buzzwordsList = new ArrayList<>();
        Pattern pattern = Pattern.compile("([A-Za-z0-9\\s()]+)");

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String buzzword = matcher.group(1).trim();
            AtsDto atsDto = new AtsDto();
            atsDto.setAst(buzzword);
            buzzwordsList.add(atsDto);
        }

        return buzzwordsList;
    }


}
