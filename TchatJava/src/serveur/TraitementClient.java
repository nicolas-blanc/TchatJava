package serveur;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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
            System.out.println("Connexion ferme");
        } catch (IOException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                    // Lectures/ecritures
                    Message mss = (Message) entree.readObject();
                    mss.information();
            } else { System.out.println("Erreur du flux d'entree"); }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        fermerTransfert();
        serveur.delListThread(this);
    }
}

/*public static void main (String[] args) {
    try {
            
    } catch (IOException e) { e.printStackTrace(); }
    serveur.fermerTransfert();
    System.out.println("Extinction serveur");
}*/