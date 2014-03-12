package Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.*;

// port = 56686

class ClientTest {

	private final Integer port;
	private final String host;
	private Socket socket;
        private OutputStream out;
        private ObjectOutputStream sortie;
        private InputStream in;
        private ObjectInputStream entree;

	public static void main (String[] args) {
		if(args.length == 2) {
			if(Integer.parseInt(args[0]) > 50000 && Integer.parseInt(args[0]) < 60000) {
				ClientTest client = new ClientTest(args[1],Integer.parseInt(args[0]));
				if(client.getOuvert()) {
					client.information();
                                        client.ouvrirStream();
                                        client.ecrire();
                                        System.out.println("1");
					/*try {
						Thread.sleep(20000);
					} catch (InterruptedException e) { System.out.println("Erreur Thread.sleep()"); }*/
					client.fermerSocket();
				}
			} else System.out.println("Erreur de port entre 50000 et 60000");
		} else System.out.println("Erreur d'arguments [port 50000->60000]");
	}
	
	public ClientTest(String h, int p) {
		port = p;
		host = h;
		ouvrirSocket();
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
                Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	
	public void fermerSocket() {
		try {
                    sortie.writeObject(new Message(" ", " ", MotCle.CLOSE));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) { System.out.println("Erreur Thread.sleep()"); }
                    System.out.println("Deconnection du serveur");
                    socket.close();
		} catch (IOException e) { System.out.println("Erreur fermeture connexion"); }
	}
	
	public void information() {
		System.out.println("Ip : " + socket.getInetAddress() + " Port : " + socket.getPort());
	}
        
        public void ecrire() {
            try {
                Scanner sc = new Scanner(System.in);
                while(true) {
                    System.out.println("Saisissez une phrase:");
                    String str = sc.nextLine();
                    // Lectures/ecritures
                    Message mss = new Message("Nico", str);
                    sortie.writeObject(mss);
                    // Lectures/ecritures
                    Message mss2 = (Message) entree.readObject();
                    mss2.information();
                }
            } catch (    IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
