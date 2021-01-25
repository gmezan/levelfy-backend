package com.uc.backend.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.uc.backend.dto.TokenDto;
import com.uc.backend.entity.Role;
import com.uc.backend.enums.RoleName;
import com.uc.backend.security.jwt.JwtProvider;
import com.uc.backend.security.jwt.JwtTokenFilter;
import com.uc.backend.service.RoleService;
import com.uc.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final static Logger logger = LoggerFactory.getLogger(OAuthController.class);

    @Value("${google.clientId}")
    String googleClientId;

    @Value("${secret.psw}")
    String secretPsw;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @PostMapping("google")
    public ResponseEntity<?> google(@RequestBody TokenDto tokenDto) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(googleClientId));

        final GoogleIdToken googleIdToken =  GoogleIdToken.parse(
                verifier.getJsonFactory(), tokenDto.getValue()
        );
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();

        //return  new ResponseEntity<>(payload, HttpStatus.OK);

        /*
            TODO: Verify if the user is Enabled/Active

        */
        com.uc.backend.entity.User user =  new com.uc.backend.entity.User();
        if (userService.existsByEmail(payload.getEmail())) // If it is already registered
        {
            logger.info("Registered user");
            user = userService.getByEmail(payload.getEmail()).get();
        }
        else // if is a new user
        {
            logger.info("New user");
            user = saveUser(payload.getEmail());
        }


        TokenDto tokenRes = login(user);
        return new ResponseEntity<>(tokenRes, HttpStatus.OK);

    }

    @PostMapping("facebook")
    public ResponseEntity<?> facebook(@RequestBody TokenDto tokenDto) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        final String[] fields = {"email", "picture"};
        User facebookUser = facebook.fetchObject("me", User.class, fields);

        com.uc.backend.entity.User user =  new com.uc.backend.entity.User();
        if (userService.existsByEmail(user.getEmail())) // If it is already registered
            user = userService.getByEmail(facebookUser.getEmail()).get();
        else // if is a new user
            user = saveUser(facebookUser.getEmail());

        TokenDto tokenRes = login(user);
        return new ResponseEntity<>(tokenRes, HttpStatus.OK);

    }

    private TokenDto login(com.uc.backend.entity.User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), secretPsw)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(jwt);
        return tokenDto;
    }

    private com.uc.backend.entity.User saveUser(String email){
        com.uc.backend.entity.User user =  new com.uc.backend.entity.User();
        user.setEmail(email);
        user.setLastname("");
        user.setName("Mock name");
        user.setPassword(passwordEncoder.encode(secretPsw));
        Role role = roleService.getByName(RoleName.client).get();
        Set<Role> roles =  new HashSet<>(); roles.add(role);
        user.setRole(roles);

        return userService.save(user);

    }


}
