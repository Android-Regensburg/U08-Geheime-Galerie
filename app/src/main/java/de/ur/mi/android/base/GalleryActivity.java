package de.ur.mi.android.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.ur.mi.android.base.secret_image.SecretImage;
import de.ur.mi.android.base.secret_image.SecretImageManager;
import de.ur.mi.android.base.ui.SecretImageAdapter;

public class GalleryActivity extends AppCompatActivity implements SecretImageManager.SecretImageManagerListener, SecretImageAdapter.SecretImageAdapterListener {

    private SecretImageAdapter adapter; // Adapter für die RecyclerView
    private SecretImageManager secretImageManager; // verwaltet unsere Daten/ Ermöglicht Trennung von UI und Datenschicht

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initSecretImageManager();
    }


    private void initUI() {
        setContentView(R.layout.activity_gallery);
        initRecyclerView();
        initFloatingActionButton();
        initActivityLauncher();
    }

    /**
     * Wir registrieren die Activity dafür, ein Result einer anderen Activity die mit diesem
     * Launcher gestartet wird zu erhalten und zu verarbeiten.
     */
    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    addNewImageFromIntent(result.getData());
                }
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new SecretImageAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Wir initialsieren den FloatingActionButton und starten bei einem Klick die CreationActivity
     * der App mit Hilfe des ActivityResultLaunchers, um das Ergebnis später verarbeiten zu können.
     */
    private void initFloatingActionButton() {
        FloatingActionButton addPictureBtn = findViewById(R.id.add_picture_button);
        addPictureBtn.setOnClickListener(v -> activityResultLauncher.launch(new Intent(getApplicationContext(), CreationActivity.class)));
    }

    private void initSecretImageManager() {
        secretImageManager = new SecretImageManager(getApplicationContext(), this);
        updateImageListInUI();
    }


    private void startDetailActivityForImage(SecretImage image) {
        Intent intent = new Intent(GalleryActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_SECRET_IMAGE, image);
        startActivity(intent);
    }

    private void addNewImageFromIntent(Intent intent) {
        SecretImage secretImage = (SecretImage) intent.getSerializableExtra(CreationActivity.KEY_SECRET_IMAGE_CREATED);
        secretImageManager.addSecretImage(secretImage);
    }

    private void updateImageListInUI() {
        ArrayList<SecretImage> currentImages = secretImageManager.getSecretImagesList();
        adapter.setImageList(currentImages);
    }

    @Override
    public void onSecretImageListUpdated() {
        runOnUiThread(this::updateImageListInUI);
    }


    @Override
    public void onSecretImageSelected(SecretImage image) {
        startDetailActivityForImage(image);
    }
}