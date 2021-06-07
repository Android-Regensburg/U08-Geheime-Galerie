package de.ur.mi.android.base.room;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import de.ur.mi.android.base.secret_image.SecretImage;

@Database(entities = {SecretImage.class}, version = 1)
public abstract class SecretImageDatabase extends RoomDatabase {
    /**
     * Abstrakte Methode, das Data Access Object nach außen gibt
     * */
    public abstract SecretImageDAO secretImageDAO();
}
