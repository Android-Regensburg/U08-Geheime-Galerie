package de.ur.mi.android.base.ui;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import de.ur.mi.android.base.DetailActivity;
import de.ur.mi.android.base.GalleryActivity;
import de.ur.mi.android.base.R;
import de.ur.mi.android.base.secret_image.SecretImage;

public class SecretImageAdapter extends RecyclerView.Adapter<SecretImageAdapter.SecretImageViewHolder> {

    private ArrayList<SecretImage> dataList;
    private final Activity context;

    public SecretImageAdapter(Activity context, ArrayList<SecretImage> list){
        this.context = context;
        this.dataList = list;
    }

    public void updateData(ArrayList<SecretImage> newDataList){
        this.dataList = newDataList;
        this.notifyDataSetChanged();
    }

    @Override
    public SecretImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        SecretImageViewHolder viewHolder = new SecretImageViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SecretImageViewHolder holder, final int position) {
        try {
            //holder.imageTitle.setText(dataList.get(position).getTitle());
            Bitmap image = BitmapFactory.decodeStream(context.openFileInput(dataList.get(position).getImgPath()));
            holder.imageView.setImageBitmap(image);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SecretImage secretImage = dataList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(GalleryActivity.KEY_SECRET_IMAGE, secretImage);
                    context.startActivity(intent);

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class SecretImageViewHolder extends RecyclerView.ViewHolder{

        //TextView imageTitle;
        ImageView imageView;

        public SecretImageViewHolder(View itemView) {
            super(itemView);
            //imageTitle = itemView.findViewById(R.id.grid_item_title);
            imageView = itemView.findViewById(R.id.grid_item_image_view);
        }
    }
}
