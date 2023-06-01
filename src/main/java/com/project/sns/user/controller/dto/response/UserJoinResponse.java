package com.project.sns.user.controller.dto.response;

import com.project.sns.user.domain.User;
import com.project.sns.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

   private Long id;
   private UserRole role;

   public static UserJoinResponse of(User user) {
      return new UserJoinResponse(user.getId(), user.getRole());
   }
}
