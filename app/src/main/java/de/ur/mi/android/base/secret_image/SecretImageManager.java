package de.ur.mi.android.base.secret_image;
import android.content.Context;
import java.util.ArrayList;
import de.ur.mi.android.base.room.DatabaseHelper;

public class SecretImageManager {

    private final ArrayList<SecretImage> secretImagesList;
    private final DatabaseHelper dbHelper;
    private final SecretImageManagerListener listener;

    public SecretImageManager(Context context, SecretImageManagerListener listener){
        this.listener = listener;
        this.dbHelper = new DatabaseHelper(context);
        this.secretImagesList = dbHelper.getAllSecretImages();
    }

    public ArrayList<SecretImage> getSecretImagesList(){
        return this.secretImagesList;
    }

    public void addSecretImage(SecretImage img){
        secretImagesList.add(img);
        dbHelper.addSecretImage(img);
        listener.onSecretImageListUpdated();
    }

    public interface SecretImageManagerListener{
        void onSecretImageListUpdated();
    }
}
