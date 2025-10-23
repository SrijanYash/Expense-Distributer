
package com.srijan.expence_service.Model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Embeddable
public class UserExpence {

    private String userName;
    private double amount;
    
}
