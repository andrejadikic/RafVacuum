package com.example.RAFVacuum.services;

import com.example.RAFVacuum.adapter.auth.AuthenticationResponse;
import com.example.RAFVacuum.adapter.auth.LoginRequest;
import com.example.RAFVacuum.adapter.auth.RegisterRequest;
import com.example.RAFVacuum.model.User;
import com.example.RAFVacuum.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    User user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    userRepository.save(user);
    String jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .build();
  }

  public AuthenticationResponse authenticate(LoginRequest request) {
    System.out.println("authenticate");
    try{
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()
          )
      );
    }catch (Exception e){
      e.printStackTrace();
    }
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(()-> new UsernameNotFoundException("Email and password don't match"));

    String jwtToken = jwtService.generateToken(user);

    return new AuthenticationResponse(jwtToken,user);
  }

}
