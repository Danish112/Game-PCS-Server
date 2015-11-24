package com.company;

import Cards.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class GameServer {

    //Global variables
    static ArrayList<RoleCard> roles;
    static LobbyStatus lobbyStatus;
    static int port;
    static int playerIDs;
    static ArrayList<Socket> connectionArray;
    static ArrayList<String> currentUsers;
    static GameBoard test1;
    static PrintWriter output;

    //--------------------------------------------------------------

    GameServer()
    {

    }

    //--------------------------------------------------------------

    public static void main(String[] args)
    {
        port = 2555;

        instantiateRoleCards();
        InLobby();

    }

    public static void InLobby()
    {
        playerIDs = 1;
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            //as long as all players are not ready (true) stay in loop
            while (!lobbyStatus.allReady) {
                //as long as there is less than 4 players connected stay in loop
                while (connectionArray.size() <= 3) {
                    Socket newPlayerSocket = serverSocket.accept();
                    connectionArray.add(newPlayerSocket);

                    System.out.println("Client has connected to server");

                    int randomIndex = new Random().nextInt(roles.size());
                    RoleCard tempRole = roles.get(randomIndex);
                    roles.remove(randomIndex);
                    Player tempPlayer = new Player(tempRole,playerIDs, GameBoard.gameBoard.getCity("atlanta"));

                    ClientConnection clientConnect = new ClientConnection(newPlayerSocket, tempPlayer, lobbyStatus);
                    Thread newClient = new Thread(clientConnect);
                    newClient.start();
                    playerIDs++;
                }
                Thread.sleep(100);
            }
        }
        catch (IOException e) {
            System.out.println("Failed to make server socket");
        } catch (InterruptedException e) {
            System.out.println("failed to sleep while waiting for all players to press ready");
        }
    }

    public static void sendLobbyChanges()
    {
        if(lobbyStatus.changeInStatus == true)
        {
                //FOR LOOP THAT RUNS THROUGH ALL SOCKETS AND SENDS THE READY STATUS OF ALL PLAYERS

            lobbyStatus.changeInStatus = false;
        }
    }


    public static void instantiateRoleCards()
    {
        RoleCard operationsExpert = new RoleCard("operations expert");
        roles.add(operationsExpert);
        RoleCard quarantineSpecialist  = new RoleCard("quarantine specialist");
        roles.add(quarantineSpecialist);
        RoleCard medic  = new RoleCard("medic");
        roles.add(medic);
        RoleCard scientist  = new RoleCard("scientist");
        roles.add(scientist);
    }

    public static void gameRunning()
    {
        //code to check if win or lose by looking in gameboard class
    }
}
