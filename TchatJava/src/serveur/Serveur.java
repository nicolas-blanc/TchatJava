/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serveur;
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
import tchatjava.Compte;
import tchatjava.MessageErreur;

/**
 *
 * @author blancn
 */
public class Serveur implements Serializable {
    private final Integer port;
    private ServerSocket socket_ecoute;
    private Socket socket_transfert;
    private LinkedBlockingQueue<TraitementClient> listThread;
    private HashMap<String, Users> utilisateurs;
    private HashMap<String, Room> rooms;

    public void lancerServeur() {
                this.attenteClient();
    }

    public Serveur() {
        this.setUtilisateurs(new HashMap<String, Users>());
        this.setRooms(new HashMap<String, Room>());
        port = 51569;
        listThread = new LinkedBlockingQueue();
        ouvrirEcoute();
    }
    
    public HashMap<String, Users> getUtilisateurs()
    {
        return utilisateurs;
    }
    
    public void setUtilisateur(String pseudo)
    {
        utilisateurs.put(pseudo, new Users());
    }
    
    private void setUtilisateurs(HashMap<String,Users> users)
    {
        utilisateurs = users;
    }
    
    public HashMap<String, Room> getRooms()
    {
        return rooms;
    }
    
    public void setRoom(String room, String pseudo)
    {
        rooms.put(room, new Room(pseudo));
    }
    
    private void setRooms(HashMap<String,Room> rooms)
    {
        this.rooms = rooms;
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
            TraitementClient tc = new TraitementClient(this,socket_transfert);
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
        for(TraitementClient thread : listThread) {
            if(room.equals(thread.getRoom()))
            {
            thread.renvoi(mss);
            }
        }
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
            catch (Exception e) 
            {
                    MessageErreur dialog = new MessageErreur();
                    dialog.setText("Probleme de restauration");
                    return this;
            } 
    }

    public void sauve() 
    {
            try 
            {
                FileOutputStream f = new FileOutputStream("Fsauvserv.ser");
                ObjectOutputStream out = new ObjectOutputStream(f);
                out.writeObject(this);
            } 
            catch (Exception e) 
            {
                    MessageErreur dialog = new MessageErreur();
                    dialog.setText("Pb de Sauvegarde dans le fichier");
            }
    }
            
}