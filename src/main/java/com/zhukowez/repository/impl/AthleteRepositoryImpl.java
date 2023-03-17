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

/*    public AthleteRepositoryImpl(DataSource dataSource, AthleteRowMapper athleteRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.athleteRowMapper = athleteRowMapper;
    }*/


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
    public List<Athlete> findAthletesByNameAndSurname(String name, String surname) {
        String sql = "SELECT * FROM m_athletes WHERE name = ? AND surname = ?";
        return jdbcTemplate.query(sql, athleteRowMapper, name, surname);
    }



    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM m_athletes WHERE id_athlete = ?";
        jdbcTemplate.update(sql, id);
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

        return findOne(athlete.getId());
    }



    @Override
    public List<Athlete> searchAthlete(String searchQuery) {
        String sql = "SELECT * FROM m_athletes WHERE LOWER(name) LIKE LOWER(?) OR LOWER(surname) LIKE LOWER(?);";
        String query = "%" + searchQuery + "%";
        return jdbcTemplate.query(sql, athleteRowMapper, query, query);
    }



    /*private static final Logger LOGGER = Logger.getLogger(AthleteRepository.class.getName());
    private final static String INSERT_ATHLETES_FIRST_TO_DB_QUERY = "INSERT INTO m_athletes (name, surname, " +
            "birth_date, height, weight, e_mail, phone_number, created, changed, is_deleted, role_ID) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String CHANGE_ATHLETE_STATUS_QUERY = "UPDATE m_athletes SET is_deleted = ? WHERE id_athlete = ?";
    private static final String UPDATE_ATHLETE_QUERY = "UPDATE m_athletes SET name = ?, surname = ?, birth_date = ?, height = ?, weight = ?, " +
            "e_mail = ?, phone_number = ?, created = ?, changed = ?, is_deleted = ?, role_id = ? WHERE id_athlete = ?";

    private final Logger logger = Logger.getLogger(AthleteRepositoryImpl.class);

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


    @Override
    public Athlete findOne(Long id) {
        String sql = "SELECT * FROM m_athletes WHERE id = ? AND is_deleted = false";
        RowMapper<Athlete> rowMapper = (rs, rowNum) -> mapAthlete(rs);
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Athlete findById(Long id) {
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
*/
}
