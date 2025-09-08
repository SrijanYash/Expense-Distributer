package com.srijan.expence_service.Model;

import java.util.List;

import lombok.Data;

@Data
public class Dto {
    private List<Integer> userIds; 
    private List<Double> percentage; 
    private List<Double> amount;
}
