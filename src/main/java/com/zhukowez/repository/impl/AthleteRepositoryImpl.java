package com.zhukowez.repository.impl;

import com.zhukowez.configuration.DatabaseProperties;
import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.zhukowez.repository.columns.AthleteColumns.ID;
import static com.zhukowez.repository.columns.AthleteColumns.NAME;
import static com.zhukowez.repository.columns.AthleteColumns.SURNAME;
import static com.zhukowez.repository.columns.AthleteColumns.WEIGHT;
import static com.zhukowez.repository.columns.AthleteColumns.BIRTH_DATE;
import static com.zhukowez.repository.columns.AthleteColumns.HEIGHT;
import static com.zhukowez.repository.columns.AthleteColumns.EMAIL;
import static com.zhukowez.repository.columns.AthleteColumns.PHONE_NUMBER;
import static com.zhukowez.repository.columns.AthleteColumns.CREATED;
import static com.zhukowez.repository.columns.AthleteColumns.CHANGED;
import static com.zhukowez.repository.columns.AthleteColumns.DELETED;
import static com.zhukowez.repository.columns.AthleteColumns.ROLE_ID;


@Repository
@RequiredArgsConstructor
@Primary

public class AthleteRepositoryImpl implements AthleteRepository {

    private static final Logger LOGGER = Logger.getLogger(AthleteRepository.class.getName());
    private final static String INSERT_ATHLETES_FIRST_TO_DB_QUERY = "INSERT INTO m_athletes" +
            "(name, surname, email, phoneNumber) VALUES (?,?,?,?)";
    private final static String INSERT_ATHLETES_SECOND_TO_DB_QUERY = "INSERT INTO m_athletes" +
            "(birthDate, height, weight) VALUES (?,?,?)";
    private final static String CHANGE_ATHLETE_STATUS_QUERY = "UPDATE m_athletes SET is_deleted = ? WHERE (`id` = ?)";

    private final static String UPDATE_ATHLETE_QUERY = "UPDATE m_athletes SET name = ?, surname = ?, birth_date = ?, " +
            "height = ?, weight = ?, e-mail = ?, phone_number = ?, created = ?, changed = ?, " +
            " WHERE (`id` = ?)";
    private final DatabaseProperties properties;

    private final Logger logger = Logger.getLogger(AthleteRepositoryImpl.class);


    @Override
    public List<Athlete> findAll() {

        logger.info("Start of findAll method");

        final String findAllQuery = "select * from m_athletes order by id desc";

        List<Athlete> result = new ArrayList<>();

        registerDriver();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(findAllQuery)
        ) {

            while (rs.next()) {
                result.add(parseResultSet(rs));
            }

            logger.info("End of findAll method");

            return result;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("SQL Issues!");
        }
    }

    private Athlete parseResultSet(ResultSet rs) {

        Athlete athlete;

        try {
            athlete = new Athlete();
            athlete.setId(rs.getLong(ID));
            athlete.setName(rs.getString(NAME));
            athlete.setSurname(rs.getString(SURNAME));
            athlete.setBirthDate(rs.getTimestamp(BIRTH_DATE));
            athlete.setWeight(rs.getDouble(WEIGHT));
            athlete.setHeight(rs.getDouble(HEIGHT));
            athlete.setEmail(rs.getString(EMAIL));
            athlete.setPhoneNumber(rs.getString(PHONE_NUMBER));
            athlete.setCreated(rs.getTimestamp(CREATED));
            athlete.setChanged(rs.getTimestamp(CHANGED));
            athlete.setDeleted(rs.getBoolean(DELETED));
            athlete.setRoleID(rs.getLong(ROLE_ID));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return athlete;
    }

    private void registerDriver() {
        try {
            Class.forName(properties.getDriverName());
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }
    }

    private Connection getConnection() {
        String jdbcURL = StringUtils.join(properties.getUrl(), properties.getPort(), properties.getName());
        try {
            return DriverManager.getConnection(jdbcURL, properties.getLogin(), properties.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Athlete findOne(Long id) {
        return findAll().stream().findFirst().get();
    }

    @Override
    public Athlete findById(Long id) {
        final String findByIdQuery = "select * from m_athlete where id = " + id;
        registerDriver();
        List<Athlete> list = new ArrayList<>();

        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(findByIdQuery);
            resultSet = preparedStatement.executeQuery(findByIdQuery);

            while (resultSet.next()) {
                list.add(parseResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("DB connection process issues", e);
            throw new RuntimeException(e);
        }
        if (list.size() > 1) {
            LOGGER.error("non-unique id");
        }
        return list.get(0);
    }


    @Override
    public Athlete create(Athlete athlete) {

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatementFirst = connection.prepareStatement(INSERT_ATHLETES_FIRST_TO_DB_QUERY,
                    Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement preparedStatementSecond = connection.prepareStatement(INSERT_ATHLETES_SECOND_TO_DB_QUERY)) {
                preparedStatementFirst.setString(1, athlete.getName());
                preparedStatementFirst.setString(2, athlete.getSurname());
                preparedStatementFirst.setString(3, athlete.getEmail());
                preparedStatementFirst.setString(4, athlete.getPhoneNumber());
                preparedStatementFirst.executeUpdate();

                try (ResultSet generatedKeys = preparedStatementSecond.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int athleteId = generatedKeys.getInt(1);
                        preparedStatementSecond.setInt(1, athleteId);
                        preparedStatementSecond.setTimestamp(5, athlete.getBirthDate());
                        preparedStatementSecond.setDouble(6, athlete.getHeight());
                        preparedStatementSecond.setDouble(7, athlete.getWeight());
                        preparedStatementSecond.executeUpdate();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.log(Level.INFO, "Exception with ending transaction", e);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Exception with adding Athlete to BD", e);
            throw new RuntimeException("SQL Issues!");
        }
        return findById(athlete.getId());
    }

    @Override
    public Athlete update(Athlete athlete) {
        registerDriver();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ATHLETE_QUERY)) {
            preparedStatement.setString(1, athlete.getName());
            preparedStatement.setString(2, athlete.getSurname());
            preparedStatement.setTimestamp(3, athlete.getBirthDate());
            preparedStatement.setDouble(4, athlete.getHeight());
            preparedStatement.setDouble(5, athlete.getWeight());
            preparedStatement.setString(6, athlete.getEmail());
            preparedStatement.setString(7, athlete.getPhoneNumber());
            preparedStatement.setTimestamp(8, athlete.getCreated());
            preparedStatement.setTimestamp(9, athlete.getChanged());
            preparedStatement.setLong(10, athlete.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Exception with update Athlete info in DB", e);
            throw new RuntimeException("SQL Issues!");
        }
        return athlete;
    }


    @Override
    public void delete(Long id) {
        long idChange = id;
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_ATHLETE_STATUS_QUERY)) {

                preparedStatement.setBoolean((int) idChange, true);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                LOGGER.log(Level.INFO, "Exception with ending transaction", e);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Error with change status is_delete for Athlete", e);
            throw new RuntimeException("SQL Issues!");
        }
    }

    @Override
    public List<Athlete> findAllAthletesByHeight(double height) {
        final String findAllUsersByHeightQuery = "select * from m_athletes where height = ?";

        return getAthletes(findAllUsersByHeightQuery);
    }


    @Override
    public List<Athlete> findAllAthletesByWeight(Double weight) {
        final String findAllUsersByWeightQuery = "select * from m_athletes where weight = ?";

        return getAthletes(findAllUsersByWeightQuery);
    }

    private List<Athlete> getAthletes(String findAllAthletesByQuery) {
        List<Athlete> athletes = new ArrayList<>();

        registerDriver();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(findAllAthletesByQuery)
        ) {
            while (resultSet.next()) {
                athletes.add(parseResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("SQL Issues!");
        }
        return athletes;
    }

    @Override
    public void searchAthlete() {

    }

    @Override
    public List<Athlete> findAllAthletesByHeight() {
        return null;
    }

    @Override
    public List<Athlete> findAllAthletesByWeight() {
        return null;
    }
}
