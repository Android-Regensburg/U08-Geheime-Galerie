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

    Context context;

    public BitmapAdjuster(Context context){
        this.context = context;
    }

    private Bitmap rotateImage(Bitmap image, int degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImage = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        image.recycle();
        return rotatedImage;
    }

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

    public Bitmap getAdjustedBitmap(String pathToOriginal, Uri targetUri) {
        Bitmap map = BitmapFactory.decodeFile(pathToOriginal);
        try {
            map = adjustBitmap(context, map, targetUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
