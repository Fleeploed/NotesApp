package com.kingdomredherous.notesapp.trash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kingdomredherous.notesapp.Note;
import com.kingdomredherous.notesapp.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.RecyclerViewAdapter> {

    private List<Note> noteList;

    private Context context;
    private ItemClickListener itemClickListener;

    public MainAdapter(List<Note> noteList, Context context, ItemClickListener itemClickListener) {
        this.noteList = noteList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new RecyclerViewAdapter(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        Note note = noteList.get(position);
        holder.tv_title.setText(note.getTitle());
        holder.tv_popis.setText(note.getPopis());
        holder.tv_datum.setText(note.getDatum());
        holder.cardView.setCardBackgroundColor(R.drawable.itemshape);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_title, tv_popis, tv_datum;
        ItemClickListener itemClickListener;
        CardView cardView;
        RecyclerViewAdapter(View item, ItemClickListener itemClickListener) {
            super(item);
            tv_title = item.findViewById(R.id.title);
            tv_popis = item.findViewById(R.id.popis);
            tv_datum = item.findViewById(R.id.datum);
            cardView = item.findViewById(R.id.card_item);
            this.itemClickListener = itemClickListener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
