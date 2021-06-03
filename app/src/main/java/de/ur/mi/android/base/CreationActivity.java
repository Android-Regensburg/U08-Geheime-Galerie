package de.ur.mi.android.base;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity zur Erstellung eines neuen Galerie-Eintrags
 * */
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
                //TODO SecretImage erstellen und an GalleryActivity zurückgeben
            }
        });
    }
}
