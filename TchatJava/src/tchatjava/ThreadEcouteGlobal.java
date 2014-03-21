package tchatjava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MotCle;
import Users.Users;
import Rooms.Room;

public class ThreadEcouteGlobal extends Thread {

    private TchatCreationServeur tchat;
    private Tchat tchat1;

    public ThreadEcouteGlobal(TchatCreationServeur tchat) {
        this.tchat = tchat;
        tchat1 = null;
    }

    public TchatCreationServeur getServeur() {
        return tchat;
    }
    
    public void setTchat(Tchat tc)
    {
        tchat1 = tc;
    }
    
    @Override
    public void run() {
        try {
                while(tchat.getCompte().getOuvert())
                {
                //Scanner sc = new Scanner(System.in);
                Message mss2 = (Message)tchat.getCompte().getEntree().readObject();
                if(mss2.getMotCle()==MotCle.MESSAGEGLOBAL)
                tchat.setJtextpanel(mss2);
                else if(mss2.getMotCle()==MotCle.USERCONNECTIONSERVEUR)
                {
                    tchat.getCompte().setUsers((HashMap<String, Users>)mss2.getDonnees());
                    tchat.miseajourconnecte();
                }
                else if (mss2.getMotCle() == MotCle.ENVOIROOMS) {
                    tchat.getCompte().setServeurs((HashMap<String, Room>) mss2.getDonnees());
                }
                else if(mss2.getMotCle() == MotCle.CREATIONROOM)
                {
                    tchat.getCompte().setServeurs((HashMap<String, Room>)mss2.getDonnees());
                    tchat.miseajourrooms();
                }
                else if (mss2.getMotCle() == MotCle.ENVOIUSERSSERVEUR) {
                    tchat.getCompte().setUsers((HashMap<String, Users>)mss2.getDonnees());
                }
                else if(mss2.getMotCle()== MotCle.CONNECTIONROOM)
                {
                    tchat.getCompte().setServeurs((HashMap<String, Room>)mss2.getDonnees());
                    for(String us : tchat.getCompte().getServeurs().keySet())
                    {
                        System.out.println(us);
                    }
                    if(tchat1!=null)
                    tchat1.miseajour();
                }
                else if(mss2.getMotCle()==MotCle.MESSAGE)
                tchat1.setJtextpanel(mss2);
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}