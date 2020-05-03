package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
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

    private PsqlStore() {
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
        try (Connection connect = pool.getConnection();
             Statement st = Objects.requireNonNull(connect).createStatement()) {
            st.execute("DROP TABLE IF EXISTS post; ");
            st.execute("DROP TABLE IF EXISTS candidates;");
//            st.execute("DROP TABLE IF EXISTS registered_users;");
            st.executeUpdate("CREATE TABLE post (id SERIAL PRIMARY KEY, name TEXT); ");
            st.executeUpdate("CREATE TABLE candidates (id SERIAL PRIMARY KEY, name TEXT, photoId TEXT);");
//            st.executeUpdate("CREATE TABLE registered_users (id SERIAL PRIMARY KEY, name TEXT, email TEXT, password TEXT);");
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }


    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidates")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name"), it.getString("photoId")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidates;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    private void createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidates(name, photoId) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getPhotoId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void createPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void updatePost(Post post) {
        try (Connection connection = pool.getConnection();
             PreparedStatement st = connection.prepareStatement("UPDATE post SET name = ? WHERE id =  ?")) {
            st.setString(1, post.getName());
            st.setInt(3, post.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void updateCandidate(Candidate candidate) {
        try (Connection connection = pool.getConnection();
             PreparedStatement st = connection.prepareStatement("UPDATE candidates SET name = ?, photoid = ? WHERE id =  ?")) {
            st.setString(1, candidate.getName());
            st.setString(2, candidate.getPhotoId());
            st.setInt(3, candidate.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate candidate = new Candidate(0, "", "");
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("SELECT * FROM candidates WHERE id = ?")
        ) {
            st.setInt(1, id);
            try (ResultSet it = st.executeQuery()) {
                while (it.next()) {
                    candidate.setId(it.getInt("id"));
                    candidate.setName(it.getString("name"));
                    candidate.setPhotoId(it.getString("photoId"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }

    @Override
    public Post findPostById(int id) {
        Post post = new Post(0, "");
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            st.setInt(1, id);
            try (ResultSet it = st.executeQuery()) {
                while (it.next()) {
                    post.setId(it.getInt("id"));
                    post.setName(it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return post;
    }

    @Override
    public void deleteCandidate(String id) {

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
