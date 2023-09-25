package com.api.api2.dominio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger; // Importar la clase BigInteger

public class Sum extends Thread {
    private BigInteger valueInitial; // Cambiar a BigInteger
    private BigInteger valueEnd; // Cambiar a BigInteger
    private BigInteger resultSum; // Cambiar a BigInteger
    public static final String PATH = "resultados.txt";

    private long hours;
    private long minutes;
    private long seconds;

    public Sum(BigInteger valueInitial, BigInteger valueEnd, BigInteger resultSum, long hours, long min, long seg) {
        this.valueInitial = valueInitial;
        this.valueEnd = valueEnd;
        this.resultSum = resultSum;
        this.hours = hours;
        this.minutes = min;
        this.seconds = seg;
    }

    public BigInteger getValueInitial() {
        return valueInitial;
    }

    public void setValueInitial(BigInteger valueInitial) { // Cambiar el tipo de parámetro a BigInteger
        this.valueInitial = valueInitial;
    }

    public BigInteger getValueEnd() {
        return valueEnd;
    }

    public void setValueEnd(BigInteger valueEnd) { // Cambiar el tipo de parámetro a BigInteger
        this.valueEnd = valueEnd;
    }

    public BigInteger getResultSum() {
        return resultSum;
    }

    public void setResultSum(BigInteger resultSum) { // Cambiar el tipo de parámetro a BigInteger
        this.resultSum = resultSum;
    }

    public void run() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("resultados.txt", true)); // El 'true' indica que se agregará al archivo existente
            long startTime = System.currentTimeMillis();

            for (BigInteger i = valueInitial; i.compareTo(valueEnd) <= 0; i = i.add(BigInteger.ONE)) {
                BigInteger valueInitialAux = valueInitial.add(new BigInteger("1"));
                resultSum = calculatePartialSum(valueInitialAux, i, resultSum);
                if (i.mod(new BigInteger("100")).equals(BigInteger.ZERO)) {
                    long endTime = System.currentTimeMillis(); // Marca de tiempo después de escribir la línea
                    long elapsedTime = endTime - startTime; // Tiempo transcurrido en milisegundos
                    long seconds = ((elapsedTime / 1000) % 60) + this.seconds;
                    long minutes = ((elapsedTime / (1000 * 60)) % 60) + this.minutes;
                    long hours = ((elapsedTime / (1000 * 60 * 60)) % 24) + this.hours;
                    writer.write(valueInitial + "," + valueEnd + "," + i + "," + resultSum + "," + hours + "," + minutes + "," + seconds);
                    writer.newLine();
                    writer.flush(); // Guardar el resultado en el archivo
                }

                Thread.sleep(10);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private BigInteger calculatePartialSum(BigInteger initialValue, BigInteger finalValue, BigInteger resultSum) {
        this.resultSum = resultSum;

        for (BigInteger i = initialValue; i.compareTo(finalValue) <= 0; i = i.add(BigInteger.ONE)) {
            this.resultSum = resultSum.add(i);
        }

        return this.resultSum;
    }



    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }
}
