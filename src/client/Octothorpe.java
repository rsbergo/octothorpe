package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.gameclient.GameClient;

public class Octothorpe
{
    public static void main(String[] args)
    {
        try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)))
        {
            GameClient game = new GameClient();
            game.run("172.17.227.65", 7777, stdIn);
        }
        catch (IOException e)
        {
            System.err.println("It seems that something went wrong...");
            e.printStackTrace();
        }
    }
}
