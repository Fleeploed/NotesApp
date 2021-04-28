package com.kingdomredherous.notesapp.NoteFolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kingdomredherous.notesapp.ApiFolder.Note;
import com.kingdomredherous.notesapp.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {
    private List<Note> notes;
    private ItemClickListener itemClickListener;
    private Context context;

    public MyAdapter(List<Note> notes, Context context, ItemClickListener itemClickListener) {
        this.notes = notes;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        TextView tv_title, tv_popis, tv_datum;
        ItemClickListener itemClickListener;
        CardView cardView;

        CustomViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            view = itemView;
            tv_title = view.findViewById(R.id.title);
            tv_popis = view.findViewById(R.id.popis);
            tv_datum = view.findViewById(R.id.datum);
            cardView = view.findViewById(R.id.card_item);
            this.itemClickListener = itemClickListener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new CustomViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tv_title.setText(notes.get(position).getTitle());
        holder.tv_popis.setText(notes.get(position).getPopis());
        holder.tv_datum.setText(notes.get(position).getDatum());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
