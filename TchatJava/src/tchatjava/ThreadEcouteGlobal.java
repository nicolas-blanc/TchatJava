package tchatjava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import Rooms.Room;
import javax.swing.JDialog;

public class ThreadEcouteGlobal extends Thread {

    private final TchatCreationServeur tchat;
    private Tchat tchat1;

    public ThreadEcouteGlobal(TchatCreationServeur tchat) {
        this.tchat = tchat;
        tchat1 = null;
    }

    public TchatCreationServeur getServeur() {
        return tchat;
    }

    public void setTchat(Tchat tc) {
        tchat1 = tc;
    }

    @Override
    public void run() {
        
            while (tchat.getCompte().getOuvert()) {
                try {
                //Scanner sc = new Scanner(System.in);
                Message mss2 = (Message) tchat.getCompte().getEntree().readObject();
                switch (mss2.getMotCle()) {
                    case MESSAGEGLOBAL:
                        tchat.setJtextpanel(mss2);
                        break;
                    case USERCONNECTIONSERVEUR:
                        tchat.getCompte().setUsers((ArrayList<String>) mss2.getDonnees());
                        tchat.miseajourconnecte();
                        break;
                    case ENVOIROOMS:
                        tchat.getCompte().setServeurs((HashMap<String, Room>) mss2.getDonnees());
                        tchat.miseajourrooms();
                        break;
                    case CONNECTIONROOM:
                        tchat.getCompte().setServeurs((HashMap<String, Room>) mss2.getDonnees());
                        if (tchat1 != null) {
                            tchat1.miseajour();
                        }
                        break;
                    case MESSAGE:
                        tchat1.setJtextpanel(mss2);
                        tchat1.miseajour();
                        break;
                    case BAN :
                        if (tchat1 != null) {
                            tchat1.dispose();
                        }
                        tchat.dispose();
                        MessageErreur me = new MessageErreur();
                        me.setText("vous avez été déconnecté du serveur");
                        me.setVisible(true);
                        break;
                    default:
                        //déconnection client
                        System.out.println("erreur");
                        break;
                }
        } catch (IOException | ClassNotFoundException ex ) {
            Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
}
