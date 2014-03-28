/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import Rooms.Room;
import Users.Users;
import java.io.Serializable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import java.util.HashMap;
import java.util.*;

/**
 *
 * @author blancn
 */
public class Serveur extends Thread implements Serializable {

    private final Integer port;
    private ServerSocket socket_ecoute;
    private Socket socket_transfert;

    private LinkedBlockingQueue<TraitementClient> listThread;

    private ArrayList<String> connecte;

    private InfoServeur info;
    private Sauvegarde sauvegarde;

    public Serveur(Integer port) {
        info = null;
        this.port = port;
        connecte = new ArrayList<>();
        listThread = new LinkedBlockingQueue();
        sauvegarde = new Sauvegarde().restaure();
        ouvrirEcoute();
    }

    public void setInfoServeur(InfoServeur info) {
        this.info = info;
    }

    public InfoServeur getInfoServeur() {
        return info;
    }

    public ArrayList<String> getBannis() {
        return sauvegarde.getBannis();
    }

    public void setBannis(String pseudo) {
        sauvegarde.addBannis(pseudo);
    }

    @Override
    public void run() {
        attenteClient();
    }

    public HashMap<String, Users> getUtilisateurs() {
        return sauvegarde.getUtilisateurs();
    }

    public ArrayList<String> transformationSetArrayList(Set<String> users) {
        ArrayList<String> usrs = new ArrayList();

        for (String user : users) {
            usrs.add(user);
        }

        return usrs;
    }

    public void setUtilisateur(String pseudo) {
        sauvegarde.getUtilisateurs().put(pseudo, new Users());
    }

    public HashMap<String, Room> getRooms() {
        return sauvegarde.getRooms();
    }

    public void setRoom(String room, String pseudo) {
        sauvegarde.getRooms().put(room, new Room(pseudo));
    }

    public String getIp() {
        return socket_ecoute.getInetAddress().toString();
    }

    public String getPort() {
        Integer p = socket_ecoute.getLocalPort();
        return p.toString();
    }

    public void sauve() {
        sauvegarde.sauve();
    }

    public ArrayList<String> getConnecte() {
        return connecte;
    }

    public void setConnecte(String pseudo) {
        connecte.add(pseudo);
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
            TraitementClient tc = new TraitementClient(this, socket_transfert);
            tc.start();
        }
    }

    public void addListThread(TraitementClient tc) {
        listThread.add(tc);
    }

    public void delListThread(TraitementClient tc) {
        listThread.remove(tc);
    }

    public void renvoi(Message mss, String room) {
        for (TraitementClient thread : listThread) {
            if (room.equals(thread.getRoom())) {
                thread.renvoi(mss);
            }
        }
    }

    public void renvoi(Message mss) {
        for (TraitementClient thread : listThread) {
            thread.renvoi(mss);
        }
    }

    public void renvoi(Message mss, TraitementClient th) {
        for (TraitementClient thread : listThread) {
            if (!th.equals(thread)) {
                thread.renvoi(mss);
            }
        }
    }

    public void ban(String pseudo) {
        //sauvegarde.add(pseudo);
        for (TraitementClient thread : listThread) {
            if (pseudo.equals(thread.getPseudo())) {
                thread.ban();
            }
        }
    }
}
