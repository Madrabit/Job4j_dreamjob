package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.dream.model.User;

/**
 * @author madrabit on 30.04.2020
 * @version 1$
 * @since 0.1
 */
public class PsqlStore implements Store {
    private static final Logger LOG = LogManager.getLogger(PsqlStore.class.getName());

    private static final BasicDataSource pool = new BasicDataSource();

    public BasicDataSource getPool() {
        return pool;
    }

    PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
        createTables();
    }

    private void createTables() {
        try (BufferedReader br = new BufferedReader(
                new FileReader("db" + File.separator + "schema.sql"))) {
            String line;
            try (Connection cn = pool.getConnection();
                 Statement st = cn.createStatement()) {
                while ((line = br.readLine()) != null) {
                    st.execute(line);
                }
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }



    @Override
    public void addUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO registered_users(name, email, password) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public User findByEmail(String email) {
        User user = new User(0, "", "", "");
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("SELECT * FROM registered_users WHERE email = ?")
        ) {
            st.setString(1, email);
            try (ResultSet it = st.executeQuery()) {
                while (it.next()) {
                    user.setId(it.getInt("id"));
                    user.setName(it.getString("name"));
                    user.setEmail(it.getString("email"));
                    user.setPassword(it.getString("password"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return user;
    }
}
