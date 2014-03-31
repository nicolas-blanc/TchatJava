package Rooms;


import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    
    private ArrayList<String> utilisateurs;
    private ArrayList<String> bannis;
    private String administrateur;
    
    public Room(String administrateur)
    {
        this.administrateur = administrateur;
        utilisateurs = new ArrayList<String>();
        bannis = new ArrayList<String>();
        utilisateurs.add(administrateur);
    }
            
    public void setUtilisateur(String utilisateur)
    {
        utilisateurs.add(utilisateur);
    }
    
    public ArrayList<String> getUtilisateurs()
    {
        return utilisateurs;
    }
    
    public void setBannis(String utilisateur)
    {
        bannis.add(utilisateur);
    }
    
    public ArrayList<String> getBannis()
    {
        return bannis;
    }
    
    public String getAdministrateur()
    {
        return administrateur;
    }
    
}
