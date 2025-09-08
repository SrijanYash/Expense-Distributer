package com.srijan.expence_service.Model;

// import com.srijan.expence_service.Model.Expence;

import lombok.Data;

@Data
public class ExpenceRequest {

    private Expence expence;   // main expense details
    private Dto splitDetails;  // extra split details

    // getters and setters

    public Dto getSplitDetails() {
        return splitDetails;
    }

    public Expence getExpence() {
        return expence;
    }
}
