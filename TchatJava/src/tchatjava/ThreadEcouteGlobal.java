package tchatjava;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import Rooms.Room;
import java.util.concurrent.*;

public class ThreadEcouteGlobal extends Thread {

    private final TchatCreationServeur tchat;
    private HashMap<String, Tchat> tchat1;

    public ThreadEcouteGlobal(TchatCreationServeur tchat) {
        this.tchat = tchat;
        tchat1 = new HashMap<>();
    }
    
    public HashMap<String, Tchat> getTchat()
    {
        return tchat1;
    }

    public TchatCreationServeur getServeur() {
        return tchat;
    }

    public void setTchat(Tchat tc, String room) {
        tchat1.put(room ,tc);
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
                        tchat.getCompte().getUsers().add(mss2.getPseudo());
                        tchat.miseajourconnecte();
                        break;
                    case DECONNECTIONUSER:
                        tchat.getCompte().getUsers().remove(mss2.getPseudo());
                        tchat.miseajourconnecte();
                        break;
                    case ENVOIROOMS:
                        tchat.getCompte().setServeurs(((HashMap<String, Room>) mss2.getDonnees()));
                        tchat.miseajourrooms();
                        break;
                    case ENVOIUSER:
                        tchat.getCompte().setUsers(((LinkedBlockingQueue<String>) mss2.getDonnees()));
                        tchat.miseajourconnecte();
                        break;
                    case CREATIONROOM:
                        tchat.getCompte().getServeurs().put(mss2.getRoom(), new Room(mss2.getPseudo()));
                        tchat.miseajourrooms();
                        if (!tchat1.isEmpty() && tchat1.containsKey(mss2.getRoom())) {
                            tchat1.get(mss2.getRoom()).miseajour();
                        }
                        break;
                    case CONNECTIONROOM:
                        tchat.getCompte().getServeurs().
                                get(mss2.getRoom()).
                                getUtilisateurs().
                                add(mss2.getPseudo());
                        if (!tchat1.isEmpty() && tchat1.containsKey(mss2.getRoom())) {
                            tchat1.get(mss2.getRoom()).miseajour();
                        }
                        break;
                    case MESSAGE:
                        tchat1.get(mss2.getRoom()).setJtextpanel(mss2);
                        tchat1.get(mss2.getRoom()).miseajour();
                        break;
                    case BAN:
                        if (!tchat1.isEmpty() && tchat1.containsKey(mss2.getRoom())) {
                            tchat1.get(mss2.getRoom()).dispose();
                        }
                        tchat.dispose();
                        MessageErreur me = new MessageErreur();
                        me.setText("vous êtes bannis du serveur");
                        me.setVisible(true);
                        me.setBan();
                        System.exit(0);
                        break;
                    case BANROOM:
                        tchat.getCompte().getServeurs().get(mss2.getRoom()).setBannis(mss2.getPseudo());
                        tchat.getCompte().getServeurs().get(mss2.getRoom()).getUtilisateurs().remove(mss2.getPseudo());
                        if (!tchat1.isEmpty() && tchat1.containsKey(mss2.getRoom())) {
                            tchat1.get(mss2.getRoom()).miseajour();
                            if (tchat.getCompte().getSave().getPseudos().
                                    get(tchat.getCompte().getSave().getPseudos().size() - 1).equals(mss2.getPseudo())) {
                                tchat1.get(mss2.getRoom()).dispose();
                                tchat1.remove(mss2.getRoom());
                                MessageErreur mes = new MessageErreur();
                                mes.setText("vous êtes bannis de la room");
                                mes.setVisible(true);
                            }
                        }
                        break;
                    case BANNIROOM:
                        tchat1.get(mss2.getRoom()).dispose();
                        tchat1.remove(mss2.getRoom());
                        MessageErreur mes = new MessageErreur();
                        mes.setText("vous êtes bannis de la room");
                        mes.setVisible(true);
                        break;
                    case DEBANROOM:
                        tchat.getCompte().getServeurs().get(mss2.getRoom()).getBannis().remove(mss2.getPseudo());
                        if (!tchat1.isEmpty() && tchat1.containsKey(mss2.getRoom()))
                            tchat1.get(mss2.getRoom()).miseajour();
                        break;
                    case DECONNECTIONUSERROOM:
                        tchat.getCompte().getServeurs().get(mss2.getRoom()).getUtilisateurs().remove(mss2.getPseudo());
                        if (!tchat1.isEmpty() && tchat1.containsKey(mss2.getRoom())) {
                            tchat1.get(mss2.getRoom()).miseajour();
                            if(tchat.getCompte().getSave().getPseudos().
                                    get(tchat.getCompte().getSave().getPseudos().size() - 1).equals(mss2.getPseudo()))
                            this.tchat1.remove(mss2.getRoom());
                        }
                        break;
                    default:
                        //déconnection client
                        System.out.println("erreur");
                        break;
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
