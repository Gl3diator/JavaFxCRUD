package com.esprit.studentcrud.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EmailJsService {

    private final HttpClient client = HttpClient.newHttpClient();

    private final String serviceId;
    private final String templateId;
    private final String publicKey;
    private final String privateKey; // optional

    public EmailJsService() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        this.serviceId  = dotenv.get("EMAILJS_SERVICE_ID");
        this.templateId = dotenv.get("EMAILJS_TEMPLATE_ID");
        this.publicKey  = dotenv.get("EMAILJS_PUBLIC_KEY");
        this.privateKey = dotenv.get("EMAILJS_PRIVATE_KEY"); // can be null/empty
    }

    public void sendStudentAdded(String name, int age, String email) {
        // JSON body for EmailJS
        String json = "{"
                + "\"service_id\":\"" + escape(serviceId) + "\","
                + "\"template_id\":\"" + escape(templateId) + "\","
                + "\"user_id\":\"" + escape(publicKey) + "\","
                + (privateKey != null && !privateKey.isBlank()
                ? "\"accessToken\":\"" + escape(privateKey) + "\","
                : "")
                + "\"template_params\":{"
                + "\"student_name\":\"" + escape(name) + "\","
                + "\"student_age\":\"" + age + "\","
                + "\"student_email\":\"" + escape(email) + "\""
                + "}"
                + "}";

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.emailjs.com/api/v1.0/email/send"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // async so UI doesn't freeze
        client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    if (res.statusCode() == 200) {
                        System.out.println("✅ EmailJS: email sent");
                    } else {
                        System.out.println("❌ EmailJS failed: " + res.statusCode());
                        System.out.println(res.body());
                    }
                })
                .exceptionally(ex -> {
                    System.out.println("❌ EmailJS error: " + ex.getMessage());
                    return null;
                });
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
