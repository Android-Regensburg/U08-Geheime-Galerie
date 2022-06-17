package de.ur.mi.android.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import de.ur.mi.android.base.data.BitmapAdjuster;
import de.ur.mi.android.base.data.FileRequest;
import de.ur.mi.android.base.secret_image.SecretImage;

public class CreationActivity extends AppCompatActivity {

    public static final String KEY_SECRET_IMAGE_CREATED = "SECRET_IMAGE_CREATED";
    private String imagePathForCurrentCameraIntent;
    private ActivityResultLauncher<Intent> launcher;
    private Uri uriForCurrentImage;
    private BitmapAdjuster bitmapAdjuster;

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
            if (result.getResultCode() == Activity.RESULT_OK) {
                updateImagePreview();
            } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                finishActivityWithFailState();
            }
        });
    }


    private void initUI() {
        bitmapAdjuster = new BitmapAdjuster(getApplicationContext());
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
            FileRequest fileRequest = FileRequest.fromContext(getApplicationContext());
            imagePathForCurrentCameraIntent = fileRequest.getFile().getAbsolutePath();
            uriForCurrentImage = fileRequest.createUriForFile(getApplicationContext());
            // Neben der URI erwartet der Intent noch das EXTRA_OUTPUT, um das Bild in Originalgöße speichern zu können
            // Ohne EXTRA_OUTPUT wird das Bild lediglich als Thumbnail zurückgegeben
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriForCurrentImage);
            launcher.launch(takePictureIntent);

        }
    }


    private SecretImage createSecretImageFromCurrentValues() {
        EditText descriptionInput = findViewById(R.id.description_input);
        String description = descriptionInput.getText().toString().trim();
        if (description.isEmpty()) {
            return null;
        }
        Bitmap map = bitmapAdjuster.getAdjustedBitmap(imagePathForCurrentCameraIntent, uriForCurrentImage);
        return new SecretImage(map, description, this, ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    private void updateImagePreview() {
        ImageView imagePreview = findViewById(R.id.image_preview);
        // Die Verwendung vorgegebener Dimensionen für das neue Bild via BitmapFactory.Options
        // wäre sicherer und an dieser Stelle auch ausreichend

        Bitmap map = bitmapAdjuster.getAdjustedBitmap(imagePathForCurrentCameraIntent, uriForCurrentImage);
        imagePreview.setImageBitmap(map);
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
