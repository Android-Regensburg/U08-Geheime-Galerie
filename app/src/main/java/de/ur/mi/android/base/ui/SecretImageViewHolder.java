package de.ur.mi.android.base.ui;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.FileNotFoundException;
import de.ur.mi.android.base.DetailActivity;
import de.ur.mi.android.base.GalleryActivity;
import de.ur.mi.android.base.R;
import de.ur.mi.android.base.secret_image.SecretImage;

public class SecretImageViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;

    public SecretImageViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.grid_item_image_view);
    }

    public void bindViews(final SecretImage secretImage, final Context context) throws FileNotFoundException {
        imageView.setImageBitmap(secretImage.getBitmap(context));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(GalleryActivity.KEY_SECRET_IMAGE, secretImage);
                context.startActivity(intent);
            }
        });
    }
}
