package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TraitementClient extends Thread {

    private final Serveur serveur;
    private Socket socket_transfert;
    private Boolean lier;

    public TraitementClient(Serveur s) {
        serveur = s;
        serveur.addListThread(this);
        lier = false;
    }

    private void ouvrirTransfert() {
        try {
            socket_transfert = serveur.getSocketEcoute().accept(); //a remettre dans serveur
            System.out.println("Serveur Ok");
            lier = true;
        } catch (IOException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fermerTransfert() {
        try {
            socket_transfert.close();
            System.out.println("Connexion ferme");
        } catch (IOException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private Socket getSocketTransfert() { return socket_transfert; }

    public void information() {
            System.out.println("Ip : " + socket_transfert.getInetAddress() + " Port : " + socket_transfert.getPort());
    }
    
    public Boolean getLier() { return lier; }
    
    @Override
    public void run() {
        ouvrirTransfert();
        information();
        fermerTransfert();
    }
}
