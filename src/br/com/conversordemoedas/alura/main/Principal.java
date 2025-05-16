package br.com.conversordemoedas.alura.main;

import br.com.conversordemoedas.alura.DTO.Conversao;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.net.URI;

public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {
        Map<String, String[]> map = new HashMap<>();
        Scanner pesquisa = new Scanner(System.in);
        String menu = "";

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setPrettyPrinting()
                .create();
        map.put("1", new String[]{"USD", "BRL"});
        map.put("2", new String[]{"BRL", "USD"});
        map.put("3", new String[]{"CAD", "BRL"});
        map.put("4", new String[]{"EUR", "USD"});
        map.put("5", new String[]{"GBP", "EUR"});
        map.put("6", new String[]{"EUR", "BRL"});


        while (!menu.equalsIgnoreCase("7")) {

            System.out.println("Qual conversão deseja fazer?");
            System.out.println("1- USD -> BRL");
            System.out.println("2- BRL -> USD");
            System.out.println("3- CAD -> BRL");
            System.out.println("4- EUR -> USD");
            System.out.println("5- GBP -> EUR");
            System.out.println("6- EUR -> BRL");
            System.out.println("7 - SAIR");
            menu = pesquisa.nextLine();

             if (Objects.equals(menu, "7")){
                System.out.println("Saindo...");
                break;
            }


            String[] moedas = map.get(menu);

            if (moedas == null ) {
                System.out.println("Opção inválida");
            }

            HttpClient client = HttpClient.newHttpClient();

            try {
                String urlApi = "https://v6.exchangerate-api.com/v6/e2d4914704ddc5e77e8d4d05/pair/" + moedas[0] + "/" + moedas[1];
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlApi))
                        .build();

                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());


                String conversao = response.body();

                Conversao taxa = gson.fromJson(conversao, Conversao.class);
                System.out.println("A taxa de rate é: " + taxa.conversion_rate());

                System.out.println("Qual valor deseja converter ?");
                double valor = Double.parseDouble(pesquisa.nextLine());

                double resultado = valor * taxa.conversion_rate();
                System.out.println("O resultado de " + valor + " em " + moedas[0] + " é " + resultado + " de " + moedas[1] );

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }

    }
}