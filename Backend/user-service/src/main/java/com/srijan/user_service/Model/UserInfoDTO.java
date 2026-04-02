package com.srijan.user_service.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// @AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

    private String name;
    private Long phone;
    private String email;

    public UserInfoDTO(String name, Long phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

}
