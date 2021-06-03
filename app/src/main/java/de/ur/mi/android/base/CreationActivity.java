package de.ur.mi.android.base;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import de.ur.mi.android.base.secret_image.SecretImage;

public class CreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_TAKE_PHOTO = 1;
    public static final String KEY_SECRET_IMAGE_CREATED = "SECRET_IMAGE_CREATED";
    private EditText descriptionInput;
    private Button saveButton;
    private ImageView imagePreview;

    private Bitmap image = null;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Kamera wird direkt gestartet
        takePictureWithCamera();
        initUI();
    }

    private void initUI(){
        setContentView(R.layout.activity_creation);
        imagePreview = findViewById(R.id.image_preview);
        descriptionInput = findViewById(R.id.description_input);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save_button){
            sendEntryBack();
        }
    }

    private void takePictureWithCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Nur wenn Datei erfolgreich erzeugt wurde, wird der eigentliche Intent gestartet
            if (photoFile != null) {
                //Erzeugen der URI für die Datei mit Hilfe des File Providers, um die Datei für andere Apps zugänglich zu machen bzw. zu übergeben.
                Uri photoURI = FileProvider.getUriForFile(this,
                        "de.ur.mi.android.base.provider",
                        photoFile);
                // Neben der URI erwartet der Intent noch das EXTRA_OUTPUT, um das Bild in Originalgöße speichern zu können
                // Ohne EXTRA_OUTPUT wird das Bild lediglich als Thumbnail zurückgegeben
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            image = BitmapFactory.decodeFile(currentPhotoPath, getBitmapOptions());
            imagePreview.setImageBitmap(image);
        }
        else if(resultCode == RESULT_CANCELED){ // Kamera wurde beendet, ohne ein Bild aufzunehmen
            // Activity beenden und resultCode dementsprechend setzen
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    /**
     * Überprüft, ob alle Eingaben getätigt wurden und schickt die Daten als Resultat zurück an die GalleryActivity
     * */
    private void sendEntryBack(){
        String description = descriptionInput.getText().toString().trim();
        if(description.isEmpty()){
            Toast.makeText(this, "Du hast noch nicht alles ausgefüllt", Toast.LENGTH_SHORT).show();
            return;
        }
        SecretImage secretImage = new SecretImage(image, description, this);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_SECRET_IMAGE_CREATED, secretImage);
        // resultCode und Intent setzen
        setResult(Activity.RESULT_OK, returnIntent);
        // Activity beenden
        finish();
    }

    private BitmapFactory.Options getBitmapOptions() {
        int targetW = imagePreview.getWidth();
        int targetH = imagePreview.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        return bmOptions;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }
}
