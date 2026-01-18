package com.esprit.studentcrud.util;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class WhatsAppService {

    // TODO: put these in config (or env vars) later, not hardcoded
    private static final String PHONE = "21624664938";   // your number
    private static final String APIKEY = "7781890";      // your key

    private final HttpClient client = HttpClient.newHttpClient();

    public void sendStudentAdded(String name, int age, String email) {
        String msg = "✅ New student added:\n"
                + "Name: " + name + "\n"
                + "Age: " + age + "\n"
                + "Email: " + email;

        send(msg);
    }

    public void send(String message) {
        try {
            String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "https://api.callmebot.com/whatsapp.php"
                    + "?phone=" + PHONE
                    + "&text=" + encoded
                    + "&apikey=" + APIKEY;

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // async => doesn't freeze JavaFX UI
            client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(res -> {
                        if (res.statusCode() == 200) {
                            System.out.println("✅ WhatsApp sent!");
                        } else {
                            System.out.println("❌ WhatsApp failed: " + res.statusCode());
                            System.out.println(res.body());
                        }
                    })
                    .exceptionally(ex -> {
                        System.out.println("❌ WhatsApp error: " + ex.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
