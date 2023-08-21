package com.example.noteapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;
import com.example.noteapp.model.Folder;
import com.example.noteapp.model.Note;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{
    private List<Note> listNotes;
    private Context context;
    private Folder folder = null;
    public NotesAdapter(List<Note> listNotes, Context context){
        this.listNotes = listNotes;
        this.context = context;
    }
    public NotesAdapter(List<Note> listNotes, Context context, Folder folder){
        this.listNotes = listNotes;
        this.context = context;
        this.folder = folder;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        NotesAdapter.NotesViewHolder viewHolder = new NotesAdapter.NotesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = listNotes.get(position);
        if(note == null){
            return;
        }

        Document document = (Document) Jsoup.parse("<p>"+note.getContext()+"</p>");
        String plainText = document.text();
        holder.tvContextNote.setText(plainText);

        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z", Locale.US);
        try {
            Date date = inputFormat.parse(note.getDate().toString());

            // Định dạng lại thời gian theo định dạng mong muốn
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.US);
            String formattedTime = outputFormat.format(date);

            holder.tvTimeNote.setText(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (folder != null){
            holder.tvNameFolderNote.setText(folder.getNameFolder());
        }

    }

    @Override
    public int getItemCount() {
        if(listNotes != null){
            return listNotes.size();
        }
        return 0;
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameNote, tvTimeNote, tvContextNote, tvNameFolderNote;
        private ImageView imgLockNote;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameNote  = itemView.findViewById(R.id.tvnameitemnote);
            tvContextNote= itemView.findViewById(R.id.tvcontextitemnote);
            tvTimeNote = itemView.findViewById(R.id.tvtimeitemnote);
            tvNameFolderNote = itemView.findViewById(R.id.tvnamefolderitemnote);
            imgLockNote = itemView.findViewById(R.id.imglockitemnote);
        }
    }
}
