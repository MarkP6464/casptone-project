package com.example.capstoneproject.controller;
import com.example.capstoneproject.Dto.UserViewLoginDto;
import com.example.capstoneproject.exception.Message;
import com.example.capstoneproject.service.impl.AuthenticationService;
import com.example.capstoneproject.service.impl.MessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/public")
    public Message getPublic() {
        return messageService.getPublicMessage();
    }

    @ApiOperation(value = "Upload a file", response = ResponseEntity.class)
    @PostMapping(value = "/public/upload/image", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFileImage(
            @RequestPart("file") MultipartFile file) {
        try {
            String fileName = authenticationService.saveTest(file);
            return ResponseEntity.ok(fileName);
        } catch (Exception e) {
            //  throw internal error;
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/protected")
    public ResponseEntity<?> getUserEmail(Principal principal) {
        if (principal instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwt = (JwtAuthenticationToken) principal;
            String email = jwt.getToken().getClaimAsString("email");
            JSONObject userInfoJson = jwt.getToken().getClaim("user_info");
            String givenName = userInfoJson.get("given_name").toString();
            String picture = userInfoJson.get("picture").toString();

            return ResponseEntity.ok(authenticationService.getInforUser(email,givenName,picture));
        }else{
            return ResponseEntity.ok("Token invalid");
        }
    }

    @GetMapping("/protected/me")
    public ResponseEntity<?> getUserEmailBalance(Principal principal) {
        if (principal instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwt = (JwtAuthenticationToken) principal;
            String email = jwt.getToken().getClaimAsString("email");
            return ResponseEntity.ok(authenticationService.getInforUserBalance(email));
        }else{
            return ResponseEntity.ok("Token invalid");
        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public Message getAdmin() {
        return messageService.getAdminMessage();
    }


}
