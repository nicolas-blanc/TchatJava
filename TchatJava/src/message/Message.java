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
    private final Object donnees;

    public Message(String pseudo, MotCle motCle) {
        this.message = "";
        this.pseudo = pseudo;
        this.motCle = motCle;
        donnees = null;
    }
    
    public Message(String pseudo, String message, MotCle motCle) {
        this.message = message;
        this.pseudo = pseudo;
        this.motCle = motCle;
        donnees = null;
    }
    
    public Message(String message, MotCle motCle, Object don) {
        this.message = message;
        this.pseudo = " ";
        this.motCle = motCle;
        donnees = don;
    }
    
    public Message(MotCle motCle, Object don) {
        this.message = " ";
        this.pseudo = " ";
        this.motCle = motCle;
        donnees = don;
    }

    public Message(MotCle motCle) {
        this.message = "";
        this.pseudo = "";
        this.motCle = motCle;
        donnees = null;
    }

    public Message(String pseudo, String message) {
        this.message = message;
        this.pseudo = pseudo;
        this.motCle = MESSAGE;
        donnees = null;
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
    
    public Object getDonnees()
    {
        return donnees;
    }

    public void information() {
        System.out.println(pseudo + " - " + message);
    }
    
}
