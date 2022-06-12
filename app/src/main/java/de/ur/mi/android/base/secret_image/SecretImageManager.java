package de.ur.mi.android.base.secret_image;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

import de.ur.mi.android.base.room.DatabaseExecutor;
import de.ur.mi.android.base.room.DatabaseHelper;

public class SecretImageManager {

    private final ArrayList<SecretImage> secretImagesList;
    private final DatabaseExecutor dbExecutor;
    private final SecretImageManagerListener listener;

    public SecretImageManager(Context context, SecretImageManagerListener listener){
        this.listener = listener;
        this.dbExecutor = new DatabaseExecutor(context);
        this.secretImagesList = new ArrayList<>();
        dbExecutor.loadAllSecretImages(new DatabaseExecutor.DataLoadListener() {
            @Override
            public void onDataLoaded(List<SecretImage> loadedImages) {
                secretImagesList.addAll(loadedImages);
                listener.onSecretImageListUpdated();
            }
        });
    }

    public ArrayList<SecretImage> getSecretImagesList(){
        return this.secretImagesList;
    }

    public void addSecretImage(SecretImage img){
        secretImagesList.add(img);
        dbExecutor.insertSecretImage(img);
        listener.onSecretImageListUpdated();
    }

    public interface SecretImageManagerListener{
        void onSecretImageListUpdated();
    }
}
