package ru.job4j.dream.servlet;

import com.google.gson.Gson;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * @author madrabit on 12.05.2020
 * @version 1$
 * @since 0.1
 */
public class GreetingServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println("Nice to meet you, " + name);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String jb;
        try (BufferedReader reader = req.getReader()) {
            jb = reader.lines().collect(Collectors.joining());
        }
        JSONObject jObj = new JSONObject(jb);
        String text = "Nice to meet you, " + jObj.get("text");
        JsonObj jsonObj = new JsonObj(text);
        String jsonString = this.gson.toJson(jsonObj);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    public static class JsonObj {
        private final String text;

        public JsonObj(String text) {
            this.text = text;
        }
    }
}
