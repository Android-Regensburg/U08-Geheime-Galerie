package de.ur.mi.android.base.room;

import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.ur.mi.android.base.secret_image.SecretImage;

public class DatabaseExecutor {

    private final DatabaseHelper helper;

    public DatabaseExecutor(Context context){
        helper = new DatabaseHelper(context);
    }

    public void insertSecretImage(SecretImage secretImage){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
            helper.addSecretImage(secretImage);
        });
    }

    public void deleteSecretImage(SecretImage image){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> helper.deleteSecretImage(image));
    }

    public void loadAllSecretImages(DataLoadListener listener){
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> listener.onDataLoaded(helper.getAllSecretImages()));
    }

    public interface DataLoadListener{
        void onDataLoaded(List<SecretImage> loadedImages);
    }

}
