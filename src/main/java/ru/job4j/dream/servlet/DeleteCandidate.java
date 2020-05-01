package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author madrabit on 26.04.2020
 * @version 1$
 * @since 0.1
 */
public class DeleteCandidate extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PsqlStore.instOf().deleteCandidate(req.getParameter("id"));
        req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//        Store.instOf().deleteCandidate(req.getParameter("id"));
//        doGet(req, resp);
//        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
