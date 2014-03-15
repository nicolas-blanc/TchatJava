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
import message.Message;
import java.util.HashMap;

/**
 *
 * @author blancn
 */
public class Serveur {
    private final Integer port;
    private ServerSocket socket_ecoute;
    private Socket socket_transfert;
    private LinkedBlockingQueue<TraitementClient> listThread;
    private HashMap<String, Users> utilisateurs;

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
        this.setUtilisateurs(new HashMap<String, Users>());
        port = p;
        listThread = new LinkedBlockingQueue();
        ouvrirEcoute();
    }
    
    public HashMap<String, Users> getUtilisateurs()
    {
        return utilisateurs;
    }
    
    public void setUtilisateur(String pseudo)
    {
        utilisateurs.put(pseudo, new Users());
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
            socket_transfert = socket_ecoute.accept();
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
        listThread.remove(tc);
    }
    
    public void renvoi(Message mss) {
        for(TraitementClient thread : listThread) {
            thread.renvoi(mss);
        }
    }
    
    private void setUtilisateurs(HashMap<String,Users> users)
    {
        utilisateurs = users;
    }
            
}