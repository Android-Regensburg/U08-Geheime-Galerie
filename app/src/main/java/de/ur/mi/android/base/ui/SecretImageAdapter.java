package de.ur.mi.android.base.ui;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import de.ur.mi.android.base.R;
import de.ur.mi.android.base.secret_image.SecretImage;

public class SecretImageAdapter extends RecyclerView.Adapter<SecretImageViewHolder> {

    private ArrayList<SecretImage> dataList;
    private final Activity context;

    public SecretImageAdapter(Activity context){
        this.context = context;
        this.dataList = new ArrayList<>();
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
            SecretImage secretImage = dataList.get(position);
            holder.bindViews(secretImage, context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
