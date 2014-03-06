package Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.*;

// port = 56686

class ClientTest {

	private final Integer port;
	private final String host;
	private static Socket socket;

	public static void main (String[] args) {
		if(args.length == 2) {
			if(Integer.parseInt(args[0]) > 50000 && Integer.parseInt(args[0]) < 60000) {
				ClientTest client = new ClientTest(args[1],Integer.parseInt(args[0]));
				if(client.getOuvert()) {
					client.information();
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) { System.out.println("Erreur Thread.sleep()"); }
                                        client.ecrire();
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
	
	public void fermerSocket() {
		try {
			socket.close();
			System.out.println("Deconnection au serveur");
		} catch (IOException e) { System.out.println("Erreur fermeture connexion"); }
	}
	
	public void information() {
		System.out.println("Ip : " + socket.getInetAddress() + " Port : " + socket.getPort());
	}
        
        private void ecrire() {
            try {
                OutputStream out = null;
                // Recuperation du flot de sortie
                out = socket.getOutputStream(); 
                if (out != null) {
                    ObjectOutputStream sortie = new ObjectOutputStream(out); // Creation du flot de sortie pour donnees typees
                    System.out.println("Flux de sortie ouvert");
                    // Lectures/ecritures
                    Message mss = new Message("Nico", "Salut ca va ?");
                    sortie.writeObject(mss);
                } else System.out.println("Erreur d'ouverture du flux de sortie");
            } catch (IOException ex) {
                Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}