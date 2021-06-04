package de.ur.mi.android.base;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.ur.mi.android.base.secret_image.SecretImage;
import de.ur.mi.android.base.secret_image.SecretImageManager;
import de.ur.mi.android.base.ui.SecretImageAdapter;

public class GalleryActivity extends AppCompatActivity implements SecretImageManager.SecretImageManagerListener {

    public static final String KEY_SECRET_IMAGE = "SECRET_IMAGE";
    private static final int REQUEST_CREATE_SECRET_IMAGE = 1;
    private static final int NUMBER_OF_COLUMNS = 3; // Anzahl der Spalten in dem Raster unserer RecyclerView
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
        FloatingActionButton addPictureBtn = findViewById(R.id.add_picture_button);
        addPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wechseln in die CreationActivity
                Intent i = new Intent(getApplicationContext(), CreationActivity.class);
                startActivityForResult(i, REQUEST_CREATE_SECRET_IMAGE);
            }
        });
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new SecretImageAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void initSecretImageManager(){
        secretImageManager = new SecretImageManager(getApplicationContext(), this);
        secretImageManager.requestUpdate();
    }

    @Override
    public void onSecretImageListUpdated() {
        adapter.updateData(secretImageManager.getSecretImagesList());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CREATE_SECRET_IMAGE){
            if(resultCode == RESULT_OK){
                // Erstelltes SecretImage über den übergebenen Intent holen und an den secretImageManager weitergeben
                SecretImage secretImage = (SecretImage) data.getSerializableExtra(CreationActivity.KEY_SECRET_IMAGE_CREATED);
                secretImageManager.addSecretImage(secretImage);
            }
        }
    }
}