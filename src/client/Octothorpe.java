package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.connector.Connector;
import client.connector.Request;
import client.connector.Response;

public class Octothorpe
{
    public static void main(String[] args)
    {
        try (Connector conn = new Connector();
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)))
        {
            conn.connectTo("172.17.227.65", 7777);
            String userInput;
            while ((userInput = stdIn.readLine()) != null)
            {
                Request request = new Request(userInput);
                conn.send(request);
                Response response;
                while ((response = conn.receive()) != null)
                {
                    if (response.getResponseCode().getCode() >= 200)
                    {
                        System.out.println(response);
                        break;
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("It seems that something went wrong...");
            e.printStackTrace();
        }
    }
}
