package com.zhukowez.repository.rowmapper;

import com.zhukowez.domain.Athlete;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.zhukowez.repository.columns.AthleteColumns.BIRTH_DATE;
import static com.zhukowez.repository.columns.AthleteColumns.CHANGED;
import static com.zhukowez.repository.columns.AthleteColumns.CREATED;
import static com.zhukowez.repository.columns.AthleteColumns.DELETED;
import static com.zhukowez.repository.columns.AthleteColumns.EMAIL;
import static com.zhukowez.repository.columns.AthleteColumns.HEIGHT;
import static com.zhukowez.repository.columns.AthleteColumns.ID;
import static com.zhukowez.repository.columns.AthleteColumns.NAME;
import static com.zhukowez.repository.columns.AthleteColumns.PHONE_NUMBER;
import static com.zhukowez.repository.columns.AthleteColumns.ROLE_ID;
import static com.zhukowez.repository.columns.AthleteColumns.SURNAME;
import static com.zhukowez.repository.columns.AthleteColumns.WEIGHT;

@Component
public class AthleteRowMapper implements RowMapper<Athlete> {
    @Override
    public Athlete mapRow(ResultSet rs, int rowNum) throws SQLException {
        Athlete athlete;

        try {
            athlete = Athlete.builder()
                    .id(rs.getLong(ID))
                    .name(rs.getString(NAME))
                    .surname(rs.getString(SURNAME))
                    .birthDate(rs.getTimestamp(BIRTH_DATE))
                    .weight(rs.getDouble(WEIGHT))
                    .height(rs.getDouble(HEIGHT))
                    .email(rs.getString(EMAIL))
                    .phoneNumber(rs.getString(PHONE_NUMBER))
                    .created(rs.getTimestamp(CREATED))
                    .changed(rs.getTimestamp(CHANGED))
                    .deleted(rs.getBoolean(DELETED))
                    .roleID(rs.getLong(ROLE_ID))
                    .build();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return athlete;
    }
}
