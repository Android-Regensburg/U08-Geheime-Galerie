package de.ur.mi.android.base;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import de.ur.mi.android.base.secret_image.SecretImage;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_SECRET_IMAGE = "secret-image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_detail);
        ImageView imageView = findViewById(R.id.image_view);
        TextView imageDescription = findViewById(R.id.image_description);
        SecretImage secretImage = (SecretImage) getIntent().getSerializableExtra(KEY_SECRET_IMAGE);
        imageDescription.setText(secretImage.getDescription());
        imageView.setImageBitmap(secretImage.getBitmap(this));
    }
}
