package com.zhukowez.service;

import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AthleteAggServiceImpl implements AthleteAggregationService {

    @Autowired
    private AthleteRepository athleteRepository;

    public AthleteAggServiceImpl(AthleteRepository athleteRepository) {

        this.athleteRepository = athleteRepository;
    }


    @Override
    public Map<String, Object> getStats() {

        List<Athlete> athletes = athleteRepository.findAll();
        Athlete one = athleteRepository.findOne(2L);
       // athleteRepository.searchAthlete("searchString", weight);

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("allAthletes", athletes);
        resultMap.put("oneAthlete", one);

        return resultMap;
    }


}
