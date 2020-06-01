package ru.job4j.dream.servlet;

import com.google.gson.Gson;
import org.json.JSONObject;
import ru.job4j.dream.store.CandidateStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author madrabit on 18.05.2020
 * @version 1$
 * @since 0.1
 */
public class CitiesAjax extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Map<Integer, String> countriesList = CandidateStore.instOf().findAllCountries();
        Map<Integer, String> regionsList = null;
        Map<Integer, String> cityList = null;
        String jb;
        try (BufferedReader reader = req.getReader()) {
            jb = reader.lines().collect(Collectors.joining());
        }
        if (!jb.isEmpty()) {
            JSONObject jObj = new JSONObject(jb);
            String countryId = (String) jObj.get("country");
            if (countryId != null) {
                regionsList = CandidateStore.instOf().findAllRegionsByCountryId(countryId);
            }
            if (jObj.has("region")) {
                String regionId = (String) jObj.get("region");
                cityList = CandidateStore.instOf().findAllCitiesById(regionId);
            }
        }
        CitiesAjax.JsonObj jsonObj = new CitiesAjax.JsonObj(countriesList, regionsList, cityList);
        String jsonString = this.gson.toJson(jsonObj);
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(jsonString);
        out.flush();
    }

    public static class JsonObj {
        private final Map<Integer, String> country;
        private final Map<Integer, String> regions;
        private Map<Integer, String> cities;


        public JsonObj(Map<Integer, String> country, Map<Integer, String> regions) {
            this.country = country;
            this.regions = regions;
        }

        public JsonObj(Map<Integer, String> country, Map<Integer, String> regions, Map<Integer, String> cities) {
            this.country = country;
            this.regions = regions;
            this.cities = cities;
        }
    }
}
