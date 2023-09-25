package com.api.api2.controller;

import com.api.api2.dominio.Sum;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.math.BigInteger; // Importar la clase BigInteger
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class SumControler {

    private static final String OWNER = "Emerson1";
    private static final String TOKEN = "202021852";
    private String status = "AVAILABLE";
    private BigInteger valueInitial;
    private BigInteger valueEnd;
    public static final String PATH = "resultados.txt";
    private final Thread periodicThread = new Thread(this::periodicStatus);
    private final Thread periodicTask = new Thread(this::periodicTask);

    public SumControler() {
        periodicThread.start();
        periodicTask.start();
    }

    @GetMapping("/task/personal")
    public String getNumbers() {
        status = "WORKING";
        String responseBody = "";
        try {
            URL url = new URL("https://distri01.max-paloma-babbie.xyz/Master/task/personal");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configura el método de solicitud como POST
            connection.setRequestMethod("POST");

            // Configura el encabezado "Content-Type" como "application/json"
            connection.setRequestProperty("Content-Type", "application/json");

            // Habilita la escritura de datos en la solicitud
            connection.setDoOutput(true);

            // Cuerpo de la solicitud en formato JSON
            BigInteger numInitial = BigInteger.ZERO; // Cambiar a BigInteger
            BigInteger numFinal = new BigInteger("3000"); // Cambiar a BigInteger

            String jsonInputString = "{\r\n  \"valueInitial\": \"" + numInitial + "\",\r\n  \"valueEnd\": \"" + numFinal + "\" ,\r\n  \"token\": \"" + TOKEN + "\",\r\n  \"owner\": \"" + OWNER + "\"\r\n}";

            // Escribe el cuerpo de la solicitud en el flujo de salida
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == 202) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    responseBody = response.toString();
                    getValues(responseBody);
                }
            } else {
                responseBody = "Error al obtener la respuesta del servidor.";
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    @GetMapping("/task/result")
    public String showResult() {

        String responseBody = "";
        String jsonInputString = "";
        try {
            URL url = new URL("https://distri01.max-paloma-babbie.xyz/Master/task/result");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);

            long startTime = System.currentTimeMillis();
            String lastNumbers = readValues();

            String[] lastNumberSplit = lastNumbers.split(",");
            BigInteger initial = BigInteger.valueOf(Long.parseLong(lastNumberSplit[2]));
            BigInteger end = BigInteger.valueOf(Long.parseLong(lastNumberSplit[1]));
            BigInteger result = BigInteger.valueOf(Long.parseLong(lastNumberSplit[3]));
            long hr = Long.parseLong(lastNumberSplit[4]);
            long min = Long.parseLong(lastNumberSplit[5]);
            long seg = Long.parseLong(lastNumberSplit[6]);
            Thread sum = new Sum(initial,end,result,hr,min,seg);
            sum.start();

            try {
                sum.join();
                String text = readTxt();
                String[] textSplit = text.split(",");
                //System.out.println(textSplit[0]);
                BigInteger numInitial = BigInteger.valueOf(Long.parseLong(textSplit[0]));
                BigInteger numFinal = BigInteger.valueOf(Long.parseLong(textSplit[1]));
                BigInteger numResult = BigInteger.valueOf(Long.parseLong(textSplit[3]));
                long endTime = System.currentTimeMillis();
                long elapsedTimeMillis = endTime - startTime;

                long seconds = Long.parseLong(textSplit[6]);
                long minutes = Long.parseLong(textSplit[5]);
                long hours = Long.parseLong(textSplit[4]);
                jsonInputString = "{\r\n  \"owner\": \"" + OWNER + "\",\r\n  \"token\": \"" + TOKEN + "\",\r\n  \"valueInitial\": " + numInitial + ",\r\n  \"valueEnd\": " + numFinal + ",\r\n  \"result\": " + numResult + ",\r\n  \"processingTime\": {\r\n    \"horas\": "+hours+",\r\n    \"minutos\": "+minutes+",\r\n    \"segundos\": "+seconds+"\r\n  }\r\n}";
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Ahora puedes usar los valores obtenidos en tu código

                // Limpiar el contenido del archivo resultados.txt
                try (FileWriter fileWriter = new FileWriter("resultados.txt", false)) {
                    // Este uso de FileWriter con el segundo argumento 'false' sobrescribe el contenido existente
                    fileWriter.write("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == 202) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    responseBody = response.toString();
                }
            } else {
                responseBody = "Error al obtener la respuesta del servidor.";
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        status = "AVAILABLE";
        return responseBody;
    }

    private void getValues(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        valueInitial = new BigInteger(jsonObject.get("valueInitial").getAsString());
        valueEnd = new BigInteger(jsonObject.get("valueEnd").getAsString());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultados.txt",true))) {
            writer.write(valueInitial + "," + valueEnd + "," + valueInitial + "," + 0 + "," + 0 + "," + 0 + "," + 0);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readValues(){
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                lines.add(linea);
            }

            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        int size = lines.size()-1;
        return lines.get(size);
    }

    public void periodicStatus() {
        while (true) {
            try {
                Thread.sleep(90 * 1000); // Esperar 90 segundos (1.5 minutos) antes de la siguiente ejecución
                status();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void periodicTask() {
        while (true) {
            try {
                if(status.compareToIgnoreCase("WORKING")==0){
                    Thread.sleep(120 * 1000);
                }else{
                    getNumbers();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private void status() {
        String responseBody = "";
        try {
            URL url = new URL("https://distri01.max-paloma-babbie.xyz/Master/register/status");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\r\n \"token\": \"" + TOKEN + "\",\r\n  \"status\": \"" + status + "\",\r\n  \"owner\": \"" + OWNER + "\"\r\n}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == 202 || responseCode == 400) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    responseBody = response.toString();
                    //getValues(responseBody);
                }
            } else {
                responseBody = "Error al obtener la respuesta del servidor.";
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readTxt(){
        String text = "";
        try {
            FileReader file = new FileReader(PATH);
            BufferedReader read = new BufferedReader(file);

            String line;
            ArrayList<String> lines =  new ArrayList<>();
            while ((line = read.readLine()) != null) {
                lines.add(line);
            }
            int size = lines.size()-1;
            text = lines.get(size);
            read.close();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return text;
    }
}
