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

	public void connexion (String message) {
            ouvrirSocket();
            if(this.getOuvert()) {
                    this.information();
                    this.ecrire(message);
                    System.out.println("1");
                    this.fermerSocket();
            }
        }
        
        
        public void connexionVerifPseudo(String pseudo) {
            ouvrirSocket();
            if(this.getOuvert()) {
                    this.information();
                    try {
                OutputStream out = null;
                InputStream in = null;
                // Recuperation du flot de sortie
                out = socket.getOutputStream();
                in = socket.getInputStream();
                if (out != null) {
                    ObjectOutputStream sortie = new ObjectOutputStream(out); // Creation du flot de sortie pour donnees typees
                    System.out.println("Flux de sortie ouvert");
                    // Lectures/ecritures
                    Message mss = new Message((String)this.getPseudos().get(this.getPseudos().size()-1), MotCle.VERIFICATIONPSEUDO);
                    sortie.writeObject(mss);
                    System.out.println("2");
                } else System.out.println("Erreur d'ouverture du flux de sortie");// Recuperation du flot d'entree
                System.out.println("3");
                if (in != null) {
                    ObjectInputStream entree = new ObjectInputStream(in); // Creation du flot d'entree pour donnees typees
                    System.out.println("Flux d'entree ouvert");
                    // Lectures/ecritures
                    Message mss2 = (Message) entree.readObject();
                    mss2.information();
                } else { System.out.println("Erreur du flux d'entree"); }
            } catch (    IOException | ClassNotFoundException ex) {
                //Logger.getLogger(class.getName()).log(Level.SEVERE, null, ex);
            }
                    System.out.println("1");
                    this.fermerSocket();
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
	
	public void fermerSocket() {
		try {
			socket.close();
			System.out.println("Deconnection au serveur");
		} catch (IOException e) { System.out.println("Erreur fermeture connexion"); }
	}
	
	public void information() {
		System.out.println("Ip : " + socket.getInetAddress() + " Port : " + socket.getPort());
	}
        
        public void ecrire(String text) {
            try {
                OutputStream out = null;
                InputStream in = null;
                // Recuperation du flot de sortie
                out = socket.getOutputStream();
                in = socket.getInputStream();
                if (out != null) {
                    ObjectOutputStream sortie = new ObjectOutputStream(out); // Creation du flot de sortie pour donnees typees
                    System.out.println("Flux de sortie ouvert");
                    // Lectures/ecritures
                    Message mss = new Message((String)this.getPseudos().get(this.getPseudos().size()-1), text);
                    sortie.writeObject(mss);
                    System.out.println("2");
                } else System.out.println("Erreur d'ouverture du flux de sortie");// Recuperation du flot d'entree
                System.out.println("3");
                if (in != null) {
                    ObjectInputStream entree = new ObjectInputStream(in); // Creation du flot d'entree pour donnees typees
                    System.out.println("Flux d'entree ouvert");
                    // Lectures/ecritures
                    Message mss2 = (Message) entree.readObject();
                    mss2.information();
                } else { System.out.println("Erreur du flux d'entree"); }
            } catch (    IOException | ClassNotFoundException ex) {
                //Logger.getLogger(class.getName()).log(Level.SEVERE, null, ex);
            }
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