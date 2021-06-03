package de.ur.mi.android.base;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class CreationActivity extends AppCompatActivity{

    private EditText descriptionInput;
    private Button saveButton;
    private ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Kamera starten
        initUI();
    }

    private void initUI(){
        setContentView(R.layout.activity_creation);
        imagePreview = findViewById(R.id.image_preview);
        descriptionInput = findViewById(R.id.description_input);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Speichern des Bildes im internen Speicher der App, unzugänglich für andere Apps.
     * Gibt den Namen/ Pfad des abgespeicherten Bildes zurück
     * Mit "BitmapFactory.decodeStream(CONTEXT.openFileInput(PATH))" kann das Bild über den Pfad geladen werden
     * */
    private String storeBitmapInPrivateFile(Bitmap bmp){
        String fileName = String.valueOf(System.currentTimeMillis());
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
}
