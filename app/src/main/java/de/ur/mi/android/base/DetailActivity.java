package de.ur.mi.android.base;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.ur.mi.android.base.secret_image.SecretImage;
import de.ur.mi.android.base.ui.DeleteDialogFragment;

public class DetailActivity extends AppCompatActivity implements DeleteDialogFragment.DeleteDialogListener {

    public static final String KEY_TO_BE_DELETED = "keyToBeDeleted";
    public static final String KEY_SECRET_IMAGE = "secret-image";
    SecretImage currentImage;
    private FloatingActionButton fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_detail);
        ImageView imageView = findViewById(R.id.image_view);
        TextView imageDescription = findViewById(R.id.image_description);
        currentImage = (SecretImage) getIntent().getSerializableExtra(KEY_SECRET_IMAGE);
        imageDescription.setText(currentImage.getDescription());
        imageView.setImageBitmap(currentImage.getBitmap(this));
        fabDelete = findViewById(R.id.fab_delete);
        fabDelete.setOnClickListener(v -> {
            DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
            deleteDialogFragment.show(getSupportFragmentManager(), "Detail::DeleteDialog");
        });
    }

    @Override
    public void onDeleteConfirmed() {
        Intent i = new Intent();
        i.putExtra(KEY_TO_BE_DELETED, true);
        i.putExtra(KEY_SECRET_IMAGE, currentImage);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
