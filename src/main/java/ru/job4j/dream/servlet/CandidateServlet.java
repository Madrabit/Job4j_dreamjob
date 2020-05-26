package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.CandidateStore;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author madrabit on 22.04.2020
 * @version 1$
 * @since 0.1
 */
public class CandidateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        req.setAttribute("candidates", CandidateStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        File file;
        String name = "";
        String lastName = "";
        String country = "";
        String region = "";
        String city = "";
        String sex = "";
        String description = "";
        String photoId = "";
        try {
            List<FileItem> items = upload.parseRequest(req);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    if ("name".equals(item.getFieldName())) {
                        fieldNotEmpty(item, resp);
                        name = item.getString("UTF-8");
                    } else if ("lastName".equals(item.getFieldName())) {
                        fieldNotEmpty(item, resp);
                        lastName = item.getString("UTF-8");
                    } else if ("country".equals(item.getFieldName())) {
                        country = item.getString("UTF-8");
                    } else if ("state".equals(item.getFieldName())) {
                        region = item.getString("UTF-8");
                    } else if ("district".equals(item.getFieldName())) {
                        city = item.getString("UTF-8");
                    } else if ("sex".equals(item.getFieldName())) {
                        sex = item.getString("UTF-8");
                    } else if ("description".equals(item.getFieldName())) {
                        description = item.getString("UTF-8");
                    }
                } else if (!item.isFormField() && item.getSize() > 0) {
                    File folder = new File("images");
                    if (!folder.exists()) {
                        folder.mkdir();
                    }

                    file = new File(folder + File.separator + item.getName());
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                    }
                    photoId = item.getName();
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        CandidateStore.instOf().saveCandidate(
                new Candidate(
                        Integer.parseInt(req.getParameter("id")),
                        name,
                        lastName,
                        country,
                        region,
                        city,
                        sex,
                        description,
                        photoId
                )
        );
        doGet(req, resp);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }

    private void fieldNotEmpty(FileItem item, HttpServletResponse resp) throws IOException {
        if (item == null || item.getString().isEmpty())
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
