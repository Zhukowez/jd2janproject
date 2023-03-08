package com.zhukowez.repository;

import com.zhukowez.domain.Athlete;

import java.util.List;

public interface AthleteRepository extends CRUDRepository<Long, Athlete> {

    Athlete findById(Long id);

    List<Athlete> findAllAthletesByHeight();


    List<Athlete> findAllAthletesByWeight();

    List<Athlete> findAllAthletesByHeight(double height);

    List<Athlete> findAllAthletesByWeight(Double weight);

    void searchAthlete();

}
