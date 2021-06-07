package de.ur.mi.android.base;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import de.ur.mi.android.base.secret_image.SecretImage;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_SECRET_IMAGE = "secret-image";
    private SecretImage secretImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveExtras();
        initUI();
    }

    // Intent und seine Extras abfangen
    private void receiveExtras(){
        secretImage = (SecretImage) getIntent().getSerializableExtra(KEY_SECRET_IMAGE);
    }

    private void initUI() {
        setContentView(R.layout.activity_detail);
        ImageView imageView = findViewById(R.id.image_view);
        TextView imageDescription = findViewById(R.id.image_description);
        imageDescription.setText(secretImage.getDescription());
        imageView.setImageBitmap(secretImage.getBitmap(this));
    }
}
