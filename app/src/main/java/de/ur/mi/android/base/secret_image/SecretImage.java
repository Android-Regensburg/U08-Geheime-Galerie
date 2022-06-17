package de.ur.mi.android.base.secret_image;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "secret_image_table")
public class SecretImage implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String imgPath;
    private String description;
    private long date;

    public SecretImage(String imgPath, String description, long date) {
        this.id = UUID.randomUUID().toString();
        this.imgPath = imgPath;
        this.description = description;
        this.date = date;
    }

    public SecretImage(Bitmap bmp, String description, Context context, long date){
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.imgPath = storeBitmapInPrivateFile(bmp, context);
        this.date = date;
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

    public String getImgPath() {
        return imgPath;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    /**Lädt ein Bitmap aus dem internen Speicher basierend auf dem imgPath und gibt es nach außen*/
    public Bitmap getBitmap(Context context){
        Bitmap btm = null;
        try {
            btm = BitmapFactory.decodeStream(context.openFileInput(this.imgPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return btm;
    }

    /**
     * Speichern des Bildes im internen Speicher der App, unzugänglich für andere Apps.
     * Gibt den Namen/ Pfad des abgespeicherten Bildes zurück.
     * Mit "BitmapFactory.decodeStream(CONTEXT.openFileInput(PATH))" kann das Bild aus dem Speicher geladen werden
     * */
    private String storeBitmapInPrivateFile(Bitmap bmp, Context context){
        String fileName = this.id;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
}
