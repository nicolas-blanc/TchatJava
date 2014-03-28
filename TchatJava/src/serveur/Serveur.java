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
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import java.util.HashMap;
import tchatjava.MessageErreur;
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
    
    private HashMap<String, Users> utilisateurs; 
    private HashMap<String, Room> rooms;
    
    private ArrayList<String> connecte;
    private ArrayList<String> bannis;
    
    private InfoServeur info;

    public Serveur(Integer port) {
        info = null;
        this.port = port;
        bannis = new ArrayList<>(); 
        this.setUtilisateurs(new HashMap<String, Users>());
        this.setRooms(new HashMap<String, Room>());
        listThread = new LinkedBlockingQueue();
        restaure();
        ouvrirEcoute();
    }
    
    public void setInfoServeur(InfoServeur info)
    {
        this.info = info;
    }
    
    public InfoServeur getInfoServeur()
    {
        return info;
    }
    
    public ArrayList<String> getBannis()
    {
        return bannis;
    }
    
    public void setBannis(String pseudo)
    {
            bannis.add(pseudo);
    }

    @Override
    public void run() {
        attenteClient();
    }

    public HashMap<String, Users> getUtilisateurs() {
        return utilisateurs;
    }
    
    public ArrayList<String> transformationSetArrayList(Set<String> users)
    {
        ArrayList<String> usrs = new ArrayList();
        
        for(String user : users)
            usrs.add(user);
        
        return usrs;
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
    
    public String getIp() {
        return socket_ecoute.getInetAddress().toString();
    }
    
    public String getPort() {
        Integer p = socket_ecoute.getLocalPort();
        return p.toString();
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
        for(TraitementClient thread : listThread) {
            thread.renvoi(mss);
        }
    }
    
    public void renvoi(Message mss, TraitementClient th) {
        for(TraitementClient thread : listThread) {
            if(!th.equals(thread))
            {
            thread.renvoi(mss);
            }
        }
    }
    
    public void ban(String pseudo) {
        bannis.add(pseudo);
        for(TraitementClient thread : listThread) {
            if(pseudo.equals(thread.getPseudo())) {
                thread.ban();
            }
        }
    }
    
    public void supprimer(String pseudo) {
        
    }
    
    public void disconnect(String pseudo) {
        connecte.remove(pseudo);
    }
    
    public Serveur restaure() 
    {
            try
            {
                FileInputStream fichier = new FileInputStream("Fsauvserv.ser");
                ObjectInputStream in = new ObjectInputStream(fichier);
                //private static final long serialVersionUID = 1L;
                return((Serveur) in.readObject());
            } 
            catch (IOException | ClassNotFoundException e) 
            {
                    MessageErreur dialog = new MessageErreur();
                    dialog.setText("Probleme de restauration");
                    return this;
            } 
    }

    public void sauve() {
        try {
            FileOutputStream f = new FileOutputStream("Fsauvserv.ser");
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(utilisateurs);
            out.writeObject(rooms);
        } catch (IOException e) {
            MessageErreur dialog = new MessageErreur();
            dialog.setText("Pb de Sauvegarde dans le fichier");
        }
    }

}
