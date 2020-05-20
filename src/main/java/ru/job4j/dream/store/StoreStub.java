package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.*;

/**
 * @author madrabit on 04.05.2020
 * @version 1$
 * @since 0.1
 */
public class StoreStub implements Store{
    private final Map<Integer, Post> postMap = new HashMap<>();
    private final Map<Integer, Candidate> candidateMap = new HashMap<>();
    private final Map<Integer, User> userMap = new HashMap<>();

    private int postIds = 0;
    private int candidatesIds = 0;
    private final int usersId = 0;


    @Override
    public Collection<Post> findAllPosts() {
        return new ArrayList<>(postMap.values());
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return new ArrayList<>(candidateMap.values());
    }

    @Override
    public void savePost(Post post) {
        post.setId(postIds++);
        this.postMap.put(post.getId(), post);
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        candidate.setId(this.candidatesIds++);
        this.candidateMap.put(candidate.getId(), candidate);
    }

    @Override
    public Candidate findCandidateById(int id) {
        return candidateMap.get(id);
    }

    @Override
    public Post findPostById(int id) {
        return postMap.get(id);
    }

    @Override
    public void deleteCandidate(String id) {
        candidateMap.remove(id);
    }

    @Override
    public void addUser(User user) {
        user.setId(postIds++);
        this.userMap.put(user.getId(), user);
    }

    @Override
    public User findByEmail(String name) {
        for (User user : userMap.values()) {
            if (name.equals(user.getEmail())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Map<Integer, String> findAllCountries() {
        return null;
    }

    @Override
    public Map<Integer, String> findAllRegionsByCountryId(String countryId) {
        return null;
    }

    @Override
    public Map<Integer, String> findAllCitiesById(String regionId) {
        return null;
    }
}
