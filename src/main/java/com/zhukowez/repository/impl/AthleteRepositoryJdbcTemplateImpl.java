package com.zhukowez.repository.impl;

import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import com.zhukowez.repository.rowmapper.AthleteRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AthleteRepositoryJdbcTemplateImpl implements AthleteRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final AthleteRowMapper athleteRowMapper;

    @Override
    public Athlete findOne(Long id) {
        return null;
    }

    @Override
    public List<Athlete> findAll() {
        return jdbcTemplate.query("select * from users", athleteRowMapper);
    }

    @Override
    public Athlete create(Athlete object) {
        return null;
    }

    @Override
    public Athlete update(Athlete object) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Athlete findById(Long id) {
        return null;
    }

    @Override
    public List<Athlete> findAllAthletesByHeight() {
        return null;
    }

    @Override
    public List<Athlete> findAllAthletesByWeight() {
        return null;
    }

    @Override
    public List<Athlete> findAllAthletesByHeight(double height) {
        return null;
    }

    @Override
    public List<Athlete> findAllAthletesByWeight(Double weight) {
        return null;
    }

    @Override
    public void searchAthlete() {

    }
}
