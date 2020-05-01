package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.Collection;

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
}
