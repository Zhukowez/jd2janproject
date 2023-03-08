package com.zhukowez.service;

import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import com.zhukowez.util.RandomValuesGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AthleteServiceImpl implements AthleteService {


    private final AthleteRepository athleteRepository;

    private final RandomValuesGenerator randomValuesGenerator;

    public AthleteServiceImpl(AthleteRepository athleteRepository, RandomValuesGenerator randomValuesGenerator) {
        this.athleteRepository = athleteRepository;
        this.randomValuesGenerator = randomValuesGenerator;
    }

    @Override
    public Athlete findOne(Long id) {
        return null;
    }

    @Override
    public List<Athlete> findAll() {
        return athleteRepository.findAll();
    }

    @Override
    public Athlete create(Athlete object) {
        if (object.getWeight() > 90) {
            throw new RuntimeException("Something wrong!");
        }

        return athleteRepository.create(object);
    }

    @Override
    public Athlete update(Athlete object) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
