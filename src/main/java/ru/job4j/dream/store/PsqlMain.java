package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;


import java.util.Map;

/**
 * @author madrabit on 30.04.2020
 * @version 1$
 * @since 0.1
 */
public class PsqlMain {


    public static void main(String[] args) {


        Store store = PsqlStore.instOf();

        Map<Integer, String> map = store.findAllCountries();
        map.forEach((key, value) -> System.out.println(key + " : " + value));

        store.savePost(new Post(0, "Java Job"));
        store.saveCandidate(new Candidate(0, "First candid", "Last name", "Man", "Some text", "", "", "", "img"));
        Candidate candidate = store.findCandidateById(1);
        System.out.println(candidate.getId() + " " + candidate.getName());
        store.saveCandidate(new Candidate(1, "Updated candid", "Last name", "Man", "Some text", "", "", "", "img"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        for (Candidate can : store.findAllCandidates()) {
            System.out.println(can.getId() + " " + can.getName() + " " + can.getPhotoId());
        }
        store.addUser(new User(1, "Vova", "aqw@123", "123123"));
        User user = store.findByEmail("aqw@123");
        System.out.println(String.format("%s %s %s %s", user.getId(), user.getName(), user.getEmail(), user.getPassword()));
    }
}
