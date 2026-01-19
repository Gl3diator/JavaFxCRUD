package com.esprit.studentcrud.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class WhatsAppService {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String PHONE = dotenv.get("WHATSAPP_PHONE");
    private static final String API_KEY = dotenv.get("WHATSAPP_API_KEY");

    private final HttpClient client = HttpClient.newHttpClient();

    public void sendStudentAdded(String name, int age, String email) {
        String msg = """
                ✅ New student added:
                Name: %s
                Age: %d
                Email: %s
                """.formatted(name, age, email);

        send(msg);
    }

    private void send(String message) {
        try {
            String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "https://api.callmebot.com/whatsapp.php"
                    + "?phone=" + PHONE
                    + "&text=" + encoded
                    + "&apikey=" + API_KEY;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(res -> {
                        if (res.statusCode() == 200) {
                            System.out.println("✅ WhatsApp message sent");
                        } else {
                            System.out.println("❌ WhatsApp error: " + res.statusCode());
                        }
                    })
                    .exceptionally(ex -> {
                        System.out.println("❌ WhatsApp exception: " + ex.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
