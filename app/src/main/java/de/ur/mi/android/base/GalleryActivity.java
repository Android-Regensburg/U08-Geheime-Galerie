package de.ur.mi.android.base;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.ur.mi.android.base.secret_image.SecretImage;
import de.ur.mi.android.base.secret_image.SecretImageManager;
import de.ur.mi.android.base.ui.SecretImageAdapter;

public class GalleryActivity extends AppCompatActivity implements SecretImageManager.SecretImageManagerListener, SecretImageAdapter.SecretImageAdapterListener {

    private static final int REQUEST_CREATE_SECRET_IMAGE = 1;
    private SecretImageAdapter adapter; // Adapter für die RecyclerView
    private SecretImageManager secretImageManager; // verwaltet unsere Daten/ Ermöglicht Trennung von UI und Datenschicht

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initSecretImageManager();
    }

    private void initUI(){
        setContentView(R.layout.activity_gallery);
        initRecyclerView();
        initFloatingActionButton();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new SecretImageAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
    }

    private void initFloatingActionButton() {
        FloatingActionButton addPictureBtn = findViewById(R.id.add_picture_button);
        addPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreationActivityForNewImage();
            }
        });
    }

    private void initSecretImageManager(){
        secretImageManager = new SecretImageManager(getApplicationContext(), this);
        updateImageListInUI();
    }

    private void startCreationActivityForNewImage() {
        Intent i = new Intent(GalleryActivity.this, CreationActivity.class);
        startActivityForResult(i, REQUEST_CREATE_SECRET_IMAGE);
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
        updateImageListInUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CREATE_SECRET_IMAGE){
            if(resultCode == RESULT_OK){
                addNewImageFromIntent(data);
            }
        }
    }

    @Override
    public void onSecretImageSelected(SecretImage image) {
        startDetailActivityForImage(image);
    }
}