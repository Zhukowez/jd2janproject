package com.zhukowez.repository;

import com.zhukowez.domain.Athlete;

import java.util.List;
import java.util.Optional;

public interface AthleteRepository extends CRUDRepository<Long, Athlete> {

    Optional<Athlete> findById(Long id);

    List<Athlete> searchAthlete(String searchQuery, Double weight);


    List<Athlete> findAthletesByNameAndSurname(String name, String surname);


    List<Athlete> findAthletesOlderThan(int age);

    void updateEmail(int id, String email);

    boolean support(String param);


}
