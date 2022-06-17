package de.ur.mi.android.base.room;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import de.ur.mi.android.base.secret_image.SecretImage;

public class DatabaseHelper {

    private static final String DATABASE_NAME = "secret_image_db";
    private final Context context;
    private SecretImageDatabase db;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE secret_image_table ADD COLUMN date INTEGER NOT NULL DEFAULT 0");
        }
    };

    public DatabaseHelper(Context context){
        this.context = context;
        initDatabase();
    }

    private void initDatabase(){
        db = Room.databaseBuilder(context, SecretImageDatabase.class, DATABASE_NAME).addMigrations(MIGRATION_1_2).build();
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

