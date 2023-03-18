package com.zhukowez.controller.requests;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AthleteCreateRequest {

    private String name;
    private String surname;
    private Timestamp birthDate;
    private Double height;
    private Double weight;
    private String email;
    private String phoneNumber;
    private Timestamp created;
    private Timestamp changed;
    private Boolean deleted;
    private Long roleID;

}
