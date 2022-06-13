package de.ur.mi.android.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

import de.ur.mi.android.base.secret_image.SecretImage;

public class CreationActivity extends AppCompatActivity {

    public static final String KEY_SECRET_IMAGE_CREATED = "SECRET_IMAGE_CREATED";
    private String imagePathForCurrentCameraIntent;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initLauncher();
        startCameraIntentForPicture();
    }

    /**
     * Wir regsitrieren die Activity dafür ein Ergebnis einer anderen Activity welches mit dem
     * Launcher gestartet wird zu erhalten und verarbeiten.
     */
    private void initLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK){
                updateImagePreview();
            }
            else if (result.getResultCode() == Activity.RESULT_CANCELED){
                finishActivityWithFailState();
            }
        });
    }

    private void initUI() {
        setContentView(R.layout.activity_creation);
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Refactor and move content to own method
                SecretImage image = createSecretImageFromCurrentValues();
                if (image != null) {
                    finishActivityWithImageResult(image);
                } else {
                    Toast.makeText(getApplicationContext(), "Du hast noch nicht alles ausgefüllt", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Wir erstellen einen Intent welcher eine Kamera-App für uns öffnet. Durch das starten der
     * Kamera-App mit dem ActivityResultLauncher können wir uns das Ergebnis (in diesem Fall das
     * gemachte Bild) zurückliefern lassen und verarbeiten.
     */
    private void startCameraIntentForPicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File imageFile = createImageFile();
                imagePathForCurrentCameraIntent = imageFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(this, "de.ur.mi.android.base.provider", imageFile);
                // Neben der URI erwartet der Intent noch das EXTRA_OUTPUT, um das Bild in Originalgöße speichern zu können
                // Ohne EXTRA_OUTPUT wird das Bild lediglich als Thumbnail zurückgegeben
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                launcher.launch(takePictureIntent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    
    private File createImageFile() throws IOException {
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String dateString = current.format(formatter);
        String imageFileName = "JPEG_" + dateString + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private SecretImage createSecretImageFromCurrentValues() {
        EditText descriptionInput = findViewById(R.id.description_input);
        String description = descriptionInput.getText().toString().trim();
        if (description.isEmpty()) {
            return null;
        }
        Bitmap image = BitmapFactory.decodeFile(imagePathForCurrentCameraIntent);
        return new SecretImage(image, description, this);
    }

    private void updateImagePreview() {
        ImageView imagePreview = findViewById(R.id.image_preview);
        // Die Verwendung vorgegebener Dimensionen für das neue Bild via BitmapFactory.Options
        // wäre sicherer und an dieser Stelle auch ausreichend
        imagePreview.setImageBitmap(BitmapFactory.decodeFile(imagePathForCurrentCameraIntent));
    }

    private void finishActivityWithImageResult(SecretImage image) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_SECRET_IMAGE_CREATED, image);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void finishActivityWithFailState() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }


}
