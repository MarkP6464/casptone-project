package com.example.capstoneproject.controller;
import com.example.capstoneproject.Dto.UserViewLoginDto;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.Message;
import com.example.capstoneproject.service.ExpertService;
import com.example.capstoneproject.service.HRService;
import com.example.capstoneproject.service.RoleService;
import com.example.capstoneproject.service.UsersService;
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
import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UsersService usersService;

    @Autowired
    ExpertService expertService;

    @Autowired
    HRService hrService;

    @Autowired
    RoleService roleService;

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
            String portalNAme ="";
            JwtAuthenticationToken jwt = (JwtAuthenticationToken) principal;
            String email = jwt.getToken().getClaimAsString("email");
            JSONObject userInfoJson = jwt.getToken().getClaim("user_info");
            String givenName = userInfoJson.get("given_name").toString();
            String picture = userInfoJson.get("picture").toString();
            JSONObject roleObject = jwt.getToken().getClaim("event");
            Object roleName = roleObject.get("request");
            if(roleName instanceof JSONObject){
                JSONObject json = (JSONObject) roleName;
                Object query = json.get("query");
                if(query instanceof  JSONObject){
                    JSONObject metadata = (JSONObject) query;
                    portalNAme = (String) metadata.get("metadata");
                }
            }
                RoleType role = authenticationService.getRole(email,givenName,picture);
                if (Objects.isNull(role)){
                    return ResponseEntity.ok(authenticationService.getInforUserV2(email,givenName,picture, portalNAme));
                }else
                if(role.toString().equals(portalNAme)){
                    return ResponseEntity.ok(authenticationService.getInforUser(email,givenName,picture));
                }else{
                    throw new BadRequestException("You do not have permission to perform this operation.");
                }

        }else{
            return ResponseEntity.ok("Token invalid");
        }
    }

//    @GetMapping("/protected/admin")
//    public ResponseEntity<?> getAdminEmail(Principal principal) {
//        if (principal instanceof JwtAuthenticationToken) {
//            String role = "";
//            JwtAuthenticationToken jwt = (JwtAuthenticationToken) principal;
//            String email = jwt.getToken().getClaimAsString("email");
//            JSONObject userInfoJson = jwt.getToken().getClaim("user_info");
//            String givenName = userInfoJson.get("given_name").toString();
//            String picture = userInfoJson.get("picture").toString();
//            JSONObject roleObject = jwt.getToken().getClaim("event");
//            Object roleName = roleObject.get("request");
//            if(roleName instanceof JSONObject){
//                JSONObject json = (JSONObject) roleName;
//                Object query = json.get("query");
//                if(query instanceof  JSONObject){
//                    JSONObject metadata = (JSONObject) query;
//                    role = (String) metadata.get("metadata");
//                }
//            }
//            if(role.equals("ADMIN")){
//                return ResponseEntity.ok(authenticationService.getInforUser(email,givenName,picture));
//            }else{
//                throw new BadRequestException("You do not have permission to perform this operation.");
//            }
//        }else{
//            return ResponseEntity.ok("Token invalid");
//        }
//    }

//    @GetMapping("/protected/expert")
//    public ResponseEntity<?> getExpertEmail(Principal principal) {
//        if (principal instanceof JwtAuthenticationToken) {
//            String role = "";
//            JwtAuthenticationToken jwt = (JwtAuthenticationToken) principal;
//            JSONObject roleObject = jwt.getToken().getClaim("event");
//            String email = jwt.getToken().getClaimAsString("email");
//            JSONObject userInfoJson = jwt.getToken().getClaim("user_info");
//            String givenName = userInfoJson.get("given_name").toString();
//            String picture = userInfoJson.get("picture").toString();
//            Object roleName = roleObject.get("request");
//            if(roleName instanceof JSONObject){
//                JSONObject json = (JSONObject) roleName;
//                Object query = json.get("query");
//                if(query instanceof  JSONObject){
//                    JSONObject metadata = (JSONObject) query;
//                    role = (String) metadata.get("metadata");
//                }
//            }
//            if(role.equals("EXPERT")){
//                if(!usersService.checkEmail(email)){
//                    Expert expert = new Expert();
//                    expert.setName(givenName);
//                    expert.setEmail(email);
//                    expert.setPrice(0.0);
//                    expert.setAvatar(picture);
//                    expert.setRole(roleService.findRole(3));
//                    LocalDate localDate = LocalDate.now();
//                    expert.setCreateDate(localDate);
//                    Expert e = expertService.create(expert);
//                    return ResponseEntity.ok(e);
//                }else{
//                    return ResponseEntity.ok(authenticationService.getInforUser(email,givenName,picture));
//                }
//            }else{
//                throw new BadRequestException("You do not have permission to perform this operation.");
//            }
//        }else{
//            return ResponseEntity.ok("Token invalid");
//        }
//    }
//
//    @GetMapping("/protected/hr")
//    public ResponseEntity<?> getHrEmail(Principal principal) {
//        if (principal instanceof JwtAuthenticationToken) {
//            String role = "";
//            JwtAuthenticationToken jwt = (JwtAuthenticationToken) principal;
//            JSONObject roleObject = jwt.getToken().getClaim("event");
//            String email = jwt.getToken().getClaimAsString("email");
//            JSONObject userInfoJson = jwt.getToken().getClaim("user_info");
//            String givenName = userInfoJson.get("given_name").toString();
//            String picture = userInfoJson.get("picture").toString();
//            Object roleName = roleObject.get("request");
//            if(roleName instanceof JSONObject){
//                JSONObject json = (JSONObject) roleName;
//                Object query = json.get("query");
//                if(query instanceof  JSONObject){
//                    JSONObject metadata = (JSONObject) query;
//                    role = (String) metadata.get("metadata");
//                }
//            }
//            if(role.equals("HR")){
//                if(!usersService.checkEmail(email)){
//                    HR hr = new HR();
//                    hr.setName(givenName);
//                    hr.setEmail(email);
//                    hr.setAvatar(picture);
//                    hr.setRole(roleService.findRole(1));
//                    LocalDate localDate = LocalDate.now();
//                    hr.setCreateDate(localDate);
//                    hr.setSubscription(false);
//                    hr.setCompanyName("ABC Company");
//                    hr.setCompanyLocation("ABC Location");
//                    hr.setCompanyDescription("ABC Description");
//                    hr.setCompanyLogo(picture);
//                    hr.setVip(false);
//                    HR e = hrService.create(hr);
//                    return ResponseEntity.ok(e);
//                }else{
//                    return ResponseEntity.ok(authenticationService.getInforUser(email,givenName,picture));
//                }
//            }else{
//                throw new BadRequestException("You do not have permission to perform this operation.");
//            }
//        }else{
//            return ResponseEntity.ok("Token invalid");
//        }
//    }

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
    @PreAuthorize("hasAuthority('read:admin')")
    public Message getAdmin() {
        return messageService.getAdminMessage();
    }


}
