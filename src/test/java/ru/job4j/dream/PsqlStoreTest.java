package ru.job4j.dream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.servlet.PostServlet;
import ru.job4j.dream.servlet.RegServlet;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.StoreStub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author madrabit on 03.05.2020
 * @version 1$
 * @since 0.1
 */

@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.dom.*", "javax.management.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({PsqlStore.class})
public class PsqlStoreTest {
    @Test
    public void whenAddPostThenStoreIt() throws IOException {
        Store store = new StoreStub();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("Vasya");
        new PostServlet().doPost(req, resp);
        List<Post> postList = new ArrayList<>(PsqlStore.instOf().findAllPosts());
        assertThat(postList.get(0).getName(), is("Vasya"));
        assertThat(PsqlStore.instOf().findPostById(0).getName(), is("Vasya"));
    }

    @Test
    public void whenAddUserThenStoreIt() throws IOException {
        Store store = new StoreStub();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("name")).thenReturn("Vasya");
        when(req.getParameter("email")).thenReturn("vasya@gmail.com");
        when(req.getParameter("password")).thenReturn("pass");
        HttpSession session = mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute("name")).thenReturn("Vasya");
        when(req.getSession().getAttribute("email")).thenReturn("vasya@gmail.com");
        new RegServlet().doPost(req, resp);
        assertThat(PsqlStore.instOf().findByEmail("vasya@gmail.com").getName(), is("Vasya"));
    }
}
