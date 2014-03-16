package tchatjava;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import static message.MotCle.*;

public class ThreadEcoute extends Thread {

    private final Socket socket_transfert;
    private boolean nonfin;
    private InputStream in;
    private ObjectInputStream entree;
    private OutputStream out;
    private ObjectOutputStream sortie;
    private Tchat tchat;

    public ThreadEcoute(Tchat tchat, Socket so) {
        this.tchat = tchat;
        socket_transfert = so;
        nonfin = true;
    }
    
    private void ouvrirTransfert() {
        try {
            in = null;
            out = null;
            // Recuperation du flot d'entree
            while(in == null) {
                in = socket_transfert.getInputStream();
                System.out.println("1");
            }
            entree = new ObjectInputStream(in); // Creation du flot d'entree pour donnees typees
            if (entree != null)
                System.out.println("Flux d'entrée ouvert");

            // Recuperation du flot de sortie
            while(out == null)
                out = socket_transfert.getOutputStream();
            sortie = new ObjectOutputStream(out); // Creation du flot de sortie pour donnees typees
            if (sortie != null)
                System.out.println("Flux de sortie ouvert");
            
        } catch (IOException ex) {
           // Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fermerTransfert() {
        try {
            socket_transfert.close();
            System.out.println("Connexion ferme");
        } catch (IOException ex) {
           // Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Tchat getServeur() {
        return tchat;
    }
    
    
    @Override
    public void run() {
        tchat.getCompte().lire(tchat);
    }
}