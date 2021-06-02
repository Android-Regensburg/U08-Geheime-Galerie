package de.ur.mi.android.base.secret_image;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "secret_image_table")
public class SecretImage implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String imgPath;
    private String description;


    public SecretImage(String imgPath, String description) {
        this.id = UUID.randomUUID().toString();
        this.imgPath = imgPath;
        this.description = description;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
