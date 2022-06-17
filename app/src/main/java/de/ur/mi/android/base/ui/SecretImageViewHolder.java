package de.ur.mi.android.base.ui;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import de.ur.mi.android.base.R;
import de.ur.mi.android.base.data.BitmapAdjuster;
import de.ur.mi.android.base.secret_image.SecretImage;

public class SecretImageViewHolder extends RecyclerView.ViewHolder{

    private final ImageView imageView;
    private final SecretImageViewHolderListener listener;


    public SecretImageViewHolder(View itemView, SecretImageViewHolderListener listener) {
        super(itemView);
        imageView = itemView.findViewById(R.id.grid_item_image_view);
        this.listener = listener;
    }

    public void bindViews(final SecretImage secretImage, Context context) {
        Bitmap bitmap = secretImage.getBitmap(context);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageViewSelected(getAdapterPosition());
                /*
                // Wechseln in die DetailActivity und übergeben des angeklickten SecretImages
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.KEY_SECRET_IMAGE, secretImage);
                context.startActivity(intent);*/
            }
        });
    }

    public interface SecretImageViewHolderListener  {

        void onImageViewSelected(int position);
    }
}
