package com.example.RAFVacuum.adapter.auth;

import com.example.RAFVacuum.model.Permission;
import com.example.RAFVacuum.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("jwt")
  private String accessToken;
  private List<AuthorityResponse> authorities;

  public AuthenticationResponse(String accessToken, User user) {
    this.accessToken = accessToken;
    authorities = from(user);
    System.out.println(authorities);
  }

  private List<AuthorityResponse> from(User user){
    return user.getRole().getPermissions().stream().map(
        permission-> new AuthorityResponse(permission.getPermission())
    ).toList();
  }
}
