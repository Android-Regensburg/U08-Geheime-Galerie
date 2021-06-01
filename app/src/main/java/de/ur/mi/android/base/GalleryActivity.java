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
    private static final int REQUEST_CODE = 1;
    private static final int NUMBER_OF_COLUMNS = 3;
    private SecretImageAdapter adapter;
    private SecretImageManager secretImageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initSecretImageManager();
    }

    private void initUI(){
        setContentView(R.layout.activity_gallery);
        FloatingActionButton addPictureBtn = findViewById(R.id.add_picture_button);
        addPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreationActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), NUMBER_OF_COLUMNS);
        // layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SecretImageAdapter(this, null);
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
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                SecretImage si = (SecretImage) data.getSerializableExtra(CreationActivity.KEY_SECRET_IMAGE_CREATED);
                secretImageManager.addSecretImage(si);
            }
        }
    }
}