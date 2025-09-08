package com.srijan.user_group_service.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// @AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

    private String name;
    private long phone;
    private String email;

    public UserInfoDTO(String name, long phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
