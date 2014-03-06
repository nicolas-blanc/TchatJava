package serveur;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TraitementClient extends Thread {

    private final Serveur serveur;
    private final Socket socket_transfert;

    public TraitementClient(Serveur serv, Socket so) {
        serveur = serv;
        socket_transfert = so;
        serveur.addListThread(this);
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
        Boolean nonfin = true;
        while (nonfin) {
            
        }
        fermerTransfert();
        serveur.delListThread(this);
    }
}
