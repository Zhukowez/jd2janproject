package com.zhukowez.repository;

import com.zhukowez.domain.Athlete;

import java.util.List;

public interface AthleteRepository extends CRUDRepository<Long, Athlete> {

    List<Athlete> findAll();

    Athlete findOne(Long id);


    Athlete findById(Long id);

    Athlete update(Athlete athlete);

    List<Athlete> findAllAthletesByHeight(double height);


    List<Athlete> findAllAthletesByWeight(double weight);

    List<Athlete> findAthletesByNameAndSurname(String name, String surname);

    void searchAthlete();
}
