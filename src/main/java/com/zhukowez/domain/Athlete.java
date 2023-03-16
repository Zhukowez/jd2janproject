package com.zhukowez.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Athlete {

    private Long id;
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

    public Athlete(String name, String surname, Timestamp birthDate, Double height, Double weight,
                   String email, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Athlete(String name, String surname, Timestamp birthDate, Double height, Double weight,
                   String email, String phoneNumber, Timestamp created, Timestamp changed, Boolean deleted) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.created = created;
        this.changed = changed;
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
