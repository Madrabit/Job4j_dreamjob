package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

/**
 * @author madrabit on 30.04.2020
 * @version 1$
 * @since 0.1
 */
public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.savePost(new Post(0, "Java Job"));
        store.saveCandidate(new Candidate(0, "First candid", "img"));
        Candidate candidate = store.findCandidateById(1);
        System.out.println(candidate.getId() + " " + candidate.getName());
        store.saveCandidate(new Candidate(1, "Updated candid", "img"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        for (Candidate can : store.findAllCandidates()) {
            System.out.println(can.getId() + " " + can.getName() + " " + can.getPhotoId());
        }
    }
}
