package de.ur.mi.android.base.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.ur.mi.android.base.R;
import de.ur.mi.android.base.secret_image.SecretImage;

public class SecretImageAdapter extends RecyclerView.Adapter<SecretImageViewHolder> implements SecretImageViewHolder.SecretImageViewHolderListener {

    private final Context context;
    private final SecretImageAdapterListener listener;
    private ArrayList<SecretImage> secretImages;

    public SecretImageAdapter(Context context, SecretImageAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.secretImages = new ArrayList<>();
    }

    public void setImageList(ArrayList<SecretImage> secretImages) {
        this.secretImages = secretImages;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SecretImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new SecretImageViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(SecretImageViewHolder holder, final int position) {
        SecretImage secretImage = secretImages.get(position);
        holder.bindViews(secretImage, context);
    }

    @Override
    public int getItemCount() {
        return secretImages.size();
    }

    @Override
    public void onImageViewSelected(int position) {
        SecretImage image = secretImages.get(position);
        listener.onSecretImageSelected(image, position);
    }

    public interface SecretImageAdapterListener {
        void onSecretImageSelected(SecretImage image, int position);
    }
}
