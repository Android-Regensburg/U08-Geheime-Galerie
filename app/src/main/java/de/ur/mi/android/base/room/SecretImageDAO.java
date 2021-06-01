package de.ur.mi.android.base.room;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import de.ur.mi.android.base.secret_image.SecretImage;

@Dao
public interface SecretImageDAO {
    @Insert
    void insertSecret(SecretImage secret);

    @Query("SELECT * FROM secret_image_table")
    List<SecretImage> getAllSecrets();
}
