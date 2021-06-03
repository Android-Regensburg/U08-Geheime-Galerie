package de.ur.mi.android.base.ui;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import de.ur.mi.android.base.R;
import de.ur.mi.android.base.secret_image.SecretImage;

public class SecretImageViewHolder extends RecyclerView.ViewHolder{

    private ImageView imageView;

    public SecretImageViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.grid_item_image_view);
    }

    public void bindViews(final SecretImage secretImage, final Context context) {
        //TODO: Foto in die ImageView laden und bei einem Klick in die DetailActivity wechseln
    }
}
