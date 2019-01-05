package br.com.radani.www.mensageiro;

/**
 * Created by kundan on 6/23/2015.
 */
public class Globals {


    private static Globals instance = new Globals();

    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private Boolean notification_index;


    private Globals() {

    }


    public Boolean getValue() {
        return notification_index;
    }


    public void setValue(Boolean notification_index) {
        this.notification_index = notification_index;
    }



}