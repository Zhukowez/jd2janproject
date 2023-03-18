package com.zhukowez.repository;

import com.zhukowez.domain.Athlete;

import java.util.List;
import java.util.Optional;

public interface AthleteRepository extends CRUDRepository<Long, Athlete> {

    void delete(Long id);

    Athlete update(Athlete athlete);

    List<Athlete> searchAthlete(String searchQuery);

    Athlete findOne(Long id);

    List<Athlete> findAll();

    List<Athlete> findAthletesByNameAndSurname(String name, String surname);

    Optional<Athlete> findById(Long id);

    Athlete create(Athlete athlete);


    List<Athlete> findAthletesOlderThan(int age);

    void updateEmail(int id, String email);


}
