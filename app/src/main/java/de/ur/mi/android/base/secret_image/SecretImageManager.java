package de.ur.mi.android.base.secret_image;
import android.content.Context;
import java.util.ArrayList;

/**
 * Klasse zur Trennung von UI und Daten.
 * Verwaltet die Daten (Liste mit secretImages) und den Zugriff auf diese.
 * */
public class SecretImageManager {

    private final ArrayList<SecretImage> secretImagesList;
    private final SecretImageManagerListener listener;

    public SecretImageManager(Context context, SecretImageManagerListener listener){
        this.listener = listener;
        this.secretImagesList = new ArrayList<>();
    }

    /**Einmaliges Informieren des Listeners*/
    public void requestUpdate(){
        listener.onSecretImageListUpdated();
    }

    public ArrayList<SecretImage> getSecretImagesList(){
        return this.secretImagesList;
    }

    public void addSecretImage(SecretImage img){
        secretImagesList.add(img);
        listener.onSecretImageListUpdated();
    }

    public interface SecretImageManagerListener{
        void onSecretImageListUpdated();
    }
}
