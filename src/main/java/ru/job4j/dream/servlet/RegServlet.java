package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author madrabit on 01.05.2020
 * @version 1$
 * @since 0.1
 */
public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("posts.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        PsqlStore.instOf().addUser(
                new User(
                        0,
                        req.getParameter("name"),
                        req.getParameter("email"),
                        req.getParameter("password")
                )
        );
        HttpSession sc = req.getSession();
        User user = new User();
        user.setName(req.getParameter("name"));
        user.setEmail(req.getParameter("email"));
        sc.setAttribute("user", user);
        resp.sendRedirect(req.getContextPath() + "/posts.do");
//        resp.sendRedirect(req.getContextPath() + "/reg.do");
    }
}
