package com.example.capstoneproject.service.impl;
import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.ChatRequest;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import com.example.capstoneproject.Dto.BulletPointDto;
import com.example.capstoneproject.Dto.ResultDto;
import com.example.capstoneproject.service.SentenceService;
import edu.stanford.nlp.pipeline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SentenceServiceImpl implements SentenceService {

    @Autowired
    ChatGPTServiceImpl chatGPTService;
    @Override
    public ResultDto checkSentences(String text) {
        List<String> sentences = splitText(text);
        ResultDto result = new ResultDto();
        List<BulletPointDto> shortBulletPoints = new ArrayList<>();
        List<BulletPointDto> punctuatedBulletPoints = new ArrayList<>();
        List<BulletPointDto> numberBulletPoints = new ArrayList<>();
        List<BulletPointDto> personalPronounsBulletPoints = new ArrayList<>();
        List<BulletPointDto> fillerBulletPoints = new ArrayList<>();
        List<BulletPointDto> quantifiedBulletPoints = new ArrayList<>();

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
            errorBulletShort.setResult("Take a look at bullet " + shortBulletErrors.toString() + ".");
            errorBulletShort.setStatus("Error");
            shortBulletPoints.add(errorBulletShort);
        }else {
            BulletPointDto errorBulletShort = new BulletPointDto();
            errorBulletShort.setStatus("Pass");
            shortBulletPoints.add(errorBulletShort);
        }

        if (!punctuatedBulletErrors.isEmpty()) {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            errorBulletPunctuated.setResult("Take a look at bullet " + punctuatedBulletErrors.toString() + ".");
            errorBulletPunctuated.setStatus("Error");
            punctuatedBulletPoints.add(errorBulletPunctuated);
        }else{
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            errorBulletPunctuated.setStatus("Pass");
            punctuatedBulletPoints.add(errorBulletPunctuated);
        }

        if (validShortBulletCount < 3 || validShortBulletCount > 6) {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            errorBulletPunctuated.setResult("Only " + validShortBulletCount + " found in this section.");
            errorBulletPunctuated.setStatus("Warning");
            numberBulletPoints.add(errorBulletPunctuated);
        } else {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            errorBulletPunctuated.setStatus("Pass");
            numberBulletPoints.add(errorBulletPunctuated);
        }

        // Check for Personal Pronouns in the sentences
        String personalPronouns = checkPersonalPronouns(sentences);
        String[] indices = personalPronouns.split(", ");
        if (!personalPronouns.isEmpty()) {
            BulletPointDto errorBulletPersonalPronouns = new BulletPointDto();
            errorBulletPersonalPronouns.setResult("Take a look at bullet " + personalPronouns + ".");
            errorBulletPersonalPronouns.setStatus("Warning");
            personalPronounsBulletPoints.add(errorBulletPersonalPronouns);
        }else{
            BulletPointDto errorBulletPersonalPronouns = new BulletPointDto();
            errorBulletPersonalPronouns.setStatus("Pass");
            personalPronounsBulletPoints.add(errorBulletPersonalPronouns);
        }

        // Check for Filler Words in the sentences
        String fillerWord = checkFiller(sentences);
        if (!fillerWord.isEmpty()) {
            BulletPointDto errorBulletFillers = new BulletPointDto();
            errorBulletFillers.setResult("Take a look at bullet " + fillerWord + ".");
            errorBulletFillers.setStatus("Warning");
            fillerBulletPoints.add(errorBulletFillers);
        }else {
            BulletPointDto errorBulletFillers = new BulletPointDto();
            errorBulletFillers.setStatus("Pass");
            fillerBulletPoints.add(errorBulletFillers);
        }

        // Check for Quantified in the sentences
        String quantified = containsNumber(sentences);
        if (!quantified.isEmpty()) {
            BulletPointDto errorBulletQuantified = new BulletPointDto();
            errorBulletQuantified.setResult("Take a look at bullet " + quantified + ".");
            errorBulletQuantified.setStatus("Warning");
            quantifiedBulletPoints.add(errorBulletQuantified);
        }else {
            BulletPointDto errorBulletQuantified = new BulletPointDto();
            errorBulletQuantified.setStatus("Pass");
            quantifiedBulletPoints.add(errorBulletQuantified);
        }

        result.setShortList(shortBulletPoints);
        result.setPunctuatedList(punctuatedBulletPoints);
        result.setNumberList(numberBulletPoints);
        result.setPersonalPronounsList(personalPronounsBulletPoints);
        result.setFillerList(fillerBulletPoints);
        result.setQuantifiedList(quantifiedBulletPoints);

        return result;
    }

    @Override
    public List<AtsDto> ListAts(ChatRequest chatRequest) {
        String message1 = "find to keywords: JOB TITLE: " +chatRequest.getTitle()+", JOB DESCRIPTION: "+chatRequest.getMessage();
        String response = chatGPTService.chatWithGPT(message1);
        List<AtsDto> atsDtoList = extractKeywords(response);
        return atsDtoList;
    }

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


}
