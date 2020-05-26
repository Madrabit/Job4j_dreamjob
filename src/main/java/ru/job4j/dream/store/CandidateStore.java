package ru.job4j.dream.store;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.dream.model.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author madrabit on 25.05.2020
 * @version 1$
 * @since 0.1
 */
public class CandidateStore  {

    private static final Logger LOG = LogManager.getLogger(CandidateStore.class.getName());

    private static Store psqlStore;

    private CandidateStore() {
        psqlStore = PsqlStore.instOf();
    }

    private static final class Lazy {
        private static final CandidateStore INST = new CandidateStore();
    }

    public static CandidateStore instOf() {
        return CandidateStore.Lazy.INST;
    }

    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = psqlStore.getPool().getConnection();
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

    public Candidate findCandidateById(int id) {
        Candidate candidate = new Candidate(0, "", "", "", "", "", "", "", "");
        try (Connection cn = psqlStore.getPool().getConnection();
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

    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    private void createCandidate(Candidate candidate) {
        try (Connection cn = psqlStore.getPool().getConnection();
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

    private void updateCandidate(Candidate candidate) {
        try (Connection connection = psqlStore.getPool().getConnection();
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

    public Map<Integer, String> findAllCountries() {
        Map<Integer, String> countries = new LinkedHashMap<>();
        try (Connection cn = psqlStore.getPool().getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT id, name FROM country ORDER BY id LIMIT 10")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    countries.put(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return countries;
    }

    public Map<Integer, String> findAllRegionsByCountryId(String countryId) {
        Map<Integer, String> regions = new LinkedHashMap<>();
        try (Connection cn = psqlStore.getPool().getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM region WHERE country_id = ? ORDER BY id LIMIT 10 ")
        ) {
            ps.setInt(1, Integer.parseInt(countryId));
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    regions.put(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return regions;
    }

    public Map<Integer, String> findAllCitiesById(String regionId) {
        Map<Integer, String> cities = new LinkedHashMap<>();
        try (Connection cn = psqlStore.getPool().getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city WHERE region_id = ? ORDER BY id LIMIT 10 ")
        ) {
            ps.setInt(1, Integer.parseInt(regionId));
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.put(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return cities;
    }

}
