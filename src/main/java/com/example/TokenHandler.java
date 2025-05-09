package main.java.com.example;

import com.google.gson.Gson;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;

public class TokenHandler extends HttpServlet {
    private final Gson gson = new Gson();
    private final Map<String, Integer> wordToId = new HashMap<>();

    public TokenHandler() {
        loadTokenMap();
    }

    private void loadTokenMap() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tokens.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String word = parts[0].trim();
                    int id = Integer.parseInt(parts[1].trim());
                    wordToId.put(word, id);
                }
            }
        } catch (IOException e) {
            System.err.println("토큰 파일 로딩 실패: " + e.getMessage());
        }
    }

    private List<Integer> mapTokens(String sentence) {
        String[] tokens = sentence.trim().split("\\s+");
        List<Integer> ids = new ArrayList<>();
        for (String token : tokens) {
            ids.add(wordToId.getOrDefault(token, 0));
        }
        return ids;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            TokenRequest request = gson.fromJson(sb.toString(), TokenRequest.class);

            List<Integer> ids = mapTokens(request.sentence);
            TokenResponse response = new TokenResponse(ids);

            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().write(gson.toJson(response));
        } catch (Exception e) {
            resp.setStatus(500);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            resp.getWriter().write(gson.toJson(error));
        }
    }
}