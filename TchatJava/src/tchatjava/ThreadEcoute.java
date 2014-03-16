package tchatjava;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MotCle;

public class ThreadEcoute extends Thread {


    private Tchat tchat;

    public ThreadEcoute(Tchat tchat) {
        this.tchat = tchat;
    }

    public Tchat getServeur() {
        return tchat;
    }
    
    @Override
    public void run() {
        try {
                if(tchat.getCompte().getOuvert()) {
                while(true)
                {
                //Scanner sc = new Scanner(System.in);
                Message mss2 = (Message) tchat.getCompte().getEntree().readObject();
                if(mss2.getMotCle()==MotCle.MESSAGE)
                tchat.setJtextpanel(mss2);
                else if(mss2.getMotCle()==MotCle.USERCONNECTIONROOM)
                {
                    tchat.setUser(mss2.getPseudo());
                }
                }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}