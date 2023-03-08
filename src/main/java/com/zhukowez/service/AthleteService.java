package com.zhukowez.service;

import com.zhukowez.domain.Athlete;

import java.util.List;

public interface AthleteService {
    Athlete findOne(Long id);

    List<Athlete> findAll();

    Athlete create(Athlete object);

    Athlete update(Athlete object);

    void delete(Long id);
}
