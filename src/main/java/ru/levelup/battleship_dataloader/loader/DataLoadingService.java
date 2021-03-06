package ru.levelup.battleship_dataloader.loader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoadingService implements CommandLineRunner {

    private JdbcTemplate template;

    @Override
    public void run(String... args) throws Exception {

        byte[] mapData = Files.readAllBytes(Paths.get("src/main/resources/users.json"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<User> users = mapper.readValue(mapData, mapper.getTypeFactory().constructCollectionType(List.class,
                User.class));

        batchInsert(users);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void batchInsert(List<User> users) {
        template.batchUpdate("insert into users (login, password, player_field_arranged, rating) " +
                "values (?, ?, ?, ?) ", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                User user = users.get(i);
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getPassword());
                ps.setBoolean(3, user.isPlayerFieldArranged());
                ps.setDouble(4, user.getRating());
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }
}