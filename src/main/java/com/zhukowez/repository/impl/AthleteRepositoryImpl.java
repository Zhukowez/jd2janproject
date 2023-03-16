package com.zhukowez.repository.impl;

import com.zhukowez.configuration.DatabaseProperties;
import com.zhukowez.domain.Athlete;
import com.zhukowez.repository.AthleteRepository;
import lombok.RequiredArgsConstructor;
    import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
    private final static String INSERT_ATHLETES_FIRST_TO_DB_QUERY = "INSERT INTO m_athletes (name, surname, " +
            "birth_date, height, weight, e_mail, phone_number, created, changed, is_deleted, role_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String CHANGE_ATHLETE_STATUS_QUERY = "UPDATE m_athletes SET is_deleted = ? WHERE id_athlete = ?";
    private static final String UPDATE_ATHLETE_QUERY = "UPDATE m_athletes SET name = ?, surname = ?, birth_date = ?, height = ?, weight = ?, " +
            "e_mail = ?, phone_number = ?, created = ?, changed = ?, is_deleted = ?, role_id = ? WHERE id_athlete = ?";
    private final DatabaseProperties properties;

    private final Logger logger = Logger.getLogger(AthleteRepositoryImpl.class);

    private void registerDriver() {
        try {
            Class.forName(properties.getDriverName());
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Cannot be loaded!");
            throw new RuntimeException("JDBC Driver Cannot be loaded!");
        }
    }

    @Override
    public List<Athlete> findAll() {

        logger.info("Start of findAll method");

        final String findAllQuery = "SELECT * FROM m_athletes";

        List<Athlete> result = new ArrayList<>();

        registerDriver();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(findAllQuery)
        ) {

            while (resultSet.next()) {
                Athlete athlete = new Athlete();
                athlete.setId(resultSet.getLong("id_athlete"));
                athlete.setName(resultSet.getString("name"));
                athlete.setSurname(resultSet.getString("surname"));
                athlete.setBirthDate(Timestamp.valueOf(resultSet.getTimestamp("birth_date").toLocalDateTime()));
                athlete.setHeight((double) resultSet.getFloat("height"));
                athlete.setWeight((double) resultSet.getFloat("weight"));
                athlete.setEmail(resultSet.getString("e_mail"));
                athlete.setPhoneNumber(resultSet.getString("phone_number"));
                athlete.setCreated(Timestamp.valueOf(resultSet.getTimestamp("created").toLocalDateTime()));
                athlete.setChanged(Timestamp.valueOf(resultSet.getTimestamp("changed").toLocalDateTime()));
                athlete.setDeleted(resultSet.getBoolean("is_deleted"));
                athlete.setRoleID(resultSet.getLong("role_id"));

                result.add(athlete);
            }

            logger.info("End of findAll method");

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("SQL Issues!");
        }
        return result;
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
        final String findOneQuery = "SELECT * FROM m_athletes WHERE id_athlete = ?";

        Athlete athlete = null;

        registerDriver();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(findOneQuery)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    athlete = parseResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("SQL Issues!");
        }
        return athlete;
    }

    @Override
    public Athlete findById(Long id) {
        /*final String findByIdQuery = "select * from m_athlete where id = " + id;
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
        return list.get(0);*/
        return findOne(id);
    }


    @Override
    public Athlete create(Athlete athlete) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ATHLETES_FIRST_TO_DB_QUERY,
                    Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, athlete.getName());
                preparedStatement.setString(2, athlete.getSurname());
                preparedStatement.setTimestamp(3, athlete.getBirthDate());
                preparedStatement.setDouble(4, athlete.getHeight());
                preparedStatement.setDouble(5, athlete.getWeight());
                preparedStatement.setString(6, athlete.getEmail());
                preparedStatement.setString(7, athlete.getPhoneNumber());
                preparedStatement.setTimestamp(8, athlete.getCreated());
                preparedStatement.setTimestamp(9, athlete.getChanged());
                preparedStatement.setBoolean(10, athlete.getDeleted());
                preparedStatement.setLong(11, athlete.getRoleID());
                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        athlete.setId(generatedKeys.getLong(1));
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
            preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis())); // Устанавливаем текущее время как время изменения
            preparedStatement.setBoolean(10, athlete.getDeleted());
            preparedStatement.setLong(11, athlete.getRoleID());
            preparedStatement.setLong(12, athlete.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Exception with updating Athlete in DB", e);
            throw new RuntimeException("SQL Issues!");
        }
        return findById(athlete.getId());
    }


    @Override
    public void delete(Long id) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_ATHLETE_STATUS_QUERY)) {

                preparedStatement.setBoolean(1, true); // Используйте индекс параметра в SQL-запросе
                preparedStatement.setLong(2, id); // Установите второй параметр - ID атлета
                preparedStatement.executeUpdate();
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


    private static final String FIND_ALL_ATHLETES_BY_HEIGHT_QUERY = "SELECT * FROM m_athletes WHERE height = ? AND is_deleted = false";

    @Override
    @Pointcut
    public List<Athlete> findAllAthletesByHeight(double height) {
        List<Athlete> athletes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_ATHLETES_BY_HEIGHT_QUERY)) {

            preparedStatement.setDouble(1, height);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    athletes.add(mapAthlete(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Exception with finding athletes by height", e);
            throw new RuntimeException("SQL Issues!");
        }
        return athletes;
    }

    private Athlete mapAthlete(ResultSet resultSet) throws SQLException {
        return Athlete.builder()
                .id(resultSet.getLong("id_athlete"))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .birthDate(resultSet.getTimestamp("birth_date"))
                .height(resultSet.getDouble("height"))
                .weight(resultSet.getDouble("weight"))
                .email(resultSet.getString("e_mail"))
                .phoneNumber(resultSet.getString("phone_number"))
                .created(resultSet.getTimestamp("created"))
                .changed(resultSet.getTimestamp("changed"))
                .deleted(resultSet.getBoolean("is_deleted"))
                .roleID(resultSet.getLong("role_id"))
                .build();
    }

    private static final String FIND_ALL_ATHLETES_BY_Weight_QUERY = "SELECT * FROM m_athletes WHERE weight = ? AND is_deleted = false";

    @Override
    @Pointcut
    public List<Athlete> findAllAthletesByWeight(double weight) {
        List<Athlete> athletes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_ATHLETES_BY_Weight_QUERY)) {

            preparedStatement.setDouble(1, weight);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    athletes.add(mapAthlete(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Exception with finding athletes by weight", e);
            throw new RuntimeException("SQL Issues!");
        }
        return athletes;
    }

    private static final String FIND_ATHLETES_BY_NAME_AND_SURNAME_QUERY = "SELECT * FROM m_athletes WHERE name = ? AND surname = ? AND is_deleted = false";

    @Override
    @Pointcut
    public List<Athlete> findAthletesByNameAndSurname(String name, String surname) {
        List<Athlete> athletes = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ATHLETES_BY_NAME_AND_SURNAME_QUERY)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    athletes.add(mapAthlete(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Exception with finding athletes by name and surname", e);
            throw new RuntimeException("SQL Issues!");
        }
        return athletes;
    }

    @Override
    public void searchAthlete() {

    }


}
