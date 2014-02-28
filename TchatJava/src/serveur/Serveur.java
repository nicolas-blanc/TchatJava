/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serveur;

import java.io.IOException;
import java.net.ServerSocket;
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
    private LinkedBlockingQueue listThread; // a modif -> voir blockingqueue ou autre ==> ne gere l'exclusion mutuelle

    public static void main (String[] args) {
        if(args.length == 1) {
            if(Integer.parseInt(args[0]) > 50000 && Integer.parseInt(args[0]) < 60000) {
                Serveur serveur = new Serveur(Integer.parseInt(args[0]));
                attenteClient();
                //while (true) {
                    TraitementClient tc = new TraitementClient(serveur);
                    tc.start();
                //}
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
    
    public void addListThread(TraitementClient tc) {
        listThread.add(tc);
    }
    
    public ServerSocket getSocketEcoute() { return socket_ecoute; }

}
/*public static void main (String[] args) {
    InputStream in = null;
    try {
            // Recuperation du flot d'entree
            in = serveur.getSocketTransfert().getInputStream();
            if (in != null) {
                    DataInputStream entree = new DataInputStream(in); // Creation du flot d'entree pour donnees typees
                    System.out.println("Flux d'entree ouvert");
                    // Lectures/ecritures
                    int j = entree.readInt();
                    System.out.println(j);
            } else { System.out.println("Erreur du flux d'entree"); }
    } catch (IOException e) { e.printStackTrace(); }
    serveur.fermerTransfert();
    System.out.println("Extinction serveur");
}*/