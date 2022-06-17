package de.ur.mi.android.base;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.ur.mi.android.base.secret_image.SecretImage;

public class FullscreenViewActivity extends AppCompatActivity{

    public static final String KEY_TO_BE_DELETED = "keyToBeDeleted";
    public static final String KEY_IMAGE_POSITION = "keyImagePosition";
    public static final String KEY_SECRET_IMAGE = "keySecretImage";
    SecretImage currentImage;
    int imagePosition;

    ActivityResultLauncher<Intent> infoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initLauncher();
    }

    private void initUI() {
        setContentView(R.layout.activity_view_fullscreen);
        ImageView imageView = findViewById(R.id.image_view);
        currentImage = (SecretImage) getIntent().getSerializableExtra(KEY_SECRET_IMAGE);
        imagePosition = getIntent().getIntExtra(KEY_IMAGE_POSITION, -99);
        imageView.setImageBitmap(currentImage.getBitmap(this));
        FloatingActionButton fabDelete = findViewById(R.id.fab_start_info);
        fabDelete.setOnClickListener(v -> {
            startInfoActivity();
        });
    }

    private void startInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra(KEY_IMAGE_POSITION, imagePosition);
        intent.putExtra(KEY_SECRET_IMAGE, currentImage);
        infoLauncher.launch(intent);
    }

    private void initLauncher() {
        infoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK){
                if (result.getData() != null){
                    if (result.getData().getBooleanExtra(KEY_TO_BE_DELETED, false)){
                        Intent i = new Intent();
                        i.putExtra(KEY_SECRET_IMAGE, currentImage);
                        i.putExtra(KEY_IMAGE_POSITION, imagePosition);
                        i.putExtra(KEY_TO_BE_DELETED, true);
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }
                }
            }
        });
    }


}
