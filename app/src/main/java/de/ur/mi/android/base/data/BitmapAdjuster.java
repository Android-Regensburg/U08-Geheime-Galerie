package de.ur.mi.android.base.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

public class BitmapAdjuster {



    public BitmapAdjuster(){

    }

    /**
     * Dreht die Bitmap um die gewünschte Gradzahl.
     * @param image Die zu rotoerende Bitmap.
     * @param degree Die Gradzahl.
     * @return Eine gedrehte Bitmap.
     */
    private Bitmap rotateImage(Bitmap image, int degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        image.recycle();
        return rotatedImage;
    }

    /**
     * Diese Methode gibt eine Bitmap zurück, welche so gedreht ist, dass Sie richtig dargestellt wird
     * und Bilder im Portraitmodus nicht um 90 Grad gedreht angezeigt werden.
     * @param context Der App-Kontext.
     * @param img Die Bitmap des Bildes.
     * @param uri Die Uri des Bildes.
     * @return Eine Bitmap, welche richtig angezigt wird.
     * @throws IOException falls der inputStream oder das ExifInterface nicht erstellt werden können.
     */
    private Bitmap adjustBitmap(Context context, Bitmap img, Uri uri) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        ExifInterface ei = new ExifInterface(input);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }


    /**
     * Liefert eine angepasste Bitmap zurück.
     * @param context Der App-Kontext.
     * @param pathToOriginal Der Pfad zur Bilddatei.
     * @param targetUri Die Uri des Bildes.
     * @return Angepasste Bitmap.
     */
    public Bitmap getAdjustedBitmap(Context context, String pathToOriginal, Uri targetUri) {
        Bitmap map = BitmapFactory.decodeFile(pathToOriginal);
        try {
            map = adjustBitmap(context, map, targetUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
