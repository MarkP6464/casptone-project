package com.example.capstoneproject.service.impl;
import com.example.capstoneproject.Dto.RoleDto;
import com.example.capstoneproject.Dto.UserViewLoginDto;
import com.example.capstoneproject.Dto.responses.AccountBalanceResponse;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.CandidateRepository;
import com.example.capstoneproject.repository.RoleRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.ExpertService;
import com.example.capstoneproject.service.HRService;
import com.example.capstoneproject.service.RoleService;
import com.google.cloud.storage.*;
import com.google.auth.oauth2.GoogleCredentials;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    HRService hrService;
    @Autowired
    RoleService roleService;

    @Autowired
    ExpertService expertService;

    private Storage storage;
    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");
            storage = StorageOptions.newBuilder().
                    setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())).
                    setProjectId("cvbuilder-dc116").build().getService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public UserViewLoginDto getInforUser(String email, String name, String image){
        Optional<Users> usersOptional = usersRepository.findByEmail(email);
        Role roleOptional = roleRepository.findByRoleName(RoleType.CANDIDATE);
        RoleDto roleDto = modelMapper.map(roleOptional, RoleDto.class);
        UserViewLoginDto userViewLoginDto = null;
        if (Objects.nonNull(roleOptional)){
            userViewLoginDto = new UserViewLoginDto();
            if(usersOptional.isPresent()){
                Users users = usersOptional.get();
                userViewLoginDto.setId(users.getId());
                userViewLoginDto.setName(users.getName());
                userViewLoginDto.setAvatar(users.getAvatar());
                userViewLoginDto.setPhone(users.getPhone());
                userViewLoginDto.setPersonalWebsite(users.getPersonalWebsite());
                userViewLoginDto.setEmail(users.getEmail());
                userViewLoginDto.setLinkin(users.getLinkin());
                userViewLoginDto.setAccountBalance(users.getAccountBalance());
                userViewLoginDto.setBanned(users.isBan());
                if(users.getRole().getRoleName().equals(RoleType.HR)){
                    Users userHr = usersOptional.get();
                    if (userHr instanceof HR){
                        HR hr = (HR) userHr;
                        if (Objects.nonNull(hr)){
                            userViewLoginDto.setSubscription(hr.getSubscription());
                            userViewLoginDto.setVip(hr.getVip());
                        }
                    }
                }
                userViewLoginDto.setRole(modelMapper.map(users.getRole(), RoleDto.class));
                LocalDate currentDate = LocalDate.now();
                users.setLastActive(currentDate);
                usersRepository.save(users);
                return  userViewLoginDto;
            }else{
                Candidate candidate = new Candidate();
                candidate.setPublish(false);
                candidate.setEmail(email);
                candidate.setName(name);
                candidate.setAvatar(image);
                candidate.setStatus(BasicStatus.ACTIVE);
                candidate.setRole(roleOptional);
                candidate.setAccountBalance( 0.0);
                LocalDate currentDate = LocalDate.now();
                candidate.setLastActive(currentDate);
                candidate.setCreateDate(currentDate);
                candidate.setBan(false);
                candidateRepository.save(candidate);
                userViewLoginDto.setId(candidate.getId());
                userViewLoginDto.setName(candidate.getName());
                userViewLoginDto.setAvatar(candidate.getAvatar());
                userViewLoginDto.setPhone(candidate.getPhone());
                userViewLoginDto.setPersonalWebsite(candidate.getPersonalWebsite());
                userViewLoginDto.setEmail(candidate.getEmail());
                userViewLoginDto.setLinkin(candidate.getLinkin());
                userViewLoginDto.setAccountBalance(candidate.getAccountBalance());
                userViewLoginDto.setRole(roleDto);
                userViewLoginDto.setBanned(candidate.isBan());
            }
        }
        return userViewLoginDto;
    }
    public UserViewLoginDto getInforUserV2(String email, String name, String image, String roleName){
        Optional<Users> usersOptional = usersRepository.findByEmail(email);
        Role roleCandidateOptional = roleRepository.findByRoleName(RoleType.CANDIDATE);
        RoleDto roleCandidateDto = modelMapper.map(roleCandidateOptional, RoleDto.class);
        Role roleExpertOptional = roleRepository.findByRoleName(RoleType.EXPERT);
        RoleDto roleExpertDto = modelMapper.map(roleExpertOptional, RoleDto.class);
        Role roleHrOptional = roleRepository.findByRoleName(RoleType.HR);
        RoleDto roleHrDto = modelMapper.map(roleHrOptional, RoleDto.class);
        UserViewLoginDto userViewLoginDto = null;
        userViewLoginDto = new UserViewLoginDto();
        if(usersOptional.isPresent()){
            Users users = usersOptional.get();
            userViewLoginDto.setId(users.getId());
            userViewLoginDto.setName(users.getName());
            userViewLoginDto.setAvatar(users.getAvatar());
            userViewLoginDto.setPhone(users.getPhone());
            userViewLoginDto.setPersonalWebsite(users.getPersonalWebsite());
            userViewLoginDto.setEmail(users.getEmail());
            userViewLoginDto.setLinkin(users.getLinkin());
            userViewLoginDto.setAccountBalance(users.getAccountBalance());
            userViewLoginDto.setBanned(users.isBan());
            if(users.getRole().getRoleName().equals(RoleType.HR)){
                Users userHr = usersOptional.get();
                if (userHr instanceof HR){
                    HR hr = (HR) userHr;
                    if (Objects.nonNull(hr)){
                        userViewLoginDto.setSubscription(hr.getSubscription());
                        userViewLoginDto.setVip(hr.getVip());
                    }
                }
            }
            userViewLoginDto.setRole(modelMapper.map(users.getRole(), RoleDto.class));
            LocalDate currentDate = LocalDate.now();
            users.setLastActive(currentDate);
            usersRepository.save(users);
            return userViewLoginDto;
        }
        else if (roleName.equals(RoleType.CANDIDATE.toString())){
                Candidate candidate = new Candidate();
                candidate.setPublish(false);
                candidate.setEmail(email);
                candidate.setName(name);
                candidate.setAvatar(image);
                candidate.setStatus(BasicStatus.ACTIVE);
                candidate.setRole(roleCandidateOptional);
                candidate.setAccountBalance( 0.0);
                LocalDate currentDate = LocalDate.now();
                candidate.setLastActive(currentDate);
                candidate.setCreateDate(currentDate);
                candidate.setBan(false);
                candidateRepository.save(candidate);
                userViewLoginDto.setId(candidate.getId());
                userViewLoginDto.setName(candidate.getName());
                userViewLoginDto.setAvatar(candidate.getAvatar());
                userViewLoginDto.setPhone(candidate.getPhone());
                userViewLoginDto.setPersonalWebsite(candidate.getPersonalWebsite());
                userViewLoginDto.setEmail(candidate.getEmail());
                userViewLoginDto.setLinkin(candidate.getLinkin());
                userViewLoginDto.setAccountBalance(candidate.getAccountBalance());
                userViewLoginDto.setRole(roleCandidateDto);
                userViewLoginDto.setBanned(candidate.isBan());
            }
        else if (roleName.equals(RoleType.HR.toString())){
            HR hr = new HR();
            hr.setName(name);
            hr.setEmail(email);
            hr.setAvatar(image);
            hr.setRole(roleHrOptional);
            LocalDate localDate = LocalDate.now();
            hr.setCreateDate(localDate);
            hr.setSubscription(false);
            hr.setCompanyName("ABC Company");
            hr.setCompanyLocation("ABC Location");
            hr.setCompanyDescription("ABC Description");
            hr.setCompanyLogo(image);
            hr.setBan(false);
            hr.setVip(false);
            HR e = hrService.create(hr);
            userViewLoginDto.setId(e.getId());
            userViewLoginDto.setName(e.getName());
            userViewLoginDto.setAvatar(e.getAvatar());
            userViewLoginDto.setPhone(e.getPhone());
            userViewLoginDto.setPersonalWebsite(e.getPersonalWebsite());
            userViewLoginDto.setEmail(e.getEmail());
            userViewLoginDto.setLinkin(e.getLinkin());
            userViewLoginDto.setAccountBalance(e.getAccountBalance());
            userViewLoginDto.setRole(roleHrDto);
            userViewLoginDto.setBanned(e.isBan());
            userViewLoginDto.setSubscription(e.getSubscription());
            userViewLoginDto.setVip(e.getVip());
        }
        else if (roleName.equals(RoleType.EXPERT.toString())){
                    Expert expert = new Expert();
                    expert.setName(name);
                    expert.setEmail(email);
                    expert.setPrice(0.0);
                    expert.setAvatar(image);
                    expert.setPunish(false);
                    expert.setRole(roleExpertOptional);
                    LocalDate localDate = LocalDate.now();
                    expert.setCreateDate(localDate);
                    Expert e = expertService.create(expert);
                    userViewLoginDto.setId(e.getId());
                    userViewLoginDto.setName(e.getName());
                    userViewLoginDto.setAvatar(e.getAvatar());
                    userViewLoginDto.setPhone(e.getPhone());
                    userViewLoginDto.setPersonalWebsite(e.getPersonalWebsite());
                    userViewLoginDto.setEmail(e.getEmail());
                    userViewLoginDto.setLinkin(e.getLinkin());
                    userViewLoginDto.setAccountBalance(e.getAccountBalance());
                    userViewLoginDto.setRole(roleExpertDto);
                    userViewLoginDto.setBanned(e.isBan());
                }
        else if (roleName.equals(RoleType.ADMIN.toString())){
            throw new BadRequestException("You are have permission to create admin account");
        }
        return userViewLoginDto;
    }
    public RoleType getRole(String email, String name, String image){
        Optional<Users> usersOptional = usersRepository.findByEmail(email);
        if (usersOptional.isPresent()){
            return usersOptional.get().getRole().getRoleName();
        }else{
            return null;
        }
    }

    public Boolean checkIsExistUser(String email){
        Optional<Users> usersOptional = usersRepository.findByEmail(email);
        if (usersOptional.isPresent()){
            return true;
        } else return false;
    }

    public AccountBalanceResponse getInforUserBalance(String email){
        Optional<Users> usersOptional = usersRepository.findByEmail(email);
        Role roleOptional = roleRepository.findByRoleName(RoleType.CANDIDATE);
        AccountBalanceResponse userViewLoginDto = null;
        if (Objects.nonNull(roleOptional)){
            userViewLoginDto = new AccountBalanceResponse();
            if(usersOptional.isPresent()){
                Users users = usersOptional.get();
                userViewLoginDto.setId(users.getId());
                userViewLoginDto.setAccountBalance(users.getAccountBalance());
                LocalDate currentDate = LocalDate.now();
                users.setLastActive(currentDate);
                usersRepository.save(users);
                return  userViewLoginDto;
            }
        }
        return userViewLoginDto;
    }

    public String saveTest(MultipartFile file) throws IOException {
        String imageName = generateFileName(file.getOriginalFilename());
        Map<String, String> map = new HashMap<>();
        map.put("firebaseStorageDownloadTokens", imageName);
        BlobId blobId = BlobId.of("cvbuilder-dc116.appspot.com", imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setMetadata(map)
                .setContentType(file.getContentType())
                .build();
        Blob blob = storage.create(blobInfo, file.getInputStream());
        String fileUrl = "https://firebasestorage.googleapis.com/v0/b/cvbuilder-dc116.appspot.com/o/"+ URLEncoder.encode(imageName, "UTF-8") + "?alt=media&token=" + imageName;
        System.out.println();
        return fileUrl;
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "." + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

}
