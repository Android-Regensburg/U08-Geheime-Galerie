package de.ur.mi.android.base.data;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileRequest {

    File file;

    private FileRequest(Context context) {
        try {
            file = createImageFile(context);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Erstellt ein temporäres File, in welches Bilder zwischengespeichert werden können, solange
     * sie noch bearbeitet werden.
     * @param context App-Kontext
     * @return ein neues File
     * @throws IOException
     */
    private File createImageFile(Context context) throws IOException {
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String dateString = current.format(formatter);
        String imageFileName = "JPEG_" + dateString + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    /**
     * Erstellt eine neues FileRequest-Objekt, welches als one-time use gedacht ist
     * @param context
     * @return
     */
    public static FileRequest fromContext(Context context){
        return new FileRequest(context);
    }

    public Uri createUriForFile(Context context)
    {
        return FileProvider.getUriForFile(context, "de.ur.mi.android.base.provider", file);
    }

    public File getFile(){
        return this.file;
    }
}
