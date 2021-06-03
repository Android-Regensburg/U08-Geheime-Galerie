package de.ur.mi.android.base;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileNotFoundException;
import de.ur.mi.android.base.secret_image.SecretImage;

public class DetailActivity extends AppCompatActivity {

    private SecretImage secretImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveExtras();
        initUI();
    }

    // Extras des Intents abfangen
    private void receiveExtras(){
        secretImage = (SecretImage) getIntent().getSerializableExtra(GalleryActivity.KEY_SECRET_IMAGE);
    }

    private void initUI() {
        setContentView(R.layout.activity_detail);
        ImageView imageView = findViewById(R.id.image_view);
        TextView imageDescription = findViewById(R.id.image_description);
        imageDescription.setText(secretImage.getDescription());
        imageView.setImageBitmap(secretImage.getBitmap(this));
    }
}
