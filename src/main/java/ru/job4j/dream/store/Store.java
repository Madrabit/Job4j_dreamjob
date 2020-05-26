package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;
import java.util.Map;

/**
 * @author madrabit on 30.04.2020
 * @version 1$
 * @since 0.1
 */
public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Candidate findCandidateById(int id);

    Post findPostById(int id);

    void deleteCandidate(String id);

    void addUser(User user);

    User findByEmail(String name);

    BasicDataSource getPool();
//
//    Map<Integer, String>  findAllCountries();
//
//    Map<Integer, String>  findAllRegionsByCountryId(String countryId);
//
//    Map<Integer, String> findAllCitiesById(String regionId);
}
