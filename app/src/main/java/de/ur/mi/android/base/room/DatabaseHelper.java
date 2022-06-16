package de.ur.mi.android.base.room;
import android.content.Context;
import androidx.room.Room;
import java.util.ArrayList;
import de.ur.mi.android.base.secret_image.SecretImage;

public class DatabaseHelper {

    private static final String DATABASE_NAME = "secret_image_db";
    private final Context context;
    private SecretImageDatabase db;

    public DatabaseHelper(Context context){
        this.context = context;
        initDatabase();
    }

    private void initDatabase(){
        db = Room.databaseBuilder(context, SecretImageDatabase.class, DATABASE_NAME).build();
    }

    /**
     * Einzelnes SecretImage zur Datenbank hinzufügen
     * */
    public void addSecretImage(SecretImage img){
        db.secretImageDAO().insertSecret(img);
    }

    public void deleteSecretImage(SecretImage image){
        db.secretImageDAO().deleteImage(image);
    }

    /**
     * Liste mit allen in der Datenbank gespeicherten SecretImages erhalten
     * */
    public ArrayList<SecretImage> getAllSecretImages(){
        return new ArrayList<>(db.secretImageDAO().getAllSecrets());
    }
}

