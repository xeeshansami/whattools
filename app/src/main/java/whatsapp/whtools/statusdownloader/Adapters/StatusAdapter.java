package whatsapp.whtools.statusdownloader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import whatsapp.whtools.statusdownloader.Model.StatusModel;
import whatsapp.whtools.statusdownloader.R;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<StatusModel> mAlbum;
    private final Context context;

    public StatusAdapter(ArrayList<StatusModel> mAlbum, Context context) {
        this.mAlbum = mAlbum;
        this.context = context;
    }

    @NonNull
    @Override
    public StatusAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_status_card,parent,false);
        return new StatusAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {


        final ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
        imageViewHolder.iv_Album.setImageResource(mAlbum.get(position).getStatusPath());

     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
    }



    @Override
    public int getItemCount() {
        return mAlbum.size();

    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_Album;


        public ImageViewHolder(View itemView) {
            super(itemView);
            iv_Album = itemView.findViewById(R.id.status_downIMG);


        }
    }
}


