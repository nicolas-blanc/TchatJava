/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tchatjava;

import java.io.Serializable;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import message.Message;
import java.io.OutputStream;
import java.net.*;
import message.MotCle;
/**
 *
 * @author ponsma
 */

public class Compte implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private ArrayList<String> pseudos;
    private ArrayList<String> serveurs;
    private ImageIcon img;
    
    private final Integer port;
    private final String host;
    private Socket socket;
    private OutputStream out;
    private ObjectOutputStream sortie;
    private InputStream in;
    private ObjectInputStream entree;

    
        public boolean ConnexionVerifPseudo(String pseudo)
        {
        boolean pris = false;
        try{
            this.ouvrirSocket();
            if(this.getOuvert()) {
            this.information();
            this.ouvrirStream();
            this.ecrire(pseudo, "", message.MotCle.VERIFICATIONPSEUDO);
            
            Message mss2 = (Message) entree.readObject();
            if(mss2.getMotCle()==message.MotCle.VERIFICATIONPSEUDO)
            {
                if(mss2.getMessage()=="pris")
                {
                   pris = true;
                }
            }
            }
            
            }
             catch (IOException | ClassNotFoundException ex) {
               // Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        return pris;
        }
        
        public void ConnexionEcrire(String mesage)
        {
            if(this.getOuvert()) 
            {
            this.information();
            this.ouvrirStream();
            this.ecrire((String)this.getPseudos().get(this.getPseudos().size()-1), mesage, message.MotCle.MESSAGE);
            }
        }
	
	public boolean getOuvert() {
		return socket != null;
	}
	
	private void ouvrirSocket() {
            try {
                socket = new Socket (host, port);
                
            } catch (IOException e) { socket = null; System.out.println("Erreur ouverture connexion"); }
	}
        
        private void ouvrirStream() {
            try {
                in = null;
                out = null;
                // Recuperation du flot de sortie
                while(out == null) {
                    out = socket.getOutputStream();
                    System.out.println("1");
                }
                sortie = new ObjectOutputStream(out); // Creation du flot de sortie pour donnees typees
                System.out.println("Flux de sortie ouvert");
                // Recuperation du flot d'entree
                while(in == null)
                    in = socket.getInputStream();
                entree = new ObjectInputStream(in); // Creation du flot d'entree pour donnees typees
                System.out.println("Flux d'entrée ouvert");
            } catch (IOException ex) {
               // Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	
	public void fermerSocket() {
		try {
                    sortie.writeObject(new Message(MotCle.CLOSE));
                    System.out.println("Deconnection du serveur");
                    socket.close();
		} catch (IOException e) { System.out.println("Erreur fermeture connexion"); }
	}
	
	public void information() {
		System.out.println("Ip : " + socket.getInetAddress() + " Port : " + socket.getPort());
	}
        
        public void ecrire(String pseudo, String messageE, message.MotCle m) {
            try {
                while(true) {
                    // Lectures/ecritures
                    Message mss = new Message(pseudo, messageE, m);
                    sortie.writeObject(mss);
                }
            } catch (IOException ex) {
              //  Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public Message lire() {
            Message mss2 = new Message("", "");
            try {
                while(true) {
                    mss2 = (Message) entree.readObject();
                }
            } catch (    IOException | ClassNotFoundException ex) {
               // Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            return mss2;
        }
    
    public void setImage(ImageIcon i)
    {
        img = i;
    }
    
    public ImageIcon getImage()
    {
        return img;
    }
   
    public Compte()
    {
        pseudos = new ArrayList();
        serveurs = new ArrayList();
        port = 54686;
	host = "localhost";
    }
    
    public ArrayList getPseudos()
    {
       return pseudos; 
    }
    
    public void setPseudo(String pseudo)
    {
        pseudos.add(pseudo);
    }
    
    public ArrayList getServeurs()
    {
       return serveurs; 
    }
    
    public void setServeur(String serveur)
    {
        serveurs.add(serveur);
    }
    

    Compte restaure() 
    {
            try
            {
                FileInputStream fichier = new FileInputStream("Fsauv.ser");
                ObjectInputStream in = new ObjectInputStream(fichier);
                //private static final long serialVersionUID = 1L;
                return((Compte) in.readObject());
            } 
            catch (Exception e) 
            {
                    MessageErreur dialog = new MessageErreur();
                    dialog.setText("Probleme de restauration");
                    return this;
            } 
    }

    void sauve() 
    {
            try 
            {
                FileOutputStream f = new FileOutputStream("Fsauv.ser");
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