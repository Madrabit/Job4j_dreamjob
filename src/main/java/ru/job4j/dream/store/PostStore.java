package ru.job4j.dream.store;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.dream.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author madrabit on 29.05.2020
 * @version 1$
 * @since 0.1
 */
public class PostStore {
    private static final Logger LOG = LogManager.getLogger(PostStore.class.getName());

    private static Store psqlStore;

    private PostStore() {
        psqlStore = PsqlStore.instOf();
    }

    private static final class Lazy {
        private static final PostStore INST = new PostStore();
    }

    public static PostStore instOf() {
        return PostStore.Lazy.INST;
    }

    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = psqlStore.getPool().getConnection();
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

    public void savePost(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    private void createPost(Post post) {
        try (Connection cn = psqlStore.getPool().getConnection();
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
        try (Connection connection = psqlStore.getPool().getConnection();
             PreparedStatement st = connection.prepareStatement("UPDATE post SET name = ? WHERE id =  ?")) {
            st.setString(1, post.getName());
            st.setInt(3, post.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public Post findPostById(int id) {
        Post post = new Post(0, "");
        try (Connection cn = psqlStore.getPool().getConnection();
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
}
