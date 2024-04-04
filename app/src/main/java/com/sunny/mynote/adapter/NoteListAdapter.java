package com.sunny.mynote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sunny.mynote.NotesClickListener;
import com.sunny.mynote.R;
import com.sunny.mynote.model.NoteViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteListAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<NoteViewModel>list;

    // on click listner
    NotesClickListener listener;


    public NoteListAdapter(Context context, List<NoteViewModel> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textViewTitle.setText(list.get(position).getTitle());
        holder.textViewTitle.setSelected(true);

        holder.textViewNotes.setText(list.get(position).getNotes());

        holder.textViewDate.setText(list.get(position).getDate());
        holder.textViewDate.setSelected(true);

        if(list.get(position).isPinned()){
            holder.imageViewPin.setImageResource(R.drawable.pin);
        }
        else {
            holder.imageViewPin.setImageResource(0);
        }

        int colorCode = getRandomColor();
        holder.notesContainer.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode,null));

        holder.notesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));

            }
        });

        holder.notesContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notesContainer);
                return true;
            }
        });

    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>() ;

        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);

        Random random = new Random();

        int randomColor = random.nextInt(colorCode.size());

        return colorCode.get(randomColor);

     }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<NoteViewModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }
}
class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notesContainer;
    TextView textViewTitle, textViewNotes, textViewDate;
    ImageView imageViewPin;
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notesContainer = itemView.findViewById(R.id.notesContainer);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewNotes = itemView.findViewById(R.id.textViewNotes);
        textViewDate = itemView.findViewById(R.id.textViewDate);
       imageViewPin = itemView.findViewById(R.id.imageViewPin);

    }
}