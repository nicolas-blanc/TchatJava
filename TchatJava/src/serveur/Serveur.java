/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author blancn
 */
public class Serveur {
    private final Integer port;
    private ServerSocket socket_ecoute;
    private Socket socket_transfert;
    private LinkedBlockingQueue listThread; // a modif -> voir blockingqueue ou autre ==> ne gere l'exclusion mutuelle

    public static void main (String[] args) {
        if(args.length == 1) {
            if(Integer.parseInt(args[0]) > 50000 && Integer.parseInt(args[0]) < 60000) {
                Serveur serveur = new Serveur(Integer.parseInt(args[0]));
                serveur.attenteClient();
                System.out.println("Extinction du serveur");
            } else System.out.println("Erreur de port entre 50000 et 60000");
        } else System.out.println("Erreur d'arguments [port 50000->60000]");
    }

    public Serveur(Integer p) {
        port = p;
        listThread = new LinkedBlockingQueue();
        ouvrirEcoute();
    }

    private void ouvrirEcoute() {
        try {
            socket_ecoute = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void ouvrirTransfert() {
        try {
            socket_transfert = socket_ecoute.accept(); //a remettre dans serveur
            System.out.println("Serveur Ok");
        } catch (IOException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void attenteClient() {
        while (true) {
            ouvrirTransfert();
            TraitementClient tc = new TraitementClient(this,socket_transfert);
            tc.start();
        }
    }
    
    public void addListThread(TraitementClient tc) {
        listThread.add(tc);
    }
    
    public void delListThread(TraitementClient tc) {
        
    }
    
    public void renvoi(String Mss) {
        
    }
    
    public void renvoi(/*Fichier*/) {
        
    }
}