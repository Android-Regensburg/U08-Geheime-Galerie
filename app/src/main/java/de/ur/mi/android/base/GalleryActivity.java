package de.ur.mi.android.base;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.ur.mi.android.base.secret_image.SecretImageManager;
import de.ur.mi.android.base.ui.SecretImageAdapter;

public class GalleryActivity extends AppCompatActivity implements SecretImageManager.SecretImageManagerListener {

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
                //TODO: CreationActivity starten, welche ein SecretImage als Resultat liefert
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
}