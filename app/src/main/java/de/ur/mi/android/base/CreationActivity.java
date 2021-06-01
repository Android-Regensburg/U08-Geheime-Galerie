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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import de.ur.mi.android.base.secret_image.SecretImage;

public class CreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_LOAD_IMAGE = 2;
    public static final String KEY_SECRET_IMAGE_CREATED = "SECRET_IMAGE_CREATED";
    private EditText titleInput, descriptionInput;
    private Button cameraButton, galleryButton, saveButton;
    private ImageView imagePreview;
    private Bitmap image = null;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI(){
        setContentView(R.layout.activity_creation);
        imagePreview = findViewById(R.id.image_preview);
        titleInput = findViewById(R.id.title_input);
        descriptionInput = findViewById(R.id.description_input);
        cameraButton = findViewById(R.id.camera_button);
        galleryButton = findViewById(R.id.gallery_button);
        saveButton = findViewById(R.id.save_button);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_button:
                takePictureWithCamera();
                break;
            case R.id.gallery_button:
                loadImageFromGallery();
                break;
            case R.id.save_button:
                sendEntryBack();
                break;
            default:
                break;
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

    private void loadImageFromGallery() {
        Intent loadImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(loadImageIntent, REQUEST_LOAD_IMAGE);
    }

    private void sendEntryBack(){
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        if(!inputValid(title, description)){
            Toast.makeText(this, "Du hast noch nicht alles ausgefüllt", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent returnIntent = new Intent();
        SecretImage secretImage = new SecretImage(null, title, description);
        secretImage.setImgPath(createImageFromBitmap(image, secretImage.getId()));
        returnIntent.putExtra(KEY_SECRET_IMAGE_CREATED, secretImage);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private boolean inputValid(String title, String description){
        return !title.isEmpty() && !description.isEmpty() && image != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ACTIVITY RESULT", "reqCode: " + requestCode + ", resCode: " + resultCode);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            image = BitmapFactory.decodeFile(currentPhotoPath, getBitmapOptions());
            imagePreview.setImageBitmap(image);
        } else if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    image = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri), null, getBitmapOptions());
                    imagePreview.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
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

    // https://stackoverflow.com/Questions/4352172/how-do-you-pass-images-bitmaps-between-android-activities-using-bundles
    private String createImageFromBitmap(Bitmap bmp, String name){
        String fileName = name;//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
}
