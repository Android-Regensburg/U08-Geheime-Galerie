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

    private void receiveExtras(){
        secretImage = (SecretImage) getIntent().getSerializableExtra(GalleryActivity.KEY_SECRET_IMAGE);
    }

    private void initUI() {
        setContentView(R.layout.activity_detail);
        TextView imageTitle = findViewById(R.id.image_title);
        TextView imageDescription = findViewById(R.id.image_description);
        ImageView imageView = findViewById(R.id.image_view);

        try {
            imageView.setImageBitmap(BitmapFactory.decodeStream(getApplicationContext().openFileInput(secretImage.getImgPath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageTitle.setText(secretImage.getTitle());
        imageDescription.setText(secretImage.getDescription());
    }
}
