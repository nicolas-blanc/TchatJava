/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tchatjava;

/**
 *
 * @author mouah
 */
import serveur.*;
import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    
    private ArrayList<String> utilisateurs;
    private String administrateur;
    
    public Room(String administrateur)
    {
        this.administrateur = administrateur;
    }
            
    public void setUtilisateur(String utilisateur)
    {
        utilisateurs.add(utilisateur);
    }
    
    public ArrayList<String> getUtilisateurs()
    {
        return utilisateurs;
    }
    
    public String getAdministrateur()
    {
        return administrateur;
    }
    
}