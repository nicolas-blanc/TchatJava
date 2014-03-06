/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package message;

import static message.MotCle.*;

/**
 *
 * @author Nicolas
 */

public class Message {
    private String message;
    private String pseudo;
    private MotCle motCle;

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
    
}
