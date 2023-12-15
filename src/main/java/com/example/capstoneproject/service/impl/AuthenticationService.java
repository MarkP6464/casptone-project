package com.example.capstoneproject.service.impl;
import com.example.capstoneproject.Dto.RoleDto;
import com.example.capstoneproject.Dto.UserViewLoginDto;
import com.example.capstoneproject.entity.Candidate;
import com.example.capstoneproject.entity.Role;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.CandidateRepository;
import com.example.capstoneproject.repository.RoleRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.google.cloud.storage.*;
import com.google.auth.oauth2.GoogleCredentials;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
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
                userViewLoginDto.setRole(modelMapper.map(users.getRole(), RoleDto.class));
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
