package com.example.capstoneproject.service.impl;
import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.AnalyzeScoreDto;
import com.example.capstoneproject.Dto.responses.EvaluateViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.enums.SectionLogStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.AtsMapper;
import com.example.capstoneproject.repository.*;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    @Autowired
    SectionLogRepository sectionLogRepository;

    @Autowired
    ScoreLogRepository scoreLogRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    EducationRepository educationRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    ExperienceRepository experienceRepository;
    @Autowired
    CertificationRepository certificationRepository;
    @Autowired
    InvolvementRepository involvementRepository;

    @Autowired
    ProjectRepository projectRepository;

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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletShort.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletShort.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletPunctuated.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletPunctuated.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletCount.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletCount.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletPersonalPronouns.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletPersonalPronouns.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletFillers.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletFillers.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletQuantified.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletQuantified.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletGrammar.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletGrammar.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletPassive.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletPassive.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletShort.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletShort.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletPunctuated.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletPunctuated.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletCount.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletCount.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletPersonalPronouns.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletPersonalPronouns.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletFillers.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletFillers.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletQuantified.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletQuantified.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletGrammar.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletGrammar.setStatus(SectionLogStatus.Warning);
            }
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
            // Check if evaluate.getCritical() is true or false
            if (evaluate.getCritical()) {
                errorBulletPassive.setStatus(SectionLogStatus.Error);
            } else {
                errorBulletPassive.setStatus(SectionLogStatus.Warning);
            }
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

    @Override
    public ScoreDto getEvaluateCv(int userId, int cvId) throws JsonProcessingException {
        Cv cv = cvRepository.getById(cvId);
        ScoreDto scoreDto = new ScoreDto();
        List<SectionCvDto> sectionCvDtos = new ArrayList<>();
        List<SectionAddScoreLogDto> sectionAddScoreLogDtos = new ArrayList<>();
        List<Evaluate> evaluates = evaluateRepository.findAll();
        Optional<Score> scoreOptional = scoreRepository.findByCv_Id(cvId);
        List<ContentDto> contentList;
        List<ContentDto> practiceList;
        List<ContentDto> optimizationList;
        List<ContentDto> formatList;
        final int[] totalWords = {0};
        if (Objects.nonNull(cv)){
            AtomicInteger total = new AtomicInteger(0);
            AtomicInteger totalExperiences = new AtomicInteger(0);
            CvBodyDto cvBodyDto = cv.deserialize();
            for (SkillDto x : cvBodyDto.getSkills()) {
                if (x.getIsDisplay()) {
                    String description = x.getDescription();
                    if (description != null) {
                        String[] words = description.split("\\s+");
                        totalWords[0] += words.length;
                    }
                }
            }

            cvBodyDto.getCertifications().forEach(x -> {
                if (x.getIsDisplay()) {
                    String skill = x.getCertificateRelevance();
                    if (skill != null) {
                        String word = skill;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                    }
                }
            });

            cvBodyDto.getEducations().forEach(x -> {
                if(x.getIsDisplay()){
                    String minor = x.getMinor();
                    String description = x.getDescription();
                    if(minor==null){
                        minor = "";
                    }
                    if(description==null){
                        description = "";
                    }
                    String word = description + " " + minor;
                    String[] words = word.split("\\s+");
                    totalWords[0] += words.length;
                }
            });

            cvBodyDto.getExperiences().forEach(x -> {
                if(x.getIsDisplay()){
                    total.addAndGet(1);
                    totalExperiences.addAndGet(1);
                    int experienceId = x.getId();
                    String title = x.getRole();
                    String location = x.getLocation();
                    String duration = x.getDuration();
                    String company = x.getCompanyName();
                    String description = x.getDescription();
                    if(title==null){
                        title = "";
                    }
                    if(company==null){
                        company = "";
                    }
                    if(description==null){
                        description = "";
                    }
                    String word = title  + " " + company + " " + description;
                    String[] words = word.split("\\s+");
                    totalWords[0] += words.length;
                    sectionCvDtos.add(new SectionCvDto(SectionEvaluate.experience, experienceId, title, location, duration));
                    sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.experience, experienceId));
                    Experience e = experienceRepository.findById(x.getId().intValue()).get();
                    modelMapper.map(e, x);
                }
            });

            cvBodyDto.getProjects().forEach(x -> {
                if(x.getIsDisplay()){
                    total.addAndGet(1);
                    int projectId = x.getId();
                    String title = x.getTitle();
                    String duration = x.getDuration();
                    String organization = x.getOrganization();
                    String description = x.getDescription();
                    if(title==null){
                        title = "";
                    }
                    if(organization==null){
                        organization = "";
                    }
                    if(description==null){
                        description = "";
                    }
                    String word = title + " " + organization + " " + description;
                    String[] words = word.split("\\s+");
                    totalWords[0] += words.length;
                    sectionCvDtos.add(new SectionCvDto(SectionEvaluate.project, projectId, title, null, duration));
                    sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.project, projectId));
                    Project e = projectRepository.findById(x.getId().intValue()).get();
                    modelMapper.map(e, x);
                }
            });

            cvBodyDto.getInvolvements().forEach(x -> {
                if(x.getIsDisplay()){
                    total.addAndGet(1);
                    int involvementId = x.getId();
                    String duration = x.getDuration();
                    String title = x.getOrganizationRole();
                    String name = x.getOrganizationName();
                    String college = x.getCollege();
                    String description = x.getDescription();
                    if(title==null){
                        title = "";
                    }
                    if(name==null){
                        name = "";
                    }
                    if(college==null){
                        college = "";
                    }
                    if(description==null){
                        description = "";
                    }
                    String word = title + " " + name + " " + college + " " + description;
                    String[] words = word.split("\\s+");
                    totalWords[0] += words.length;
                    sectionCvDtos.add(new SectionCvDto(SectionEvaluate.involvement, involvementId, title, null, duration));
                    sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.involvement, involvementId));
                    Involvement e = involvementRepository.findById(x.getId().intValue()).get();
                    modelMapper.map(e, x);
                }
            });
            if(cv.getOverview()!=null){
                Score score = scoreOptional.get();
                return cv.deserializeOverview();

            }else{
                Score score = new Score();
                score.setCv(cv);
                Score savedScore = scoreRepository.save(score);

                Integer savedScoreId = savedScore.getId();
                for (SectionAddScoreLogDto sectionLog1: sectionAddScoreLogDtos){
                    List<SectionLog> sectionLog = sectionLogRepository.findBySection_TypeNameAndSection_TypeId(sectionLog1.getTypeName(), sectionLog1.getTypeId());
                    for(SectionLog sectionLog2: sectionLog){
                        ScoreLog scoreLog = new ScoreLog();
                        scoreLog.setSectionLog(sectionLog2);
                        scoreLog.setScore(savedScore);
                        scoreLogRepository.save(scoreLog);
                    }
                }

                contentList = evaluateContentSections(evaluates, sectionCvDtos, total.get());
                double contentScore = 0;
                int contentMax = 0;
                for(ContentDto contentS:contentList){
                    contentScore += contentS.getScore();
                    contentMax += contentS.getMax();
                }
                int contentTotal = (int) Math.ceil(( contentScore / contentMax) * 100);
                ScoreMaxMinDto contentS = new ScoreMaxMinDto( contentTotal,100,(int)contentScore + "/" + contentMax);


                practiceList = evaluateBestPractices(evaluates, sectionCvDtos, userId, cvId, totalWords, cv, totalExperiences.get(), total.get());
                double practiceScore = 0;
                int practiceMax = 0;
                for(ContentDto practiceS:practiceList){
                    practiceScore += practiceS.getScore();
                    practiceMax += practiceS.getMax();
                }
                int practiceTotal = (int) Math.ceil(( practiceScore / practiceMax) * 100);
                ScoreMaxMinDto practiceS = new ScoreMaxMinDto(practiceTotal,100,(int)practiceScore + "/" + practiceMax);

                optimizationList = evaluateOptimational(evaluates, cvId);
                double optimizationScore = 0;
                int optimizationMax = 0;
                for(ContentDto optimizationS:optimizationList){
                    optimizationScore += optimizationS.getScore();
                    optimizationMax += optimizationS.getMax();
                }
                int optimizationTotal = (int) Math.ceil(( optimizationScore / optimizationMax) * 100);
                ScoreMaxMinDto optimationS = new ScoreMaxMinDto(optimizationTotal,100,(int)optimizationScore + "/" + optimizationMax);

                formatList = evaluateFormat(evaluates, cvId);
                double formatScore = 0;
                int formatMax = 0;
                for(ContentDto formatListS:formatList){
                    formatScore += formatListS.getScore();
                    formatMax += formatListS.getMax();
                }
                int formatTotal = (int) Math.ceil(( formatScore / formatMax) * 100);
                ScoreMaxMinDto formatS = new ScoreMaxMinDto(formatTotal,100,(int)formatScore + "/" + formatMax);

                Score scoreOptional1 = scoreRepository.findById1(savedScoreId);
                scoreOptional1.setContent(contentTotal);
                scoreOptional1.setPractice(practiceTotal);
                scoreOptional1.setOptimization(optimizationTotal);
                scoreOptional1.setFormat(formatTotal);
                double totalPercentage = (contentTotal*0.5) + (practiceTotal*0.3) + (optimizationTotal*0.1) + (formatTotal*0.1);
                String resultLabel = getResultLabel(totalPercentage);
                scoreOptional1.setResult(resultLabel);
                scoreRepository.save(scoreOptional1);

                scoreDto = new ScoreDto(contentS,contentList,practiceS, practiceList,optimationS, optimizationList,formatS,formatList,resultLabel,(int)totalPercentage);
                cv.setOverview(cv.toOverviewBody(scoreDto));
                cvRepository.save(cv);

                return scoreDto;
            }
        }
        return scoreDto;
    }

    @Override
    public String updateScoreEvaluate(Integer adminId, Integer evaluateId, EvaluateScoreDto dto) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(adminId, RoleType.ADMIN);
        if(usersOptional.isPresent()){
            Optional<Evaluate> evaluateOptional = evaluateRepository.findById(evaluateId);
            if(evaluateOptional.isPresent()){
                Evaluate evaluate = evaluateOptional.get();
                Integer newScore = dto.getScore();
                if (newScore != null && newScore >= 1 && newScore <= 10) {
                    evaluate.setScore(newScore);
                } else {
                    throw new BadRequestException("Invalid score. Score must be in the range of 1 to 10.");
                }
                evaluateRepository.save(evaluate);
                return "Update score successful";
            }else {
                throw new BadRequestException("Evaluate not found.");
            }
        }else{
            throw new BadRequestException("Please login with role Admin.");
        }
    }

    @Override
    public String updateCriteriaEvaluate(Integer adminId, Integer evaluateId, EvaluateCriteriaDto dto) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(adminId, RoleType.ADMIN);
        if(usersOptional.isPresent()){
            Optional<Evaluate> evaluateOptional = evaluateRepository.findById(evaluateId);
            if(evaluateOptional.isPresent()){
                Evaluate evaluate = evaluateOptional.get();
                if (dto.getCriteria() == null) {
                    evaluate.setCritical(false);
                } else {
                    evaluate.setCritical(dto.getCriteria());
                }
                evaluateRepository.save(evaluate);
                return "Update criteria successful";
            }else {
                throw new BadRequestException("Evaluate not found.");
            }
        }else{
            throw new BadRequestException("Please login with role Admin.");
        }
    }

    @Override
    public List<EvaluateViewDto> viewEvaluate(Integer adminId) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(adminId, RoleType.ADMIN);
        if(usersOptional.isPresent()){
            List<EvaluateViewDto> evaluateViews = new ArrayList<>();
            List<Evaluate> evaluates = evaluateRepository.findAll();
            for(Evaluate evaluate: evaluates){
                EvaluateViewDto evaluateView = new EvaluateViewDto();
                evaluateView.setId(evaluate.getId());
                evaluateView.setCriteria(evaluate.getCritical());
                evaluateView.setDescription(evaluate.getDescription());
                evaluateView.setTitle(evaluate.getTitle());
                evaluateView.setScore(evaluate.getScore());
                evaluateViews.add(evaluateView);
            }
            return evaluateViews;
        }else{
            throw new BadRequestException("Please login with role Admin.");
        }
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

    private List<ContentDto> evaluateContentSections(List<Evaluate> evaluates, List<SectionCvDto> sectionCvDtos, int total) {
        List<ContentDto> contentList = new ArrayList<>();
        int evaluateId = 1;

        for (int i = 1; i <= 8; i++) {
            Evaluate evaluate = evaluates.get(i - 1);
            List<Section> sections = cvRepository.findSectionsWithNonPassStatus(evaluateId, SectionLogStatus.Pass);
            List<Section> sectionsPass = cvRepository.findSectionsWithPassStatus(evaluateId, SectionLogStatus.Pass);

            AnalyzeScoreDto sameSections = findSameSections(sectionCvDtos, sections, sectionsPass, total);

            if (!sameSections.getMoreInfos().isEmpty()) {
                ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(), Math.round((evaluate.getScore() * ((total - sameSections.getCount()) / (double) total)) * 100.0) / 100.0,evaluate.getScore(), sameSections);
                contentList.add(contentDto);
            }else{
                ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),(double) evaluate.getScore(),evaluate.getScore(), sameSections);
                contentList.add(contentDto);
            }

            evaluateId++;
        }

        return contentList;
    }

    private AnalyzeScoreDto findSameSections(List<SectionCvDto> sectionCvDtos, List<Section> sections, List<Section> sectionsPass, int total) {
        List<ContentDetailDto> sameSections = new ArrayList<>();
        int experiences = 0;
        int projects = 0;
        int involvements = 0;
        int count = 0;
        for (SectionCvDto sectionCvDto : sectionCvDtos) {
            for (Section section : sections) {
                if (section.getTypeName() == sectionCvDto.getTypeName() && section.getTypeId() == sectionCvDto.getTypeId()
                        && sectionCvDto.getTitle() != null) {
                    count +=1;
                    if(section.getTypeName()== SectionEvaluate.experience){
                        experiences += 1;
                    }else if(section.getTypeName()== SectionEvaluate.project){
                        projects += 1;
                    }else{
                        involvements += 1;
                    }
                    sameSections.add(new ContentDetailDto(sectionCvDto.getTypeName(), sectionCvDto.getTypeId(), sectionCvDto.getTitle()));
                }
            }
        }

        return new AnalyzeScoreDto(count,experiences,projects,involvements,total,sameSections);
    }


    private List<ContentDto> evaluateBestPractices(List<Evaluate> evaluates, List<SectionCvDto> sectionCvDtos, int userId, int cvId, int[] totalWords, Cv cv, int totalExperiences, int totalDate) {
        List<ContentDto> resultList = new ArrayList<>();

        for (int i = 8; i < 14; i++) {
            Evaluate evaluate = evaluates.get(i);

            switch (i) {
                case 8:
                    // Check location = null
                    int experiences = 0;
                    int count = 0;
                    List<ContentDetailDto> sameSectionsLocation = new ArrayList<>();
                    List<Section> sections = cvRepository.findAllByTypeName(SectionEvaluate.experience);
                    for (SectionCvDto sectionCvDto : sectionCvDtos) {
                        for (Section section : sections) {
                            if (section.getTypeName() == sectionCvDto.getTypeName() && section.getTypeId() == sectionCvDto.getTypeId()
                                    && section.getTypeName() == SectionEvaluate.experience
                                    && sectionCvDto.getTitle() != null) {
                                if(sectionCvDto.getLocation() == null){
                                    experiences += 1;
                                    count += 1;
                                    sameSectionsLocation.add(new ContentDetailDto(sectionCvDto.getTypeName(), sectionCvDto.getTypeId(), sectionCvDto.getTitle()));
                                }
                            }
                        }
                    }
                    if(count>=1){
                        AnalyzeScoreDto analyzeScoreLocation = new AnalyzeScoreDto(count,experiences,0,0,totalExperiences,sameSectionsLocation);
                        double sco = Math.round((evaluate.getScore() * ((double) (totalExperiences - analyzeScoreLocation.getCount()) / totalExperiences)) * 100.0) / 100.0;
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),sco,evaluate.getScore(), analyzeScoreLocation);
                        resultList.add(contentDto);
                    }else{
                        AnalyzeScoreDto analyzeScoreLocation = new AnalyzeScoreDto(count,experiences,0,0,totalExperiences,null);
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),(double)evaluate.getScore(),evaluate.getScore(), analyzeScoreLocation);
                        resultList.add(contentDto);
                    }
                    break;

                case 9:
                    // Check date = null
                    int experiencesDate = 0;
                    int projectsDate = 0;
                    int involvementDate = 0;
                    int countDate = 0;
                    List<ContentDetailDto> sameSectionsDate = new ArrayList<>();
                    List<Section> dateSections = cvRepository.findAllByTypeNames(Arrays.asList(SectionEvaluate.experience, SectionEvaluate.project, SectionEvaluate.involvement));
                    for (SectionCvDto sectionCvDto : sectionCvDtos) {
                        for (Section section : dateSections) {
                            if (section.getTypeName() == sectionCvDto.getTypeName() && section.getTypeId() == sectionCvDto.getTypeId()
                                    && (sectionCvDto.getTitle() != null)) {
                                if(sectionCvDto.getDuration() == null){
                                    countDate += 1;
                                    if(section.getTypeName() == SectionEvaluate.experience){
                                        experiencesDate += 1;
                                    }else if(section.getTypeName() == SectionEvaluate.project){
                                        projectsDate += 1;
                                    }else{
                                        involvementDate += 1;
                                    }
                                    sameSectionsDate.add(new ContentDetailDto(sectionCvDto.getTypeName(), sectionCvDto.getTypeId(), sectionCvDto.getTitle()));
                                }
                            }
                        }
                    }
                    if(countDate>=1){
                        AnalyzeScoreDto analyzeScoreDate = new AnalyzeScoreDto(countDate,experiencesDate,projectsDate,involvementDate,totalDate,sameSectionsDate);
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(), Math.round((evaluate.getScore() * ((totalDate - analyzeScoreDate.getCount()) / (double) totalDate)) * 100.0) / 100.0, evaluate.getScore(), analyzeScoreDate);
                        resultList.add(contentDto);
                    }else{
                        AnalyzeScoreDto analyzeScoreDate = new AnalyzeScoreDto(countDate,experiencesDate,projectsDate,involvementDate,totalDate,sameSectionsDate);
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(), (double) evaluate.getScore(), evaluate.getScore(), analyzeScoreDate);
                        resultList.add(contentDto);
                    }
                    break;

                case 10:
                    // Check phone = null
                    Optional<Users> users = usersRepository.findUsersById(userId);
                    if (users.isPresent() && users.get().getPhone() == null) {
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),0.0,evaluate.getScore(), null);
                        resultList.add(contentDto);
                        // Do something
                    }else{
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),(double)evaluate.getScore(),evaluate.getScore(), null);
                        resultList.add(contentDto);
                    }
                    break;

                case 11:
                    // Check linkin = null
                    Optional<Users> users1 = usersRepository.findUsersById(userId);
                    if (users1.isPresent() && users1.get().getLinkin() == null) {
                        // Do something
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),0.0,evaluate.getScore(), null);
                        resultList.add(contentDto);
                    }else{
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),(double)evaluate.getScore(),evaluate.getScore(), null);
                        resultList.add(contentDto);
                    }
                    break;

                case 12:
                    // Check count word
                    if (totalWords[0] < 300) {
                        String totalWordsString = Arrays.toString(totalWords);
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription() + totalWordsString,0.0,evaluate.getScore(), null);
                        resultList.add(contentDto);
                    }else{
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),(double)evaluate.getScore(),evaluate.getScore(), null);
                        resultList.add(contentDto);
                    }
                    break;

                case 13:
                    // Check summary
                    Cv cv1 = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
                    if (cv1.getSummary() == null || cv1.getSummary().length() < 30) {
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(),0.0,evaluate.getScore(), null);
                        resultList.add(contentDto);
                    }else{
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(), evaluate.getDescription(), (double)evaluate.getScore(),evaluate.getScore(), null);
                        resultList.add(contentDto);
                    }
                    break;

                default:
                    break;
            }
        }
        return resultList;
    }

    private List<ContentDto> evaluateOptimational(List<Evaluate> evaluates, int cvId) {
        List<ContentDto> contentDtoList = new ArrayList<>();

        for (int i = 14; i < 15; i++) {
            Evaluate evaluate = evaluates.get(i);

            // Check Ats = null
            Cv getCv = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
            JobDescription jobDescription = getCv.getJobDescription();
            if (jobDescription != null) {
                Integer jobDescriptionId = jobDescription.getId();
                List<Ats> ats = atsRepository.findAllByJobDescriptionId(jobDescriptionId);
                if (ats == null) {
                    contentDtoList.add(new ContentDto(evaluate.getCritical(), evaluate.getTitle(), evaluate.getDescription(),0.0,evaluate.getScore(), null));
                }else {
                    contentDtoList.add(new ContentDto(evaluate.getCritical(), evaluate.getTitle(), evaluate.getDescription(),(double)evaluate.getScore(),evaluate.getScore(), null));
                }
            }
        }

        return contentDtoList;
    }

    private List<ContentDto> evaluateFormat(List<Evaluate> evaluates, int cvId) throws JsonProcessingException {
        List<ContentDto> contentDtoList = new ArrayList<>();

        for (int i = 15; i < 17; i++) {
            Evaluate evaluate = evaluates.get(i);
            switch (i) {
                case 15:
                    // Check use template
                    ContentDto contentDto1 = new ContentDto(evaluate.getCritical(),evaluate.getTitle(),evaluate.getDescription(),(double) evaluate.getScore(),evaluate.getScore(),null);
                    contentDtoList.add(contentDto1);

                    break;
                case 16:
                    // Check font size
                    Cv getCv = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
                    CvBodyDto cvBodyDto = getCv.deserialize();
                    String font = cvBodyDto.getCvStyle().getFontSize();
                    Integer getFont = getFontSize(font);
                    if(!(getFont>8 && getFont<=11)){
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(),evaluate.getDescription(),0.0,evaluate.getScore(),null);
                        contentDtoList.add(contentDto);
                    }else{
                        ContentDto contentDto = new ContentDto(evaluate.getCritical(),evaluate.getTitle(),evaluate.getDescription(),(double) evaluate.getScore(),evaluate.getScore(),null);
                        contentDtoList.add(contentDto);
                    }
                    break;

                default:
                    break;
            }
        }

        return contentDtoList;
    }

    public Integer getFontSize(String font){
        // Define a pattern to match digits
        Pattern pattern = Pattern.compile("\\d+");

        // Create a matcher with the input font string
        Matcher matcher = pattern.matcher(font);

        // Check if there is a match
        if (matcher.find()) {
            // Extract the matched digits
            String numericPart = matcher.group();

            // Convert the numeric part to an integer

            // Now, fontSize contains the numeric part without "pt"
            return Integer.parseInt(numericPart);
        } else {
            return 0;
        }
    }

    private String getResultLabel(double percentage) {
        if (percentage < 50) {
            return "bad";
        } else if (percentage >= 50  && percentage <= 79) {
            return "improvement";
        } else {
            return "excellent";
        }
    }

}
