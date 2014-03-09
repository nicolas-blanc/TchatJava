/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package serveur;

import message.Message;

/**
 *
 * @author Nicolas
 */
public class TraiterMessage extends Thread {
    private final TraitementClient client;
    private final Message mss;

    public TraiterMessage(TraitementClient client, Message mss) {
        this.client = client;
        this.mss = mss;
    }

    @Override
    public void run() {
        client.getServeur().renvoi(mss);
    }
    
    
}
