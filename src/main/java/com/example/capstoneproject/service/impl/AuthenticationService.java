package com.example.capstoneproject.service.impl;

//import com.example.capstoneproject.Dto.AuthenticationRequest;
//import com.example.capstoneproject.Dto.RegisterRequest;
//import com.example.capstoneproject.Dto.UserViewLoginDto;
//import com.example.capstoneproject.Dto.responses.AuthenticationResponse;
//import com.example.capstoneproject.entity.Role;
//import com.example.capstoneproject.entity.Users;
//import com.example.capstoneproject.enums.RoleType;
//import com.example.capstoneproject.exception.BadRequestException;
//import com.example.capstoneproject.filter.JwtService;
import com.example.capstoneproject.Dto.RoleDto;
import com.example.capstoneproject.Dto.UserViewLoginDto;
import com.example.capstoneproject.entity.Role;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.UsersMapper;
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
    private final UsersRepository repository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RoleRepository roleRepository;

    private Storage storage;

//    public AuthenticationResponse register(RegisterRequest request, String uid) {
//        UserViewLoginDto userViewLoginDto = new UserViewLoginDto();
//        Role role = roleRepository.findByRoleName(RoleType.CANDIDATE);
//        var customer = Users.builder()
//                .Name(request.getName())
//                .address(request.getAddress())
//                .phone((request.getPhone()))
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(role)//request.getRole()
//                .build();
//        var savedUser = repository.save(customer);
//        userViewLoginDto.setUid(uid);
//        userViewLoginDto.setId(savedUser.getId());
//        userViewLoginDto.setName(savedUser.getName());
//        userViewLoginDto.setAvatar(savedUser.getAvatar());
//        userViewLoginDto.setPhone(savedUser.getPhone());
//        userViewLoginDto.setPermissionWebsite(savedUser.getPersonalWebsite());
//        userViewLoginDto.setEmail(savedUser.getEmail());
//        userViewLoginDto.setLinkin(savedUser.getLinkin());
//        var jwtToken = jwtService.generateToken(customer);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .user(userViewLoginDto)
//                .build();
//    }

//    public AuthenticationResponse authenticate(AuthenticationRequest request, String uid) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//        var customer = repository.findByEmail(request.getEmail())
//                .orElseThrow();
//        UserViewLoginDto userViewLoginDto = new UserViewLoginDto();
//        userViewLoginDto.setUid(uid);
//        userViewLoginDto.setId(customer.getId());
//        userViewLoginDto.setName(customer.getName());
//        userViewLoginDto.setAvatar(customer.getAvatar());
//        userViewLoginDto.setPhone(customer.getPhone());
//        userViewLoginDto.setPermissionWebsite(customer.getPersonalWebsite());
//        userViewLoginDto.setEmail(customer.getEmail());
//        userViewLoginDto.setLinkin(customer.getLinkin());
//        var jwtToken = jwtService.generateToken(customer);
//        return AuthenticationResponse.builder()
//                .accessToken(jwtToken)
//                .user(userViewLoginDto)
//                .build();
//    }
//    public String changePassword(String uid, String newPassword, String reNewPassword) throws FirebaseAuthException {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        UserRecord userRecord = firebaseAuth.getUser(uid);
//        if(newPassword.equals(reNewPassword)){
//            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
//                    .setPassword(newPassword);
//
//            UserRecord updatedUser = firebaseAuth.updateUser(request);
//            return "Password changed successfully.";
//        }else{
//            return "Password and Re-Password incorrect.";
//        }
//    }
//    public Users forgetPassword(String email) {
//        try {
//            String str = FirebaseAuth.getInstance().generatePasswordResetLink(email);
//            sendEmail(email, "Review Request Created", "Your review request has been created successfully.\n" + str);
//        } catch (FirebaseAuthException e) {
//            e.printStackTrace();
//        }
//        Optional<Users> user = usersRepository.findByEmail(email);
//        return user.get();
//    }
//    public void sendEmail(String toEmail, String subject, String message) {
//        // Cấu hình thông tin SMTP
//        String host = "smtp.gmail.com";
//        String username = "cvbuilder.ai@gmail.com";
//        String password = "cvbtldosldixpkeh";
//
//        // Cấu hình các thuộc tính cho session
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.port", "587");
//        properties.put("mail.smtp.starttls.enable", "true");
//
//        // Tạo một phiên gửi email
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//            MimeMessage mimeMessage = new MimeMessage(session);
//
//            mimeMessage.setFrom(new InternetAddress(username));
//
//            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//
//            mimeMessage.setSubject(subject);
//
//            mimeMessage.setText(message);
//
//            Transport.send(mimeMessage);
//
//            System.out.println("Email sent successfully.");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            throw new BadRequestException("Failed to send email.");
//        }
//    }

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
                userViewLoginDto.setRole(modelMapper.map(users.getRole(), RoleDto.class));
                return  userViewLoginDto;
            }else{
                Users newUser = new Users();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setAvatar(image);
                usersRepository.save(newUser);
                userViewLoginDto.setId(newUser.getId());
                userViewLoginDto.setName(newUser.getName());
                userViewLoginDto.setAvatar(newUser.getAvatar());
                userViewLoginDto.setPhone(newUser.getPhone());
                userViewLoginDto.setPersonalWebsite(newUser.getPersonalWebsite());
                userViewLoginDto.setEmail(newUser.getEmail());
                userViewLoginDto.setLinkin(newUser.getLinkin());
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
