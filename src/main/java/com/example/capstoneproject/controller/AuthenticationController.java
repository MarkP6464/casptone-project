package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.AuthenticationRequest;
import com.example.capstoneproject.Dto.RegisterRequest;
import com.example.capstoneproject.Dto.responses.AuthenticationResponse;
import com.example.capstoneproject.entity.Role;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.repository.RoleRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.impl.AuthenticationService;
import com.example.capstoneproject.Config.FirebaseConfig;
import com.example.capstoneproject.service.impl.FirebaseServiceImpl;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @Autowired
    private FirebaseServiceImpl firebaseService;
    @Autowired
    UsersRepository userRepository;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/firebase")
    public ResponseEntity<AuthenticationResponse> authorizeToken(@RequestBody String token) {
        try {
            Role role = roleRepository.findByRoleName(RoleType.CANDIDATE);

            // Xác thực token
            FirebaseToken decodedToken = firebaseService.verifyToken(token);

            // Lấy thông tin từ token (ví dụ: uid của người dùng)
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String picture = decodedToken.getPicture();
            Optional<Users> usersOptional = userRepository.findByEmail(email);
            if(usersOptional.isPresent()){
                Users users = usersOptional.get();
                AuthenticationRequest request = new AuthenticationRequest();
                request.setEmail(users.getEmail());
                request.setPassword(" ");
                return ResponseEntity.ok(authenticationService.authenticate(request,uid));
                //authenticationService.authenticate();
            }else{
                RegisterRequest request = new RegisterRequest();
                request.setName("");
                request.setAddress("");
                request.setPhone("");
                request.setEmail(email);
                request.setPassword(" ");
                request.setRole(role);
                return ResponseEntity.ok(authenticationService.register(request, uid));
            }
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse());
        }

    }

    @PostMapping("/firebase/forgot-password")
    public ResponseEntity<?> firebaseForget(@RequestParam String email) {
        if(authenticationService.forgetPassword(email)!=null){
            return ResponseEntity.ok("Please check your email.");
        }
        return ResponseEntity.ok("Email incorrect. Please check again.");
    }

    @PutMapping("/firebase/change-password")
    public ResponseEntity<String> changePassword(@RequestParam("uid") String uid, @RequestParam("password") String password, @RequestParam("rePassword") String rePassword) {
        try {
            return ResponseEntity.ok(authenticationService.changePassword(uid, password, rePassword));
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password");
        }
    }

    @GetMapping("/hihi")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getByBranch() {
        return "hello user";
    }

    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping(value = "/upload/image", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file) {
        try {
            String fileName = authenticationService.saveTest(file);
            return ResponseEntity.ok(fileName);
        } catch (Exception e) {
            //  throw internal error;
        }
        return ResponseEntity.ok().build();
    }

}
