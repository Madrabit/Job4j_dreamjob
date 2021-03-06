package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author madrabit on 18.04.2020
 * @version 1$
 * @since 0.1
 */
public class MemStore implements Store {
    private static final MemStore INST = new MemStore();
    private static final AtomicInteger POST_ID = new AtomicInteger(4);
    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(4);


    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public MemStore() {
//        posts.put(1, new Post(1, "Junior Java Job", "Some text 1"));
//        posts.put(2, new Post(2, "Middle Java Job", "Some text 2"));
//        posts.put(3, new Post(3, "Senior Java Job", "Some text 3"));
//        candidates.put(1, new Candidate(1, "Junior Java", ""));
//        candidates.put(2, new Candidate(2, "Middle Java", ""));
//        candidates.put(3, new Candidate(3, "Senior Java", ""));
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

//    @Override
//    public Collection <Candidate> findAllCandidates() {
//        return candidates.values();
//    }

    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }


    public Post findPostById(int id) {
        return posts.get(id);
    }

//    @Override
//    public void saveCandidate(Candidate candidate) {
//        if (candidate.getId() == 0) {
//            candidate.setId(CANDIDATE_ID.incrementAndGet());
//        }
//        candidates.put(candidate.getId(), candidate);
//    }

    public void deleteCandidate(String id) {
        Candidate candidate = candidates.get(Integer.parseInt(id));
        if (!candidate.getPhotoId().isEmpty()) {
            File file = new File("images" + File.separator + candidate.getPhotoId());
            file.delete();
        }
        candidates.remove(Integer.parseInt(id));
    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public User findByEmail(String name) {
        return null;
    }

//    @Override
//    public Candidate findCandidateById(int id) {
//        return candidates.get(id);
//    }
//
//    @Override
//    public Map<Integer, String> findAllCountries() {
//        return null;
//    }
//
//    @Override
//    public Map<Integer, String> findAllRegionsByCountryId(String countryId) {
//        return null;
//    }
//
//    @Override
//    public Map<Integer, String> findAllCitiesById(String regionId) {
//        return null;
//    }

    @Override
    public BasicDataSource getPool() {
        return null;
    }
}
