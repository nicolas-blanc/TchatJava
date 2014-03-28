/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import Rooms.Room;
import Users.Users;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import tchatjava.MessageErreur;

/**
 *
 * @author mouah
 */
public class Sauvegarde implements Serializable {

    private HashMap<String, Users> utilisateurs;
    private HashMap<String, Room> rooms;
    private ArrayList<String> bannis;

    public Sauvegarde() {
        utilisateurs = new HashMap<String, Users>();
        rooms = new HashMap<>();
        bannis = new ArrayList<>();
    }

    public ArrayList<String> getBannis() {
        return bannis;
    }

    public void addBannis(String pseudo) {
        bannis.add(pseudo);
    }
    
    public void delBannis(String pseudo) {
         bannis.remove(pseudo);
    }

    public HashMap<String, Users> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateur(String pseudo) {
        utilisateurs.put(pseudo, new Users());
    }

    private void setUtilisateurs(HashMap<String, Users> users) {
        utilisateurs = users;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    public void setRoom(String room, String pseudo) {
        rooms.put(room, new Room(pseudo));
    }

    private void setRooms(HashMap<String, Room> rooms) {
        this.rooms = rooms;
    }

    public Sauvegarde restaure() {
        try {
            FileInputStream fichier = new FileInputStream("Fsauvserv.ser");
            ObjectInputStream in = new ObjectInputStream(fichier);
            //private static final long serialVersionUID = 1L;
            return ((Sauvegarde) in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            MessageErreur dialog = new MessageErreur();
            dialog.setText("Probleme de restauration");
            return this;
        }
    }

    public void sauve() {
        try {
            FileOutputStream f = new FileOutputStream("Fsauvserv.ser");
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(this);
        } catch (IOException e) {
            MessageErreur dialog = new MessageErreur();
            dialog.setText("Pb de Sauvegarde dans le fichier");
        }
    }
}
