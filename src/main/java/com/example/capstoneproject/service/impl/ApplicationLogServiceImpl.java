package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.ApplicationLogStatus;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ApplicationLogService;
import com.example.capstoneproject.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.rpc.NotFoundException;
import io.swagger.models.auth.In;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationLogServiceImpl implements ApplicationLogService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    ApplicationLogRepository applicationLogRepository;

    @Autowired
    HistoryCoverLetterRepository historyCoverLetterRepository;

    @Autowired
    HistoryService historyService;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    CoverLetterRepository coverLetterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean applyCvToPost(Integer userId, Integer cvId, Integer coverLetterId, Integer postingId, NoteDto dto) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId,cvId);
        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(postingId, BasicStatus.ACTIVE, StatusReview.Published);
        ApplicationLog applicationLog = new ApplicationLog();
        LocalDate currentDate = LocalDate.now();


        Optional<Users> usersOptional = usersRepository.findUsersById(userId);
        if (usersOptional.isPresent()){
            Users user = usersOptional.get();
            applicationLog.setUser(user);
        }else throw new InternalServerException("Not found user");
        if(cvOptional.isPresent()) {
            Cv cv = cvOptional.get();
            HistoryViewDto hisCvId = historyService.create(userId, cvId);
            applicationLog.setCv(hisCvId.getId());
        }else throw new InternalServerException("Not found cv");
        Cv cv = cvOptional.get();
        Optional<CoverLetter> coverLetterOptional = coverLetterRepository.findByCv_User_IdAndId(userId, coverLetterId);
        if(coverLetterOptional.isPresent()){
            CoverLetter coverLetter = coverLetterOptional.get();

            //save cover letter history
            HistoryCoverLetter historyCoverLetter = new HistoryCoverLetter();
            historyCoverLetter.setTitle(coverLetter.getTitle());
            historyCoverLetter.setDear(coverLetter.getDear());
            historyCoverLetter.setDate(coverLetter.getDate());
            historyCoverLetter.setCompany(coverLetter.getCompany());
            historyCoverLetter.setDescription(coverLetter.getDescription());
            historyCoverLetter.setCoverLetter(coverLetter);
            historyCoverLetter = historyCoverLetterRepository.save(historyCoverLetter);

            applicationLog.setCoverLetter(historyCoverLetter.getId());
        }
        applicationLog.setNote(dto.getNote());
        applicationLog.setTimestamp(LocalDate.now());

        if (jobPostingOptional.isPresent()){
            JobPosting jobPosting = jobPostingOptional.get();
            List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByUser_IdAndJobPosting_IdOrderByTimestampDesc(userId,postingId);
            applicationLog.setJobPosting(jobPosting);
            if (!applicationLogs.isEmpty()){
                ApplicationLog applicationLogCheck = applicationLogs.get(0);
                LocalDate countDate = applicationLogCheck.getTimestamp();
                Integer condition = jobPosting.getApplyAgain();
                LocalDate resultDate = countDate.plusDays(condition);
                if(resultDate.isBefore(currentDate) || resultDate.isEqual(currentDate)){
                    applicationLog.setJobPosting(jobPosting);
                }else{
                    throw new BadRequestException("Please apply after date " + resultDate);
                }
            }
            applicationLog.setStatus(ApplicationLogStatus.RECEIVED);
            applicationLog.setNote(dto.getNote());
            applicationLogRepository.save(applicationLog);
//            sendEmail(cv.getUser().getEmail(), "Review Request Created", "Your review request has been created successfully.");
            return true;
        } else throw new InternalServerException("Not found Job posting");
    }

    public static void sendEmail(String toEmail, String subject, String message) {
        // Cấu hình thông tin SMTP
        String host = "smtp.gmail.com";
        String username = "acc3cuaminh@gmail.com";
        String password = "jwkspmuznvxbikcx";

        // Cấu hình các thuộc tính cho session
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");

        // Tạo một phiên gửi email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(username));

            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            mimeMessage.setSubject(subject);

            mimeMessage.setText(message);

            Transport.send(mimeMessage);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email.");
        }
    }

    @Override
    public List<ApplicationLogJobResponse> getAll(Integer postId){
        List<ApplicationLogJobResponse> newList = null;
        List<ApplicationLog> list = applicationLogRepository.findAllByJobPosting_IdOrderByTimestampDesc(postId);

        HashMap<Integer, String> listCvMap = new HashMap<>();
        HashMap<Integer, String> listClMap = new HashMap<>();
        List<Integer> cvList = new ArrayList<>();
        List<Integer> clList = new ArrayList<>();
        if (!list.isEmpty()){
            list.stream().map(x -> {
                return listCvMap.put(x.getCv(), null);
            });
            list.stream().map(x -> {
                return listClMap.put(x.getCoverLetter(), null);
            });

            newList = list.stream().map(x -> {
                cvList.add(x.getCv());
                if (Objects.nonNull(x.getCoverLetter())){
                    clList.add(x.getCoverLetter());
                }
                ApplicationLogJobResponse applicationLogResponse = new ApplicationLogJobResponse();
                applicationLogResponse.setApplyDate(x.getTimestamp());
                applicationLogResponse.setNote(x.getNote());
                applicationLogResponse.setEmail(x.getUser().getEmail());
                applicationLogResponse.setCandidateName(x.getUser().getName());
                applicationLogResponse.setStatus(x.getStatus());
                JobPostingNameViewDto jobPostingNameView = new JobPostingNameViewDto();
                jobPostingNameView.setId(x.getJobPosting().getId());
                jobPostingNameView.setName(x.getJobPosting().getTitle());
                applicationLogResponse.setJobPosting(jobPostingNameView);
                applicationLogResponse.getCvs().put("historyId", x.getCv());
                applicationLogResponse.getCvs().put("resumeName", null);
                applicationLogResponse.getCoverLetters().put("historyCoverLetterId", x.getCoverLetter());
                applicationLogResponse.getCoverLetters().put("title", null);
                return applicationLogResponse;
            }).collect(Collectors.toList());

            List<History> cvHistoryList = historyRepository.findAllByIdIn(cvList);
            cvHistoryList.stream().forEach(x -> {
                listCvMap.put(x.getId(), x.getCv().getResumeName());
            });
            newList.stream().forEach(x -> {
                Integer historyID = (Integer) x.getCvs().get("historyId");
                x.getCvs().put("resumeName", listCvMap.get(historyID));
            });

            List<HistoryCoverLetter> clHistoryList = historyCoverLetterRepository.findAllByIdIn(clList);
            clHistoryList.stream().forEach(x -> {
                listClMap.put(x.getId(), x.getCoverLetter().getTitle());
            });
            newList.stream().forEach(x -> {
                Integer historyCoverLetterId = (Integer) x.getCoverLetters().get("historyCoverLetterId");
                x.getCoverLetters().put("title", listClMap.get(historyCoverLetterId));
            });
        }
        return newList;


//        if (!list.isEmpty()){
//            for(ApplicationLog applicationLog: list){
//                ApplicationLogJobResponse applicationLogResponse = new ApplicationLogJobResponse();
//                applicationLogResponse.setJobTitle(applicationLogResponse.getJobTitle());
//                applicationLogResponse.setCandidateName(applicationLogResponse.getCandidateName());
//                applicationLogResponse.setApplyDate(applicationLogResponse.getApplyDate());
//                applicationLogResponse.setNote(applicationLogResponse.getNote());
//                applicationLogResponse.setEmail(applicationLogResponse.getEmail());
//            }
//        }
//        return newList;
    }

    @Override
    public List<ApplicationLogFullResponse> getAllByHrID(Integer hrId){
        List<ApplicationLogFullResponse> newList = Collections.EMPTY_LIST;
        List<ApplicationLog> list = applicationLogRepository.findAllByJobPosting_User_IdOrderByTimestamp(hrId);
        HashMap<Integer, String> listCvMap = new HashMap<>();
        HashMap<Integer, String> listClMap = new HashMap<>();
        List<Integer> cvList = new ArrayList<>();
        List<Integer> clList = new ArrayList<>();
        if (!list.isEmpty()){
            list.stream().map(x -> {
                return listCvMap.put(x.getCv(), null);
            });
            list.stream().map(x -> {
                return listClMap.put(x.getCoverLetter(), null);
            });

            newList = list.stream().map(x -> {
                cvList.add(x.getCv());
                if (Objects.nonNull(x.getCoverLetter())){
                    clList.add(x.getCoverLetter());
                }
                ApplicationLogFullResponse applicationLogResponse = new ApplicationLogFullResponse();
                applicationLogResponse.setEmail(x.getUser().getEmail());
                JobPostingNameViewDto jobPosting = new JobPostingNameViewDto();
                jobPosting.setId(x.getJobPosting().getId());
                jobPosting.setName(x.getJobPosting().getTitle());
                applicationLogResponse.setJobPosting(jobPosting);
                applicationLogResponse.setCandidateName(x.getUser().getName());
                applicationLogResponse.setApplyDate(x.getTimestamp());
                applicationLogResponse.setNote(x.getNote());
                applicationLogResponse.setStatus(x.getStatus());
                applicationLogResponse.getCvs().put("historyId", x.getCv());
                applicationLogResponse.getCvs().put("resumeName", null);
                applicationLogResponse.getCoverLetters().put("historyCoverLetterId", x.getCoverLetter());
                applicationLogResponse.getCoverLetters().put("title", null);
                return applicationLogResponse;
            }).collect(Collectors.toList());

            List<History> cvHistoryList = historyRepository.findAllByIdIn(cvList);
            cvHistoryList.stream().forEach(x -> {
                listCvMap.put(x.getId(), x.getCv().getResumeName());
            });
            newList.stream().forEach(x -> {
                Integer historyID = (Integer) x.getCvs().get("historyId");
                x.getCvs().put("resumeName", listCvMap.get(historyID));
            });

            List<HistoryCoverLetter> clHistoryList = historyCoverLetterRepository.findAllByIdIn(clList);
            clHistoryList.stream().forEach(x -> {
                listClMap.put(x.getId(), x.getCoverLetter().getTitle());
            });
            newList.stream().forEach(x -> {
                Integer historyCoverLetterId = (Integer) x.getCoverLetters().get("historyCoverLetterId");
                x.getCoverLetters().put("title", listClMap.get(historyCoverLetterId));
            });
        }
        return newList;
    }

    @Override
    public List<ApplicationLogCandidateResponse> getAllByCandidateId(Integer id){
        List<ApplicationLogCandidateResponse> newList = Collections.EMPTY_LIST;
        List<ApplicationLog> list = applicationLogRepository.findAllByUser_IdOrderByTimestamp(id);
        HashMap<Integer, String> listCvMap = new HashMap<>();
        HashMap<Integer, String> listClMap = new HashMap<>();
        List<Integer> cvList = new ArrayList<>();
        List<Integer> clList = new ArrayList<>();
        if (!list.isEmpty()){
            list.stream().map(x -> {
                return listCvMap.put(x.getCv(), null);
            });
            list.stream().map(x -> {
                return listClMap.put(x.getCoverLetter(), null);
            });

            newList = list.stream().map(x -> {
                cvList.add(x.getCv());
                if (Objects.nonNull(x.getCoverLetter())){
                    clList.add(x.getCoverLetter());
                }
                ApplicationLogCandidateResponse applicationLogResponse = new ApplicationLogCandidateResponse();
                applicationLogResponse.setApplyDate(x.getTimestamp());
                applicationLogResponse.setNote(x.getNote());
                applicationLogResponse.setStatus(x.getStatus());
                applicationLogResponse.setCandidateName(x.getUser().getName());
                JobPostingNameViewDto jobPostingNameView = new JobPostingNameViewDto();
                jobPostingNameView.setId(x.getJobPosting().getId());
                jobPostingNameView.setName(x.getJobPosting().getTitle());
                applicationLogResponse.setJobPosting(jobPostingNameView);
                applicationLogResponse.setCompany(x.getJobPosting().getCompanyName());
                applicationLogResponse.getCvs().put("historyId", x.getCv());
                applicationLogResponse.getCvs().put("resumeName", null);
                applicationLogResponse.getCoverLetters().put("historyCoverLetterId", x.getCoverLetter());
                applicationLogResponse.getCoverLetters().put("title", null);
                return applicationLogResponse;
            }).collect(Collectors.toList());

            List<History> cvHistoryList = historyRepository.findAllByIdIn(cvList);
            cvHistoryList.stream().forEach(x -> {
                listCvMap.put(x.getId(), x.getCv().getResumeName());
            });
            newList.stream().forEach(x -> {
                Integer historyID = (Integer) x.getCvs().get("historyId");
                x.getCvs().put("resumeName", listCvMap.get(historyID));
            });

            List<HistoryCoverLetter> clHistoryList = historyCoverLetterRepository.findAllByIdIn(clList);
            clHistoryList.stream().forEach(x -> {
                listClMap.put(x.getId(), x.getTitle());
            });
            newList.stream().forEach(x -> {
                Integer historyCoverLetterId = (Integer) x.getCoverLetters().get("historyCoverLetterId");
                x.getCoverLetters().put("title", listClMap.get(historyCoverLetterId));
            });
        }
        return newList;
    }

    @Override
    public ApplicationLogResponse updateDownloaded(Integer id)  {
        ApplicationLogResponse response;
        Optional<ApplicationLog> optionalApplicationLog = applicationLogRepository.findById(id);
        if (optionalApplicationLog.isPresent()){
            ApplicationLog entity = optionalApplicationLog.get();
            entity.setStatus(ApplicationLogStatus.DOWNLOADED);
            entity = applicationLogRepository.save(entity);
            response = modelMapper.map(entity, ApplicationLogResponse.class);
        } else throw new ResourceNotFoundException("Not found the log by id");
        return response;
    }


    @Override
    public ApplicationLogResponse updateSeen(Integer id)  {
        ApplicationLogResponse response;
        Optional<ApplicationLog> optionalApplicationLog = applicationLogRepository.findById(id);
        if (optionalApplicationLog.isPresent()){
            ApplicationLog entity = optionalApplicationLog.get();
            entity.setStatus(ApplicationLogStatus.SEEN);
            entity = applicationLogRepository.save(entity);
            response = modelMapper.map(entity, ApplicationLogResponse.class);
        } else throw new ResourceNotFoundException("Not found the log by id");
        return response;
    }
}
