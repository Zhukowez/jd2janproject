package com.zhukowez.repository.impl;

import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import com.zhukowez.repository.rowmapper.AthleteRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Primary
@Component
@RequiredArgsConstructor
public class AthleteRepositoryImpl implements AthleteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AthleteRowMapper athleteRowMapper;

    @Override
    public Athlete create(Athlete athlete) {
        String sql = "INSERT INTO m_athletes (name, surname, birth_date, weight, height, e_mail, phone_number, created, changed, is_deleted, role_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_athlete";
        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                athlete.getName(),
                athlete.getSurname(),
                athlete.getBirthDate(),
                athlete.getWeight(),
                athlete.getHeight(),
                athlete.getEmail(),
                athlete.getPhoneNumber(),
                athlete.getCreated(),
                athlete.getChanged(),
                athlete.getDeleted(),
                athlete.getRoleID());
        athlete.setId(id);
        return athlete;
    }

    @Override
    public Athlete findOne(Long id) {
        String sql = "SELECT * FROM m_athletes WHERE id_athlete = ?";
        try {
            return jdbcTemplate.queryForObject(sql, athleteRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Athlete> findAll() {
        String sql = "SELECT * FROM m_athletes";
        return jdbcTemplate.query(sql, athleteRowMapper);
    }

    @Override
    public Optional<Athlete> findById(Long id) {
        String sql = "SELECT * FROM m_athletes WHERE id_athlete = ?";
        try {
            Athlete athlete = jdbcTemplate.queryForObject(sql, athleteRowMapper, id);
            return Optional.ofNullable(athlete);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Athlete update(Athlete athlete) {
        String sql = "UPDATE m_athletes SET name = ?, surname = ?, birth_date = ?, weight = ?, height = ?, e_mail = ?, phone_number = ?, created = ?, changed = ?, is_deleted = ?, role_id = ? WHERE id_athlete = ?";

        jdbcTemplate.update(sql,
                athlete.getName(),
                athlete.getSurname(),
                athlete.getBirthDate(),
                athlete.getWeight(),
                athlete.getHeight(),
                athlete.getEmail(),
                athlete.getPhoneNumber(),
                athlete.getCreated(),
                athlete.getChanged(),
                athlete.getDeleted(),
                athlete.getRoleID(),
                athlete.getId());

        return athlete;
    }

    @Override
    public List<Athlete> searchAthlete(String searchQuery, Double weight) {
        String sql = "SELECT * FROM m_athletes WHERE LOWER(name) LIKE LOWER(?) OR LOWER(surname) LIKE LOWER(?);";
        String query = "%" + searchQuery + "%";
        return jdbcTemplate.query(sql, athleteRowMapper, query, query);
    }

    @Override
    public List<Athlete> findAthletesByNameAndSurname(String name, String surname) {
        String sql = "SELECT * FROM m_athletes WHERE name = ? AND surname = ?";
        return jdbcTemplate.query(sql, athleteRowMapper, name, surname);
    }

    public List<Athlete> findAthletesOlderThan(int age) {
        String sql = "SELECT * FROM get_athletes_older_than(?);";
        return jdbcTemplate.query(sql, new Object[]{age}, (rs, rowNum) -> {
            Athlete athlete = new Athlete();
            athlete.setId((long) rs.getInt("id_athlete"));
            athlete.setName(rs.getString("name"));
            athlete.setSurname(rs.getString("surname"));
            athlete.setEmail(rs.getString("e_mail"));
            athlete.setBirthDate(rs.getTimestamp("birth_date"));
            return athlete;
        });
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM m_athletes WHERE id_athlete = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateEmail(int id, String email) {
        jdbcTemplate.update("CALL update_athlete_email(?, ?)", id, email);
    }

    @Override
    public boolean support(String param) {
        return param.equalsIgnoreCase("jdbctemplate");
    }

}
