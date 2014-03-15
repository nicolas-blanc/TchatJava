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
import static message.MotCle.CLOSE;

public class TraitementClient extends Thread {

    private final Serveur serveur;
    private final Socket socket_transfert;
    private boolean nonfin;
    private InputStream in;
    private ObjectInputStream entree;
    private OutputStream out;
    private ObjectOutputStream sortie;

    public TraitementClient(Serveur serv, Socket so) {
        serveur = serv;
        socket_transfert = so;
        serveur.addListThread(this);
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
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        ouvrirTransfert();
        try {
            while (nonfin) {
                // Lectures/ecritures
                Message mss;
                mss = (Message) entree.readObject();
                if(mss.getMotCle() == CLOSE)
                    nonfin = false;
                else if(mss.getMotCle() == message.MotCle.MESSAGE)
                    transfertMessage(mss);
                else if(mss.getMotCle() == message.MotCle.VERIFICATIONPSEUDO)
                {
                    if(serveur.getUtilisateurs().containsKey(mss.getPseudo()))
                    {
                        System.out.println("entrée");
                        this.renvoi(new Message("", "oui", message.MotCle.VERIFICATIONPSEUDO));
                    }
                    else
                    {
                        System.out.println("entré5555e");
                        this.renvoi(new Message("", "non", message.MotCle.VERIFICATIONPSEUDO));
                        serveur.setUtilisateur(mss.getPseudo());
                    }
                }
                else
                {
                    //déconnection client
                    System.out.println("erreur");
                }
            }
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        fermerTransfert();
        serveur.delListThread(this);
    }

    public void renvoi(Message mss) {
        try {
            // Lectures/ecritures
            if (sortie != null) {
                sortie.writeObject(mss);
                System.out.println("Renvoi");
            } else
                information();
        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void transfertMessage (Message mss) {
        serveur.renvoi(mss);
    }
}