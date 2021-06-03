package de.ur.mi.android.base.secret_image;
import java.io.Serializable;
import java.util.UUID;

/**
 * Stellt einen einzelnen Eintrag in der Bildergalerie dar.
 * Das
 * */
public class SecretImage implements Serializable {
    private String id;
    private String imgPath;
    private String description;


    public SecretImage(String imgPath, String description) {
        this.id = UUID.randomUUID().toString();
        this.imgPath = imgPath;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
