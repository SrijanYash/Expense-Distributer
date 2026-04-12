package com.srijan.user_group_service.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDTO {

    private Integer id;
    private String name;
    private Long phone;
    private String email;

    public UserInfoDTO(Integer id, String name, Long phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public UserInfoDTO(String name, Long phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
