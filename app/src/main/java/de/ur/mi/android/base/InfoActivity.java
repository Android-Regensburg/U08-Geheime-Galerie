package de.ur.mi.android.base;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import de.ur.mi.android.base.secret_image.SecretImage;
import de.ur.mi.android.base.ui.DeleteDialogFragment;

public class InfoActivity extends AppCompatActivity implements DeleteDialogFragment.DeleteDialogListener{

    TextView txtDate, txtDescription;
    private SecretImage currentImage;
    private int imagePosition;
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initUI();
        initData();
        fillViewsWithData();

    }

    private void initData() {
        currentImage = (SecretImage) getIntent().getSerializableExtra(FullscreenViewActivity.KEY_SECRET_IMAGE);
        imagePosition = getIntent().getIntExtra(FullscreenViewActivity.KEY_IMAGE_POSITION, -99);
    }

    /**
     * Beim klicken des Buttons wird der DeleteDialog angezeigt. In diesem setzen wir die Activity als
     * Listener, damit wir das Ergebnis durch die Methoden des interface verarbeiten können.
     */
    private void initUI() {
        txtDate = findViewById(R.id.txt_date);
        txtDescription = findViewById(R.id.txt_description);
        btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v-> {
            DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
            deleteDialogFragment.setSecretImage(currentImage);
            deleteDialogFragment.show(getSupportFragmentManager(), "Info::DeleteDialog");
        });
    }

    private void fillViewsWithData(){
        ZonedDateTime captureTime = Instant.ofEpochMilli(currentImage.getDate()).atZone(ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        if (currentImage.getDate() == 0) {
            txtDate.setText("Keine Daten vorhanden");
        } else {
            txtDate.setText(dateTimeFormatter.format(captureTime));
        }
        txtDescription.setText(currentImage.getDescription());
    }



    @Override
    public void onDeleteConfirmed() {
        Intent i = new Intent();
        i.putExtra(FullscreenViewActivity.KEY_TO_BE_DELETED, true);
        i.putExtra(FullscreenViewActivity.KEY_SECRET_IMAGE, currentImage);
        i.putExtra(FullscreenViewActivity.KEY_IMAGE_POSITION, imagePosition);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}