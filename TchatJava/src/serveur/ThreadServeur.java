package serveur;

public class ThreadServeur extends Thread {

    private final Serveur serveur;

    public ThreadServeur(Serveur s, Integer port) {
        serveur = s;
    }
    
    @Override
    public void run() {
//        serveur.lancerServeur();
    }
}