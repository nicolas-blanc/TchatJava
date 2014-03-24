/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tchatjava;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import message.Message;
import java.io.OutputStream;
import message.MotCle;
import java.util.HashMap;
import Users.Users;
import Rooms.Room;
import java.util.ArrayList; 

/**
 *
 * @author ponsma
 */

public class Compte {

    private ArrayList<String> usersserv;
    private HashMap<String, Room> serveurs;
    private ImageIcon img;
    private SauvegardePseudo save;
    private ThreadEcouteGlobal th;

    private Integer port;
    private String host;
    private Socket socket;
    private OutputStream out;
    private ObjectOutputStream sortie;
    private InputStream in;
    private ObjectInputStream entree;

    public Compte() {
        save = new SauvegardePseudo().restaure();
        serveurs = new HashMap<String, Room>();
        usersserv = new ArrayList<String>();
    }
    
    public void connexionServeur(Integer port, String host) {
        this.port = port;
        this.host = host;
        this.ouvrirSocket();
            if (this.getOuvert()) 
                this.ouvrirStream();
    }
    
    public SauvegardePseudo getSave() {
        return save;
    }

    public ObjectInputStream getEntree() {
        return entree;
    }

    public boolean ConnectionVerifPseudo(String pseudo) {
        boolean pris = true;
        try {
            if (this.getOuvert()) {
                this.ecrire(pseudo, "", message.MotCle.VERIFICATIONPSEUDO);
                Message mss2 = (Message) entree.readObject();
                if (mss2.getMotCle() == message.MotCle.VERIFICATIONPSEUDO) {
                    if (mss2.getMessage().contains("oui")) {
                        pris = true;
                    } else {
                        pris = false;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pris;
    }

    public void ConnectionEcrire(String mesage) {
        if (this.getOuvert()) {
            this.ecrire((String) this.getSave().getPseudos().get(this.getSave().getPseudos().size() - 1), mesage, message.MotCle.MESSAGE);
        }
    }
    
    public void ConnectionEcrireGlobal(String mesage) {
        if (this.getOuvert()) {
            this.ecrire((String) this.getSave().getPseudos().get(this.getSave().getPseudos().size() - 1), mesage, message.MotCle.MESSAGEGLOBAL);
        }
    }

    public void ConnectionRoom(String room, TchatCreationServeur tchat) {
        if (this.getOuvert()) {
            if (this.serveurs.containsKey(room)) {
                this.ecrire("", room, message.MotCle.CONNECTIONROOM);
            } else {
                this.ecrire(this.getSave().getPseudos().get(this.getSave().getPseudos().size() - 1), room, message.MotCle.CREATIONROOM);
            }
        }
    }

    public void demandeRoom() {
            if (this.getOuvert()) {
                this.ecrire(this.getSave().getPseudos().get(this.getSave().getPseudos().size() - 1), "", message.MotCle.DEMANDEROOMS);
        }
    }

    public boolean getOuvert() {
        return socket != null;
    }

    private void ouvrirSocket() {
        try {
            socket = new Socket(host, port);

        } catch (IOException e) {
            socket = null;
            System.out.println("Erreur ouverture connexion");
        }
    }

    public Socket getSocket() {
        return socket;
    }

    private void ouvrirStream() {
        try {
            in = null;
            out = null;
            // Recuperation du flot de sortie
            while (out == null) {
                out = socket.getOutputStream();
            }
            sortie = new ObjectOutputStream(out); // Creation du flot de sortie pour donnees typees
            // Recuperation du flot d'entree
            while (in == null) {
                in = socket.getInputStream();
            }
            entree = new ObjectInputStream(in); // Creation du flot d'entree pour donnees typees
        } catch (IOException ex) {
            Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fermerSocket() {
        try {
            sortie.writeObject(new Message(MotCle.CLOSE));
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur fermeture connexion");
        }
    }

    public void ecrire(String pseudo, String messageE, message.MotCle m) {
        try {
            // Lectures/ecritures
            Message mss = new Message(pseudo, messageE, m);
            sortie.writeObject(mss);
        } catch (IOException ex) {
            Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lireRoom(Tchat tchat) {
        th.setTchat(tchat);
    }

    public void lireGlobal(TchatCreationServeur tchat) {
        th = new ThreadEcouteGlobal(tchat);
        th.start();
    }

    public void setImage(ImageIcon i) {
        img = i;
    }

    public ImageIcon getImage() {
        return img;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public HashMap<String, Room> getServeurs() {
        return serveurs;
    }

    public ArrayList<String> getUsers() {
        return usersserv;
    }

    public void setUsers(ArrayList<String> users) {
        usersserv = users;
    }

    public void setServeurs(HashMap<String, Room> serveurs) {
        this.serveurs = serveurs;
    }

}
