package com.conversor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

public class Conversor {

    public static void main(String[] args) throws IOException {

    boolean running = true;

    
        
        do{
            // HashMap para armazenar os códigos das moedas
            HashMap<Integer, String> currencyCode = new HashMap<Integer, String>();
            // Adicionando códigos de moedas
            currencyCode.put(1, "ARS");
            currencyCode.put(2, "BOB");
            currencyCode.put(3, "BRL");
            currencyCode.put(4, "CLP");
            currencyCode.put(5, "COP");
            currencyCode.put(6, "USD");

            Integer from, to;
            String fromCode, toCode;
            double amount;
            Scanner sc = new Scanner(System.in);

            // Moeda de origem
            System.out.println("\nDigite o código da moeda de origem: ");
            System.out.println("1 - ARS \n2 - BOB \n3 - BRL \n4 - CLP \n5 - COP \n6 - USD");
            from = sc.nextInt();
            while (from < 1 || from > 6) {
                System.out.println("\nCódigo inválido, digite novamente: ");
                System.out.println("\nDigite o código da moeda de origem: ");
                System.out.println("1 - ARS \n2 - BOB \n3 - BRL \n4 - CLP \n5 - COP \n6 - USD");
                from = sc.nextInt();
            }
            fromCode = currencyCode.get(from);

            // Moeda de destino
            System.out.println("\nDigite o código da moeda de destino: ");
            System.out.println("1 - ARS \n2 - BOB \n3 - BRL \n4 - CLP \n5 - COP \n6 - USD");
            to = sc.nextInt();
            while (to < 1 || to > 6) {
                System.out.println("\nCódigo inválido, digite novamente: ");
                System.out.println("\nDigite o código da moeda de destino: ");
                System.out.println("1 - ARS \n2 - BOB \n3 - BRL \n4 - CLP \n5 - COP \n6 - USD");
                to = sc.nextInt();
            }
            toCode = currencyCode.get(to);

            // Valor a ser convertido
            System.out.println("\nDigite o valor a ser convertido: ");
            amount = sc.nextDouble();

            // Chamando o método para enviar a requisição HTTP
            sendHttpRequest(fromCode, toCode, amount);

            // Verificando se o usuário deseja realizar outra conversão
            System.out.println("\nDeseja realizar outra conversão? (S/N)");
            String option = sc.next();
            if(option.equalsIgnoreCase("N")) {
                running = false;
            }
        }while(running);

        System.out.println("\nFim da execução");

    }

    private static void sendHttpRequest(String fromCode, String toCode, double amount) throws IOException {
        
        DecimalFormat df = new DecimalFormat("00.00");
        String apiKey = "cc9d6600973750676320936d";

        String GET_URL = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + fromCode + "/"+ toCode + "/" + amount;
        URL url = new URL(GET_URL);
        System.out.println(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK) {//sucesso
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }in.close();
            
            JSONObject obj = new JSONObject(response.toString());
            //Double exchengeRate = obj.getDouble("conversion_rate");
            Double result = obj.getDouble("conversion_result");

            System.out.println("Taxa de conversão "+ df.format(obj.getDouble("conversion_rate")));//imprime o objeto json
            //System.out.println(exchengeRate);//imprime a taxa de conversão
            System.out.println("\n" + df.format(amount) + " " + fromCode + " = " + df.format(result) + " " + toCode);
            
        } else {
            System.out.println("\nGET request not worked");
        }
    }

    // Método para enviar requisição HTTP

}