package de.ur.mi.android.base.ui;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import de.ur.mi.android.base.DetailActivity;
import de.ur.mi.android.base.R;
import de.ur.mi.android.base.secret_image.SecretImage;

public class SecretImageViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;

    public SecretImageViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.grid_item_image_view);
    }

    public void bindViews(final SecretImage secretImage, final Context context) {
        imageView.setImageBitmap(secretImage.getBitmap(context));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wechseln in die DetailActivity und übergeben des angeklickten SecretImages
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.KEY_SECRET_IMAGE, secretImage);
                context.startActivity(intent);
            }
        });
    }
}
