package serveur;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import static message.MotCle.*;

public class ThreadServeur extends Thread {

    private Serveur serveur;

    public ThreadServeur(Serveur s) {
        serveur = s;
    }
    
    @Override
    public void run() {
        serveur.lancerServeur();
    }
}