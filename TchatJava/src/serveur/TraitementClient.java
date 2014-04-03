package serveur;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MotCle;

public class TraitementClient extends Thread {

    private final Serveur serveur;
    private final Socket socket_transfert;
    private boolean nonfin;
    private InputStream in;
    private ObjectInputStream entree;
    private OutputStream out;
    private ObjectOutputStream sortie;
    private String pseudo;
    private LinkedBlockingQueue<String> rooms;

    public TraitementClient(Serveur serv, Socket so) {
        serveur = serv;
        socket_transfert = so;
        serveur.addListThread(this);
        rooms = new LinkedBlockingQueue<String>();
        nonfin = true;
    }
    
    public LinkedBlockingQueue<String> getRooms()
    {
        return rooms;
    }

    private void ouvrirTransfert() {
        try {
            in = null;
            out = null;
            // Recuperation du flot d'entree
            while (in == null) {
                in = socket_transfert.getInputStream();
                System.out.println("1");
            }
            entree = new ObjectInputStream(in); // Creation du flot d'entree pour donnees typees
            if (entree != null) {
                System.out.println("Flux d'entrée ouvert");
            }

            // Recuperation du flot de sortie
            while (out == null) {
                out = socket_transfert.getOutputStream();
            }
            sortie = new ObjectOutputStream(out); // Creation du flot de sortie pour donnees typees
            if (sortie != null) {
                System.out.println("Flux de sortie ouvert");
            }

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
                try {
                    mss = (Message) entree.readObject();
                    switch (mss.getMotCle()) {
                        case CLOSE:
                            nonfin = false;
                            //serveur.disconnect(pseudo); -> a faire
                            serveur.getListThread().remove(this);
                            serveur.renvoi(new Message(pseudo, MotCle.DECONNECTIONUSER));
                            serveur.getConnecte().remove(pseudo);
                            serveur.getInfoServeur().miseAJourUtilisateurs();
                            break;
                        case MESSAGE:
                            transfertMessage(mss, mss.getRoom());
                            break;
                        case BANROOM:
                            serveur.getRooms().get(mss.getRoom()).getUtilisateurs().remove(mss.getPseudo());
                            serveur.getRooms().get(mss.getRoom()).setBannis(mss.getPseudo());
                            serveur.renvoi(mss);
                            break;
                        case DEBANROOM:
                            serveur.getRooms().get(mss.getRoom()).setUtilisateur(mss.getPseudo());
                            serveur.getRooms().get(mss.getRoom()).getBannis().remove(mss.getPseudo());
                            serveur.renvoi(mss);
                            break;
                        case MESSAGEGLOBAL:
                            serveur.renvoi(mss);
                            break;
                        case CREATIONROOM:
                            rooms.add(mss.getRoom());
                            serveur.setRoom(mss.getRoom(), pseudo);
                            serveur.renvoi(new Message(pseudo, "", message.MotCle.CREATIONROOM, mss.getRoom()));
                            serveur.getInfoServeur().miseAJourRooms();
                            break;
                        case CONNECTIONROOM:
                            rooms.add(mss.getRoom());
                            if(!serveur.getRooms().get(mss.getRoom()).getBannis().contains(pseudo))
                            {
                            serveur.getRooms().get(mss.getRoom()).setUtilisateur(pseudo);
                            serveur.renvoi(new Message(pseudo, "", message.MotCle.CONNECTIONROOM, mss.getRoom()));
                            }
                            else
                                this.renvoi(new Message(MotCle.BANNIROOM, mss.getRoom()));
                            break;
                        case DEMANDEROOMS:
                            this.pseudo = mss.getPseudo();
                            if (!(bannis(pseudo))) {
                                serveur.setConnecte(pseudo);
                                this.renvoi(new Message(message.MotCle.ENVOIROOMS, serveur.getRooms()));
                                serveur.getInfoServeur().miseAJourUtilisateurs();
                                serveur.renvoi(new Message(pseudo ,message.MotCle.USERCONNECTIONSERVEUR));
                            } else {
                                this.renvoi(new Message(MotCle.BAN));
                            }
                            break;
                        case DEMANDEUSER:
                            this.renvoi(new Message(message.MotCle.ENVOIUSER, serveur.getConnecte()));
                            break;
                        case VERIFICATIONPSEUDO:
                            if (serveur.getUtilisateurs().containsKey(mss.getPseudo())) {
                                this.renvoi(new Message("oui", message.MotCle.VERIFICATIONPSEUDO));
                            } else {
                                this.renvoi(new Message("non", message.MotCle.VERIFICATIONPSEUDO));
                                serveur.setUtilisateur(mss.getPseudo());
                                this.pseudo = mss.getPseudo();
                            }
                            break;
                        default:
                            //déconnection client
                            System.out.println("erreur");
                            break;
                        case DECONNECTIONUSERROOM:
                            rooms.remove(mss.getRoom());
                            serveur.getRooms().get(mss.getRoom()).getUtilisateurs().remove(mss.getPseudo());
                            serveur.renvoi(mss);
                            break;
                    }
                } catch (IOException ex) {
                    nonfin = false;
                }
            }
        } catch (ClassNotFoundException ex) {
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
                //System.out.println("Renvoi");
            } else {
                information();
            }
        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(TraitementClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getPseudo() {
        return pseudo;
    }

    public void transfertMessage(Message mss, String room) {
        serveur.renvoi(mss, room);
    }

    public void ban() {
        nonfin = false;
        renvoi(new Message(message.MotCle.BAN));
    }

    public Boolean bannis(String ps) {
        return serveur.getBannis().contains(ps);
    }
}
