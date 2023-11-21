package com.example.capstoneproject.service.impl;
import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.SectionLogStatus;
import com.example.capstoneproject.mapper.AtsMapper;
import com.example.capstoneproject.repository.AtsRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.EvaluateRepository;
import com.example.capstoneproject.repository.JobDescriptionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import com.example.capstoneproject.service.EvaluateService;
import edu.stanford.nlp.pipeline.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    private StanfordCoreNLP pipeline;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        this.pipeline = new StanfordCoreNLP(props);
    }

    @Autowired
    EvaluateRepository evaluateRepository;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    AtsRepository atsRepository;

    @Autowired
    AtsServiceImpl atsService;

    @Autowired
    AtsMapper atsMapper;

    @Autowired
    JobDescriptionRepository jobDescriptionRepository;

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
            Evaluate evaluate = evaluateRepository.findById(1);
            errorBulletShort.setTitle(evaluate.getTitle());
            errorBulletShort.setDescription(evaluate.getDescription());
            errorBulletShort.setResult("Take a look at bullet " + shortBulletErrors.toString() + ".");
            errorBulletShort.setStatus(SectionLogStatus.Error);
            allBulletPoints.add(errorBulletShort);
        }else {
            BulletPointDto errorBulletShort = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(1);
            errorBulletShort.setTitle(evaluate.getTitle());
            errorBulletShort.setDescription(evaluate.getDescription());
            errorBulletShort.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletShort);
        }

        if (!punctuatedBulletErrors.isEmpty()) {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(2);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setResult("Take a look at bullet " + punctuatedBulletErrors.toString() + ".");
            errorBulletPunctuated.setStatus(SectionLogStatus.Error);
            allBulletPoints.add(errorBulletPunctuated);
        }else{
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(2);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletPunctuated);
        }

        if (validShortBulletCount < 3 || validShortBulletCount > 6) {
            BulletPointDto errorBulletCount = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(3);
            errorBulletCount.setTitle(evaluate.getTitle());
            errorBulletCount.setDescription(evaluate.getDescription());
            errorBulletCount.setResult("Only " + validShortBulletCount + " found in this section.");
            errorBulletCount.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletCount);
        } else {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(3);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletPunctuated);
        }

        // Check for Personal Pronouns in the sentences
        String personalPronouns = checkPersonalPronouns(sentences);
        if (!personalPronouns.isEmpty()) {
            BulletPointDto errorBulletPersonalPronouns = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(4);
            errorBulletPersonalPronouns.setTitle(evaluate.getTitle());
            errorBulletPersonalPronouns.setDescription(evaluate.getDescription());
            errorBulletPersonalPronouns.setResult("Take a look at bullet " + personalPronouns + ".");
            errorBulletPersonalPronouns.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletPersonalPronouns);
        }else{
            BulletPointDto errorBulletPersonalPronouns = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(4);
            errorBulletPersonalPronouns.setTitle(evaluate.getTitle());
            errorBulletPersonalPronouns.setDescription(evaluate.getDescription());
            errorBulletPersonalPronouns.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletPersonalPronouns);
        }

        // Check for Filler Words in the sentences
        String fillerWord = checkFiller(sentences);
        if (!fillerWord.isEmpty()) {
            BulletPointDto errorBulletFillers = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(5);
            errorBulletFillers.setTitle(evaluate.getTitle());
            errorBulletFillers.setDescription(evaluate.getDescription());
            errorBulletFillers.setResult("Take a look at bullet " + fillerWord + ".");
            errorBulletFillers.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletFillers);
        }else {
            BulletPointDto errorBulletFillers = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(5);
            errorBulletFillers.setTitle(evaluate.getTitle());
            errorBulletFillers.setDescription(evaluate.getDescription());
            errorBulletFillers.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletFillers);
        }

        // Check for Quantified in the sentences
        String quantified = containsNumber(sentences);
        if (!quantified.isEmpty()) {
            BulletPointDto errorBulletQuantified = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(6);
            errorBulletQuantified.setTitle(evaluate.getTitle());
            errorBulletQuantified.setDescription(evaluate.getDescription());
            errorBulletQuantified.setResult("Take a look at bullet " + quantified + ".");
            errorBulletQuantified.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletQuantified);
        }else {
            BulletPointDto errorBulletQuantified = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(6);
            errorBulletQuantified.setTitle(evaluate.getTitle());
            errorBulletQuantified.setDescription(evaluate.getDescription());
            errorBulletQuantified.setStatus(SectionLogStatus.Pass);
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
    public List<BulletPointDto> checkSentencesSecond(EvaluateDescriptionDto dto) {
        List<String> sentences = splitText(dto.getSentences());

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
            Evaluate evaluate = evaluateRepository.findById(1);
            errorBulletShort.setTitle(evaluate.getTitle());
            errorBulletShort.setDescription(evaluate.getDescription());
            errorBulletShort.setResult("Take a look at bullet " + shortBulletErrors.toString() + ".");
            errorBulletShort.setStatus(SectionLogStatus.Error);
            allBulletPoints.add(errorBulletShort);
        }else {
            BulletPointDto errorBulletShort = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(1);
            errorBulletShort.setTitle(evaluate.getTitle());
            errorBulletShort.setDescription(evaluate.getDescription());
            errorBulletShort.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletShort);
        }

        if (!punctuatedBulletErrors.isEmpty()) {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(2);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setResult("Take a look at bullet " + punctuatedBulletErrors.toString() + ".");
            errorBulletPunctuated.setStatus(SectionLogStatus.Error);
            allBulletPoints.add(errorBulletPunctuated);
        }else{
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(2);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletPunctuated);
        }

        if (validShortBulletCount < 3 || validShortBulletCount > 6) {
            BulletPointDto errorBulletCount = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(3);
            errorBulletCount.setTitle(evaluate.getTitle());
            errorBulletCount.setDescription(evaluate.getDescription());
            errorBulletCount.setResult("Only " + validShortBulletCount + " found in this section.");
            errorBulletCount.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletCount);
        } else {
            BulletPointDto errorBulletPunctuated = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(3);
            errorBulletPunctuated.setTitle(evaluate.getTitle());
            errorBulletPunctuated.setDescription(evaluate.getDescription());
            errorBulletPunctuated.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletPunctuated);
        }

        // Check for Personal Pronouns in the sentences
        String personalPronouns = checkPersonalPronouns(sentences);
        if (!personalPronouns.isEmpty()) {
            BulletPointDto errorBulletPersonalPronouns = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(4);
            errorBulletPersonalPronouns.setTitle(evaluate.getTitle());
            errorBulletPersonalPronouns.setDescription(evaluate.getDescription());
            errorBulletPersonalPronouns.setResult("Take a look at bullet " + personalPronouns + ".");
            errorBulletPersonalPronouns.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletPersonalPronouns);
        }else{
            BulletPointDto errorBulletPersonalPronouns = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(4);
            errorBulletPersonalPronouns.setTitle(evaluate.getTitle());
            errorBulletPersonalPronouns.setDescription(evaluate.getDescription());
            errorBulletPersonalPronouns.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletPersonalPronouns);
        }

        // Check for Filler Words in the sentences
        String fillerWord = checkFiller(sentences);
        if (!fillerWord.isEmpty()) {
            BulletPointDto errorBulletFillers = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(5);
            errorBulletFillers.setTitle(evaluate.getTitle());
            errorBulletFillers.setDescription(evaluate.getDescription());
            errorBulletFillers.setResult("Take a look at bullet " + fillerWord + ".");
            errorBulletFillers.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletFillers);
        }else {
            BulletPointDto errorBulletFillers = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(5);
            errorBulletFillers.setTitle(evaluate.getTitle());
            errorBulletFillers.setDescription(evaluate.getDescription());
            errorBulletFillers.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletFillers);
        }

        // Check for Quantified in the sentences
        String quantified = containsNumber(sentences);
        if (!quantified.isEmpty()) {
            BulletPointDto errorBulletQuantified = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(6);
            errorBulletQuantified.setTitle(evaluate.getTitle());
            errorBulletQuantified.setDescription(evaluate.getDescription());
            errorBulletQuantified.setResult("Take a look at bullet " + quantified + ".");
            errorBulletQuantified.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletQuantified);
        }else {
            BulletPointDto errorBulletQuantified = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(6);
            errorBulletQuantified.setTitle(evaluate.getTitle());
            errorBulletQuantified.setDescription(evaluate.getDescription());
            errorBulletQuantified.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletQuantified);
        }

        return allBulletPoints;
    }

    @Override
    public List<AtsDto> ListAts(int cvId, int jobId, JobDescriptionDto chatRequest) throws JsonProcessingException {
        String message1 = "find 10 keywords: JOB TITLE: " +chatRequest.getTitle()+", JOB DESCRIPTION: "+chatRequest.getDescription();
        String response = chatGPTService.chatWithGPT(message1);
        List<AtsDto> atsDtoList = extractKeywords(response);
        List<AtsDto> atsList = new ArrayList<>();
        Optional<JobDescription> jobDescription = jobDescriptionRepository.findById(jobId);
        if (jobDescription.isPresent()) {
            for (AtsDto atsDto : atsDtoList) {
                Ats ats = new Ats();
                ats.setAts(atsDto.getAts());
                ats.setJobDescription(jobDescription.get());
                atsService.createAts(ats);
            }
        }
        //check status of Ats
        List<Ats> ats = atsRepository.findAllByJobDescriptionId(jobId);
        Cv cv = cvRepository.getById(cvId);
        StringBuilder combinedDescription = new StringBuilder();
        if (Objects.nonNull(cv)){
            CvBodyDto cvBodyDto = cv.deserialize();

            cvBodyDto.getSkills().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });

            cvBodyDto.getCertifications().forEach(x -> {
                String description = x.getCertificateRelevance();
                String name = x.getName();
                combinedDescription.append(description).append(" ").append(name).append(" ");
            });

            cvBodyDto.getEducations().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });

            cvBodyDto.getExperiences().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });

            cvBodyDto.getProjects().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });

            cvBodyDto.getInvolvements().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });
            String combinedString = combinedDescription.toString();
            for (Ats ats1 : ats) {
                String atsValue = ats1.getAts();
                if (combinedString.contains(atsValue)) {
                    atsList.add(new AtsDto(ats1.getAts(),SectionLogStatus.Pass));
                }else{
                    atsList.add(new AtsDto(ats1.getAts(),SectionLogStatus.Warning));
                }
            }
        }


        return atsList;
    }

    @Override
    public List<AtsDto> getAts(int cvId, int jobId) throws JsonProcessingException {
        List<Ats> ats = atsRepository.findAllByJobDescriptionId(jobId);
        List<AtsDto> atsList = new ArrayList<>();
        Cv cv = cvRepository.getById(cvId);
        StringBuilder combinedDescription = new StringBuilder();
        if (Objects.nonNull(cv)){
            CvBodyDto cvBodyDto = cv.deserialize();

            cvBodyDto.getSkills().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });

            cvBodyDto.getCertifications().forEach(x -> {
                String description = x.getCertificateRelevance();
                String name = x.getName();
                combinedDescription.append(description).append(" ").append(name).append(" ");
            });

            cvBodyDto.getEducations().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });

            cvBodyDto.getExperiences().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });

            cvBodyDto.getProjects().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });

            cvBodyDto.getInvolvements().forEach(x -> {
                String description = x.getDescription();
                combinedDescription.append(description).append(" ");
            });
            String combinedString = combinedDescription.toString();
            for (Ats ats1 : ats) {
                String atsValue = ats1.getAts();
                if (combinedString.contains(atsValue)) {
                    atsList.add(new AtsDto(ats1.getAts(),SectionLogStatus.Pass));
                }else{
                    atsList.add(new AtsDto(ats1.getAts(),SectionLogStatus.Warning));
                }
            }
        }
        return atsList;
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
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
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
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
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
                String buzzwordText = buzzword.getAts();
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

//    public static List<AtsDto> extractKeywords(String response) {
//        List<AtsDto> keywordsList = new ArrayList<>();
//        Pattern pattern = Pattern.compile(":\\s*(.*?)\\s*$", Pattern.DOTALL);
//        Matcher matcher = pattern.matcher(response);
//
//        while (matcher.find()) {
//            String[] keywords = matcher.group(1).split(",\\s*|\n\\d+\\.\\s*");
//            for (String keyword : keywords) {
//                AtsDto atsDto = new AtsDto();
//                atsDto.setAst(keyword.trim());
//                keywordsList.add(atsDto);
//            }
//        }
//
//        return keywordsList;
//    }
    public static List<AtsDto> extractKeywords(String response) {
    List<AtsDto> keywordsList = new ArrayList<>();
    Pattern pattern = Pattern.compile("(\\d+\\.\\s*)(.*?)\\n|$");
    Matcher matcher = pattern.matcher(response);

    while (matcher.find()) {
        String keyword = matcher.group(2);
        if (keyword != null) {
            String[] keywords = keyword.split(",\\s*");
            for (String k : keywords) {
                AtsDto atsDto = new AtsDto();
                atsDto.setAts(k.trim());
                keywordsList.add(atsDto);
            }
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
            atsDto.setAts(buzzword);
            buzzwordsList.add(atsDto);
        }

        return buzzwordsList;
    }


}
