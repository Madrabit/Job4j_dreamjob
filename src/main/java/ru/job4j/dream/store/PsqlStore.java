package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

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
                    candidates.add(new Candidate(it.getInt("id"),
                            it.getString("name"),
                            it.getString("lastName"),
                            it.getString("country"),
                            it.getString("region"),
                            it.getString("city"),
                            it.getString("sex"),
                            it.getString("description"),
                            it.getString("photoId")));
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
             PreparedStatement ps = cn.prepareStatement("INSERT INTO candidates(name, lastName, country, region, city, sex, description, photoId) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getLastName());
            ps.setString(3, candidate.getCountry());
            ps.setString(4, candidate.getRegion());
            ps.setString(5, candidate.getCity());
            ps.setString(6, candidate.getSex());
            ps.setString(7, candidate.getDescription());
            ps.setString(8, candidate.getPhotoId());
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
             PreparedStatement ps = connection.prepareStatement("UPDATE candidates SET name = ?, lastName = ?, country = ?, region = ?, city = ?, sex = ?, description = ?, photoid = ? "
                     + "WHERE id =  ?")) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getLastName());
            ps.setString(3, candidate.getCountry());
            ps.setString(4, candidate.getRegion());
            ps.setString(5, candidate.getCity());
            ps.setString(6, candidate.getSex());
            ps.setString(7, candidate.getDescription());
            ps.setString(8, candidate.getPhotoId());
            ps.setInt(9, candidate.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate candidate = new Candidate(0, "", "", "", "", "", "", "", "");
        try (Connection cn = pool.getConnection();
             PreparedStatement st = cn.prepareStatement("SELECT * FROM candidates WHERE id = ?")
        ) {
            st.setInt(1, id);
            try (ResultSet it = st.executeQuery()) {
                while (it.next()) {
                    candidate.setId(it.getInt("id"));
                    candidate.setName(it.getString("name"));
                    candidate.setLastName(it.getString("lastName"));
                    candidate.setCountry(it.getString("country"));
                    candidate.setRegion(it.getString("region"));
                    candidate.setCity(it.getString("city"));
                    candidate.setSex(it.getString("sex"));
                    candidate.setDescription(it.getString("description"));
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
        File file = new File("images" + File.separator + id);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
        try (Connection connection = pool.getConnection();
             PreparedStatement st = connection.prepareStatement("DELETE FROM candidates WHERE id =  ?")) {
            st.setInt(1, Integer.parseInt(id));
            st.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
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

//    @Override
//    public Map<Integer, String> findAllCountries() {
//        Map<Integer, String> countries = new LinkedHashMap<>();
//        try (Connection cn = pool.getConnection();
//             PreparedStatement ps = cn.prepareStatement("SELECT id, name FROM country ORDER BY id LIMIT 10")
//        ) {
//            try (ResultSet it = ps.executeQuery()) {
//                while (it.next()) {
//                    countries.put(it.getInt("id"), it.getString("name"));
//                }
//            }
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//        }
//        return countries;
//    }
//
//    @Override
//    public Map<Integer, String> findAllRegionsByCountryId(String countryId) {
//        Map<Integer, String> regions = new LinkedHashMap<>();
//        try (Connection cn = pool.getConnection();
//             PreparedStatement ps = cn.prepareStatement("SELECT * FROM region WHERE country_id = ? ORDER BY id LIMIT 10 ")
//        ) {
//            ps.setInt(1, Integer.parseInt(countryId));
//            ps.execute();
//            try (ResultSet it = ps.executeQuery()) {
//                while (it.next()) {
//                    regions.put(it.getInt("id"), it.getString("name"));
//                }
//            }
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//        }
//        return regions;
//    }
//
//    @Override
//    public Map<Integer, String> findAllCitiesById(String regionId) {
//        Map<Integer, String> cities = new LinkedHashMap<>();
//        try (Connection cn = pool.getConnection();
//             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city WHERE region_id = ? ORDER BY id LIMIT 10 ")
//        ) {
//            ps.setInt(1, Integer.parseInt(regionId));
//            ps.execute();
//            try (ResultSet it = ps.executeQuery()) {
//                while (it.next()) {
//                    cities.put(it.getInt("id"), it.getString("name"));
//                }
//            }
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//        }
//        return cities;
//    }
}
