/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author blancn
 */
public class Serveur {
	private final Integer port;
	private ServerSocket socket_ecoute;
	private Socket socket_transfert;

	public static void main (String[] args) {
		if(args.length == 1) {
			if(Integer.parseInt(args[0]) > 50000 && Integer.parseInt(args[0]) < 60000) {
				Serveur serveur = new Serveur(Integer.parseInt(args[0]));
				serveur.information();
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) { System.out.println("Erreur Thread.sleep()"); }
				serveur.fermerTransfert();
			} else System.out.println("Erreur de port entre 50000 et 60000");
		} else System.out.println("Erreur d'arguments [port 50000->60000]");
	}
	
	public Serveur(Integer p) {
		port = p;
		ouvrirEcoute();
		ouvrirTransfert();
	}
	
	private void ouvrirEcoute() {
		try {
			socket_ecoute = new ServerSocket(port);
		} catch (IOException e) { System.out.println("Erreur ouverture socket d'ecoute"); }
	}
	
	private void ouvrirTransfert() {
		try {
			socket_transfert = socket_ecoute.accept(); // appel bloquant
		} catch (IOException e) { System.out.println("Erreur ouverture socket de transfert"); }
	}
	
	public void fermerTransfert() {
		try {
			socket_transfert.close();
		} catch (IOException e) { System.out.println("Erreur fermeture socket de transfert"); }
	
	}
	
	public void information() {
		System.out.println("Ip : " + socket_transfert.getInetAddress() + " Port : " + socket_transfert.getPort());
	}
	
}