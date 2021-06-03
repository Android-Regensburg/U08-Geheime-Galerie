package de.ur.mi.android.base;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity für die Detailansicht eines Bildes.
 * Wird gestartet, wenn ein Bild der Galerie ausgewählt wird.
 * */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_detail);
    }
}
