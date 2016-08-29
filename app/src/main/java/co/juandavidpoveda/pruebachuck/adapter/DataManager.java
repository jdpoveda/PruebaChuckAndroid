package co.juandavidpoveda.pruebachuck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.juandavidpoveda.pruebachuck.R;
import co.juandavidpoveda.pruebachuck.database.Frase;

/**
 * Created by JUAN DAVID on 28/08/2016.
 */
public class DataManager extends RecyclerView.Adapter<DataManager.RecyclerViewHolder> {

    private List<Frase> frasesList;
    private Context context;
    private DeleteFraseListener listener;

    public DataManager(List<Frase> frasesList, Context context, DeleteFraseListener listener){
        this.frasesList = frasesList;
        this.context = context;
        this.listener = listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView fraseItem;
        ImageView imagenItem;
        ImageView deleteItem;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            fraseItem = (TextView) itemView.findViewById(R.id.fraseItemTextView);
            imagenItem = (ImageView) itemView.findViewById(R.id.imagenItem);
            deleteItem = (ImageView) itemView.findViewById(R.id.deleteItemImageView);
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.frase_item, viewGroup, false);
        return new RecyclerViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {
        Frase frase = frasesList.get(position);

        Picasso.with(context)
                .load(frase.getIcon_url())
                .into(viewHolder.imagenItem, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });
        viewHolder.fraseItem.setText(frase.getValue());
        viewHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("*****Delete Item clicked");
                listener.onRemovePFraseClick(frasesList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return frasesList.size();
    }

    public void removeDeletedItem(int pos){
        frasesList.remove(pos);
    }

    public int tamanoList(){
        return frasesList.size();
    }

    public interface DeleteFraseListener {

        public void onRemovePFraseClick(Frase frase, int position);

    }
}