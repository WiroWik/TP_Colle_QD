package com.leon.colle;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestNGSpring {
    // Client HTTP partagé
    private HttpClient client;

    // URL de base de l'API
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeClass
    public void setup() {
        // Initialisation de HttpClient
        client = HttpClient.newHttpClient();
    }

    // Test GET - Vérifie si la liste des ressources est retournée
    @Test
    public void testGetAllResources() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/client"))  // Modifie selon le chemin de ton entité
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Vérification du code HTTP
        Assert.assertEquals(response.statusCode(), 200);

        // Vérification de la présence d'un champ spécifique dans le corps de la réponse
        String responseBody = response.body();
        Assert.assertTrue(responseBody.contains("firstName"), "La réponse doit contenir un champ 'firstName'");
    }

    // Test POST - Crée une nouvelle ressource
    @Test
    public void testCreateResource() throws Exception {
        String jsonBody = "{\"name\":\"Nouvelle Ressource\", \"description\":\"Une description\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/client"))  // Modifie selon le chemin de ton entité
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Vérification du statut HTTP
        Assert.assertEquals(response.statusCode(), 201);

        // Vérification de la présence de l'en-tête Location
        String locationHeader = response.headers().firstValue("Location").orElse(null);
        Assert.assertNotNull(locationHeader, "L'en-tête Location doit être présent");
    }
}
