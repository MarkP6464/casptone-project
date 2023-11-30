package com.example.capstoneproject.service.impl;
import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.SectionLogStatus;
import com.example.capstoneproject.mapper.AtsMapper;
import com.example.capstoneproject.repository.AtsRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.EvaluateRepository;
import com.example.capstoneproject.repository.JobDescriptionRepository;
import com.example.capstoneproject.service.TransactionService;
import com.example.capstoneproject.utils.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import com.example.capstoneproject.service.EvaluateService;
import edu.stanford.nlp.pipeline.*;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    SecurityUtil securityUtil;

    private StanfordCoreNLP pipeline;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos,lemma,parse");
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

    public static JLanguageTool langTool = new JLanguageTool(new BritishEnglish());

    @Override
    public List<BulletPointDto> checkSentences(String text) {
        List<String> sentences = splitText(text);

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
            errorBulletCount.setResult("Find " + validShortBulletCount + " found in this section.");
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

        // Check for Grammar in the sentences
        String grammar = checkGrammar(sentences);
        if (!grammar.isEmpty()) {
            BulletPointDto errorBulletGrammar = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(7);
            errorBulletGrammar.setTitle(evaluate.getTitle());
            errorBulletGrammar.setDescription(evaluate.getDescription());
            errorBulletGrammar.setResult(grammar);
            errorBulletGrammar.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletGrammar);
        }else {
            BulletPointDto errorBulletGrammar = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(7);
            errorBulletGrammar.setTitle(evaluate.getTitle());
            errorBulletGrammar.setDescription(evaluate.getDescription());
            errorBulletGrammar.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletGrammar);
        }

        // Check for Passive voice in the sentences
        String passive = checkPassiveVoice(sentences);
        if (!passive.isEmpty()) {
            BulletPointDto errorBulletPassive = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(8);
            errorBulletPassive.setTitle(evaluate.getTitle());
            errorBulletPassive.setDescription(evaluate.getDescription());
            errorBulletPassive.setResult("Take a look at bullet "+passive);
            errorBulletPassive.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletPassive);
        }else {
            BulletPointDto errorBulletPassive = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(8);
            errorBulletPassive.setTitle(evaluate.getTitle());
            errorBulletPassive.setDescription(evaluate.getDescription());
            errorBulletPassive.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletPassive);
        }

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

        // Check for Grammar in the sentences
        String grammar = checkGrammar(sentences);
        if (!grammar.isEmpty()) {
            BulletPointDto errorBulletGrammar = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(7);
            errorBulletGrammar.setTitle(evaluate.getTitle());
            errorBulletGrammar.setDescription(evaluate.getDescription());
            errorBulletGrammar.setResult(grammar);
            errorBulletGrammar.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletGrammar);
        }else {
            BulletPointDto errorBulletGrammar = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(7);
            errorBulletGrammar.setTitle(evaluate.getTitle());
            errorBulletGrammar.setDescription(evaluate.getDescription());
            errorBulletGrammar.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletGrammar);
        }

        // Check for Passive voice in the sentences
        String passive = checkPassiveVoice(sentences);
        if (!passive.isEmpty()) {
            BulletPointDto errorBulletPassive = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(8);
            errorBulletPassive.setTitle(evaluate.getTitle());
            errorBulletPassive.setDescription(evaluate.getDescription());
            errorBulletPassive.setResult("Take a look at bullet "+passive);
            errorBulletPassive.setStatus(SectionLogStatus.Warning);
            allBulletPoints.add(errorBulletPassive);
        }else {
            BulletPointDto errorBulletPassive = new BulletPointDto();
            Evaluate evaluate = evaluateRepository.findById(8);
            errorBulletPassive.setTitle(evaluate.getTitle());
            errorBulletPassive.setDescription(evaluate.getDescription());
            errorBulletPassive.setStatus(SectionLogStatus.Pass);
            allBulletPoints.add(errorBulletPassive);
        }

        return allBulletPoints;
    }

    @Override
    public List<AtsDto> ListAts(int cvId, int jobId, JobDescriptionDto dto, Principal principal) throws JsonProcessingException {
        String system = "Please input the job description in the text box below. This prompt will identify and extract relevant Application Tracking System (ATS) keywords from the description provided. ATS keywords are crucial for optimizing job applications to ensure they pass through automated tracking systems effectively.";
        String userMessage = "Job Description:\n" +
                "“"+dto.getDescription()+"”\n"+
                "Limit only 15 the most important keywords. Limit only 15 the most important keywords, no need to be formal.\n" +
                "Keywords Extracted:";
        List<Map<String, Object>> messagesList = new ArrayList<>();
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", system);
        messagesList.add(systemMessage);
        Map<String, Object> userMessageMap = new HashMap<>();
        userMessageMap.put("role", "user");
        userMessageMap.put("content", userMessage);
        messagesList.add(userMessageMap);
        String messagesJson = new ObjectMapper().writeValueAsString(messagesList);
        transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId());
        String response = chatGPTService.chatWithGPTCoverLetterRevise(messagesJson);
        List<AtsDto> atsDtoList = extractAtsKeywords(response);
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

    public String checkGrammar(List<String> sentences1) {
        StringBuilder result = new StringBuilder();
        try {
            for (String sentenceText : sentences1) {
                List<RuleMatch> matches = langTool.check(sentenceText);
                for (RuleMatch match : matches) {
                    result.append(match.getMessage().replaceAll("<[^>]*>", "").replaceAll("\\?$", "").trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String checkPersonalPronouns(List<String> sentences1) {
        StringBuilder result = new StringBuilder();
        boolean firstMatch = true;
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

    private String checkPassiveVoice(List<String> sentences1) {
        StringBuilder result = new StringBuilder();
        boolean firstMatch = true;
        for (int i = 0; i < sentences1.size(); i++) {
            String sentenceText = sentences1.get(i);
            Annotation document = new Annotation(sentenceText);
            pipeline.annotate(document);
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            boolean hasPersonalPronoun = false;
            for (CoreMap sentence : sentences) {
                // Lấy cây cú pháp (semantic graph) từ câu
                SemanticGraph semanticGraph = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

                // Kiểm tra xem động từ ở vị trí đầu tiên có phải là động từ bị động không
                if (semanticGraph != null && !semanticGraph.isEmpty()) {
                    String pos = semanticGraph.getFirstRoot().tag();
                    String lemma = semanticGraph.getFirstRoot().lemma();

                    // Kiểm tra xem động từ là dạng VBN (past participle) và không phải là "be"
                    if(pos.equals("VBN") && !lemma.equalsIgnoreCase("be")){
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
    private List<AtsDto> extractAtsKeywords(String input) {
        List<AtsDto> atsList = new ArrayList<>();

        // Biểu thức chính quy để tìm các dòng bắt đầu với số và sau đó trích xuất nội dung
        Pattern pattern = Pattern.compile("\\d+\\.\\s+(.*)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String keyword = matcher.group(1);
            AtsDto atsDto = new AtsDto();
            atsDto.setAts(keyword);
            atsList.add(atsDto);
        }

        return atsList;
    }

}
