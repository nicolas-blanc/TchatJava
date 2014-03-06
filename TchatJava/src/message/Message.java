/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package message;

import java.io.Serializable;
import static message.MotCle.*;

/**
 *
 * @author Nicolas
 */

public class Message implements Serializable{
    private final String message;
    private final String pseudo;
    private final MotCle motCle;

    public Message(String message, String pseudo, MotCle motCle) {
        this.message = message;
        this.pseudo = pseudo;
        this.motCle = motCle;
    }

    public Message(String message, String pseudo) {
        this.message = message;
        this.pseudo = pseudo;
        this.motCle = MESSAGE;
    }

    public String getMessage() {
        return message;
    }

    public String getPseudo() {
        return pseudo;
    }

    public MotCle getMotCle() {
        return motCle;
    }

    public void information() {
        System.out.println("Pseudo : " + pseudo);
        System.out.println("Message : " + message);
    }
    
}
