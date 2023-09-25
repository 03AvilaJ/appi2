package com.api.api2.dominio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class ResultSumThread extends Thread {
    private BigInteger valueInitial;
    private BigInteger valueEnd;
    private String fileName;

    public ResultSumThread(BigInteger valueInitial, BigInteger valueEnd, String fileName) {
        this.valueInitial = valueInitial;
        this.valueEnd = valueEnd;
        this.fileName = fileName;
    }

    public void run() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            long startTime = System.currentTimeMillis();
            for (BigInteger i = valueInitial; i.compareTo(valueEnd) <= 0; i = i.add(BigInteger.ONE)) {
                BigInteger resultSum = calculatePartialSum(valueInitial, i);
                writer.write("Valor: " + i + ", Resultado parcial: " + resultSum);
                writer.newLine();
                writer.flush(); // Guardar el resultado en el archivo

                Thread.sleep(10); // Esperar 5 segundos y continuar
            }
            long endTime = System.currentTimeMillis();
            long elapsedTimeMillis = endTime - startTime;

            //this.seconds = (elapsedTimeMillis / 1000) % 60;
            //this.minutes = (elapsedTimeMillis / (1000 * 60)) % 60;
            //this.hours = (elapsedTimeMillis / (1000 * 60 * 60)) % 24;
            writer.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BigInteger calculatePartialSum(BigInteger initialValue, BigInteger finalValue) {
        BigInteger resultSum = BigInteger.ZERO;

        for (BigInteger i = initialValue; i.compareTo(finalValue) <= 0; i = i.add(BigInteger.ONE)) {
            resultSum = resultSum.add(i);
        }

        return resultSum;
    }

    public static void main(String[] args) {
        BigInteger initialValue = BigInteger.ONE;
        BigInteger finalValue = BigInteger.valueOf(2000); // Cambia esto según tus necesidades
        String fileName = "resultados.txt"; // Nombre del archivo de salida

        Thread resultThread = new ResultSumThread(initialValue, finalValue, fileName);
        resultThread.start();
    }
}
