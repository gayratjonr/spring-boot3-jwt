package com.mohirdev.mohidev.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mohirdev.mohidev.security.TokenProvider;
import com.mohirdev.mohidev.web.rest.vm.LoginVM;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserJwtController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    public UserJwtController(AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    static class JWTToken {
        private String token;

        public JWTToken(String token) {
            this.token = token;
        }

        @JsonProperty("jwt_token")
        public String getToken() {
            return token;
        }
    }
}
