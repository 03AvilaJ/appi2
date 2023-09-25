package com.api.api2.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import java.math.BigInteger; // Importa la clase BigInteger

@RestController
public class RegisterController {

    private static final String OWNER = "Emerson1";
    private static final String TOKEN = "202021852";

    @GetMapping("/register")
    public String register(){
        String responseBody = "";
        try {
            URL url = new URL("https://distri01.max-paloma-babbie.xyz/Master/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configura el método de solicitud como POST
            connection.setRequestMethod("POST");

            // Configura el encabezado "Content-Type" como "application/json"
            connection.setRequestProperty("Content-Type", "application/json");

            // Habilita la escritura de datos en la solicitud
            connection.setDoOutput(true);

            // Cuerpo de la solicitud en formato JSON
            String jsonInputString = "{\r\n  \"owner\": \"" + OWNER + "\",\r\n  \"token\": \"" + TOKEN + "\"\r\n}";
            //String jsonInputString = gson.toJson(userInfo);

            // Escribe el cuerpo de la solicitud en el flujo de salida
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BigInteger responseCode = BigInteger.valueOf(connection.getResponseCode()); // Cambiado a BigInteger

            // Leer la respuesta del servidor, si es necesario
            if (responseCode.equals(BigInteger.valueOf(HttpURLConnection.HTTP_OK)) || responseCode.equals(BigInteger.valueOf(202))) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    responseBody = response.toString();

                    System.out.println("Respuesta del servidor: " + responseBody);
                }
            } else {
                System.out.println("Error al obtener la respuesta del servidor.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @GetMapping("/delete")
    public String delete(){
        String responseBody = "";
        try {
            URL url = new URL("https://distri01.max-paloma-babbie.xyz/Master/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configura el método de solicitud como POST
            connection.setRequestMethod("DELETE");

            // Configura el encabezado "Content-Type" como "application/json"
            connection.setRequestProperty("Content-Type", "application/json");

            // Habilita la escritura de datos en la solicitud
            connection.setDoOutput(true);

            // Cuerpo de la solicitud en formato JSON
            String jsonInputString = "{\r\n  \"owner\": \"" + OWNER + "\",\r\n  \"token\": \"" + TOKEN + "\"\r\n}";
            //String jsonInputString = gson.toJson(userInfo);

            // Escribe el cuerpo de la solicitud en el flujo de salida
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BigInteger responseCode = BigInteger.valueOf(connection.getResponseCode()); // Cambiado a BigInteger


            // Leer la respuesta del servidor, si es necesario
            if (responseCode.equals(BigInteger.valueOf(HttpURLConnection.HTTP_OK)) || responseCode.equals(BigInteger.valueOf(202))) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    responseBody = response.toString();

                }
            } else {
                System.out.println("Error al obtener la respuesta del servidor.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @GetMapping("/listAll")
    public String listAll(){
        String responseBody = "";
        try {
            URL url = new URL("https://distri01.max-paloma-babbie.xyz/Master/listAll");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configura el método de solicitud como POST
            connection.setRequestMethod("GET");

            // Configura el encabezado "Content-Type" como "application/json"
            connection.setRequestProperty("Content-Type", "application/json");

            BigInteger responseCode = BigInteger.valueOf(connection.getResponseCode()); // Cambiado a BigInteger


            // Leer la respuesta del servidor, si es necesario
            if (responseCode.equals(BigInteger.valueOf(HttpURLConnection.HTTP_OK)) || responseCode.equals(BigInteger.valueOf(202))) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    responseBody = response.toString();
                }
            } else {
                System.out.println("Error al obtener la respuesta del servidor.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @GetMapping("/owner")
    public String getOneOwner(){
        String responseBody = "";
        try {
            URL url = new URL("https://distri01.max-paloma-babbie.xyz/Master/task/Emerson");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configura el método de solicitud como POST
            connection.setRequestMethod("GET");

            // Configura el encabezado "Content-Type" como "application/json"
            connection.setRequestProperty("Content-Type", "application/json");

            BigInteger responseCode = BigInteger.valueOf(connection.getResponseCode()); // Cambiado a BigInteger

            // Leer la respuesta del servidor, si es necesario
            if (responseCode.equals(BigInteger.valueOf(HttpURLConnection.HTTP_OK)) || responseCode.equals(BigInteger.valueOf(202))) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    responseBody = response.toString();

                }
            } else {
                System.out.println("Error al obtener la respuesta del servidor.");
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }


}
