package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author madrabit on 18.04.2020
 * @version 1$
 * @since 0.1
 */
public class Store {
    private static final Store INST = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Some text 1"));
        posts.put(2, new Post(2, "Middle Java Job", "Some text 2"));
        posts.put(3, new Post(3, "Senior Java Job", "Some text 3"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}
