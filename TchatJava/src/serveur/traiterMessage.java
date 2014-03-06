/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serveur;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

/**
 *
 * @author Nicolas
 */
public class traiterMessage extends Thread {
    private final TraitementClient client;
    private final Socket socket_transfert;
    private final Message mss;

    public traiterMessage(TraitementClient client, Socket socket_transfert, Message mss) {
        this.client = client;
        this.socket_transfert = socket_transfert;
        this.mss = mss;
    }

    @Override
    public void run() {
    }
    
    
}
