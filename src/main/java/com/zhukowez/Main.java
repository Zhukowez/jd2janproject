package com.zhukowez;

import com.zhukowez.configuration.DatabaseProperties;
import com.zhukowez.repository.AthleteRepository;
import com.zhukowez.repository.impl.AthleteRepositoryImpl;


public class Main {

    public static void main(String[] args) {

        AthleteRepository athleteRepository = new AthleteRepositoryImpl(new DatabaseProperties());


    }
}