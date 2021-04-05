package com.uc.backend.controller.general;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.uc.backend.dto.TokenDto;
import com.uc.backend.entity.Role;
import com.uc.backend.enums.RoleName;
import com.uc.backend.security.jwt.JwtProvider;
import com.uc.backend.service.model.RoleService;
import com.uc.backend.service.model.UserService;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
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

    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    JwtProvider jwtProvider;
    UserService userService;
    RoleService roleService;

    @Autowired
    public OAuthController(RoleService roleService, UserService userService, JwtProvider jwtProvider,
                           AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("google")
    public ResponseEntity<?> google(@RequestBody TokenDto tokenDto) throws IOException, IllegalArgumentException {
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

        /*            TODO: Verify if the user is Enabled/Active
 */
        com.uc.backend.entity.User user =  new com.uc.backend.entity.User();
        if (userService.existsByEmail(payload.getEmail())) // If it is already registered
        {

            logger.info("Registered user");
            user = userService.getByEmailOrThrow(payload.getEmail());
            if (user.getRole().isEmpty())
                userService.save(setClientRole(user));
            if (!user.isActive())
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        }
        else // if is a new user
        {

            logger.info("New user");
            user = saveNewUser(payload);

        }


        TokenDto tokenRes = login(user);
        return new ResponseEntity<>(tokenRes, HttpStatus.OK);

    }


    @GetMapping("logout")
    public ResponseEntity<?> getLogout() {
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("logout")
    public ResponseEntity<?> postLogout() {
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    /*
    @PostMapping("facebook")
    public ResponseEntity<?> facebook(@RequestBody TokenDto tokenDto) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        final String[] fields = {"email", "picture", "name"};
        User facebookUser = facebook.fetchObject("me", User.class, fields);

        com.uc.backend.entity.User user =  new com.uc.backend.entity.User();
        if (userService.existsByEmail(user.getEmail())) // If it is already registered
            user = userService.getByEmail(facebookUser.getEmail()).get();
        else // if is a new user
            user = saveUser(facebookUser);

        TokenDto tokenRes = login(user);
        return new ResponseEntity<>(tokenRes, HttpStatus.OK);

    }*/

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

    private com.uc.backend.entity.User saveNewUser(GoogleIdToken.Payload payload){
        com.uc.backend.entity.User user =  new com.uc.backend.entity.User();
        user.setEmail(payload.getEmail());
        user.setLastname("");
        user.setName((String) payload.get("name"));
        user.setPhoto((String) payload.get("picture"));
        user.setPassword(passwordEncoder.encode(secretPsw));
        user.setActive(true);

        return userService.save(setClientRole(user));

    }

    private com.uc.backend.entity.User setClientRole(com.uc.backend.entity.User user) {
        Optional<Role> optionalRole = roleService.getByName(RoleName.ROLE_CLIENT);

        if (optionalRole.isPresent()) {
            Set<Role> roles =  new HashSet<>(); roles.add(optionalRole.get());
            user.setRole(roles);
        } else user = null;

        return user;
    }



}
