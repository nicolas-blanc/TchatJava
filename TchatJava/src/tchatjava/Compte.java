/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tchatjava;

import java.io.Serializable;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 *
 * @author ponsma
 */

public class Compte implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private ArrayList<String> pseudos;
    
    public Compte()
    {
        pseudos = new ArrayList();
    }
    
    public ArrayList getPseudos()
    {
       return pseudos; 
    }
    
    public void setPseudo(String pseudo)
    {
        pseudos.add(pseudo);
    }
    

    Compte restaure() 
    {
            try
            {
                FileInputStream fichier = new FileInputStream("Fsauv.ser");
                ObjectInputStream in = new ObjectInputStream(fichier);
                //private static final long serialVersionUID = 1L;
                return((Compte) in.readObject());
            } 
            catch (Exception e) 
            {
                    MessageErreur dialog = new MessageErreur();
                    dialog.setText("Probleme de restauration");
                    return this;
            } 
    }

    void sauve() 
    {
            try 
            {
                FileOutputStream f = new FileOutputStream("Fsauv.ser");
                ObjectOutputStream out = new ObjectOutputStream(f);
                out.writeObject(this);
            } 
            catch (Exception e) 
            {
                    MessageErreur dialog = new MessageErreur();
                    dialog.setText("Pb de Sauvegarde dans le fichier");
            }
    }
    
}
