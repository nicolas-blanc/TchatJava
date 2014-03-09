package serveur;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

public class TraitementClient extends Thread {

    private final Serveur serveur;
    private final Socket socket_transfert;
    private Boolean nonfin;

    public TraitementClient(Serveur serv, Socket so) {
        serveur = serv;
        socket_transfert = so;
        serveur.addListThread(this);
        nonfin = true;
    }

    public void fermerTransfert() {
        try {
            socket_transfert.close();
            serveur.delListThread(this);
            System.out.println("Connexion ferme");
            information();
        } catch (IOException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Serveur getServeur() {
        return serveur;
    }
    
    public void information() {
        System.out.println("Ip : " + socket_transfert.getInetAddress() + " Port : " + socket_transfert.getPort());
    }
    
    @Override
    public void run() {
        information();
        InputStream in = null;
        try {
            // Recuperation du flot d'entree
            in = socket_transfert.getInputStream();
            if (in != null) {
                ObjectInputStream entree = new ObjectInputStream(in); // Creation du flot d'entree pour donnees typees
                System.out.println("Flux d'entree ouvert");
                while (nonfin) {
                    // Lectures/ecritures
                    TraiterMessage tm = new TraiterMessage(this, (Message) entree.readObject());
                    tm.start();
                }
            } else { System.out.println("Erreur du flux d'entree"); }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        fermerTransfert();
        serveur.delListThread(this);
    }

    public void renvoi(Message mss) {
        try {
            OutputStream out = null;
            // Recuperation du flot de sortie
            out = socket_transfert.getOutputStream();
            if (out != null) {
                ObjectOutputStream sortie = new ObjectOutputStream(out); // Creation du flot de sortie pour donnees typees
                // Lectures/ecritures
                sortie.writeObject(mss);
                System.out.println("Renvoi");
            } else System.out.println("Erreur d'ouverture du flux de sortie");
        } catch (IOException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}