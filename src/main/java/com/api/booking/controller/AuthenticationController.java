package com.api.booking.controller;

import com.api.booking.config.JwtTokenService;
import com.api.booking.config.JwtUserDetailsService;
import com.api.booking.dto.AuthenticationRequest;
import com.api.booking.dto.AuthenticationResponse;
import com.api.booking.dto.JwtUserDetails;
import com.api.booking.dto.RegistrationRequest;
import com.api.booking.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenService jwtTokenService;
    private final RegistrationService registrationService;

    public AuthenticationController(final AuthenticationManager authenticationManager,
                                    final JwtUserDetailsService jwtUserDetailsService,
                                    final JwtTokenService jwtTokenService, RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.registrationService = registrationService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
        return authenticationResponse;
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid final RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public String index() {
        return "\"Hello World! :) \n  Welcome to the Booking Application!\"";
    }
}
