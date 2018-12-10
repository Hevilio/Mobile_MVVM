package chen.esiea.myapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import chen.esiea.myapplication.databinding.ActivityMainBinding;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST=1;
    public static final int EDIT_NOTE_REQUEST=2;
    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        recyclerView=binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        FloatingActionButton buttonAdd=findViewById(R.id.button_add_note);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddEditNoteActivity.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);
            }
        });
        final NoteAdapter adapter=new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            //Work on foreground
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //Update Recycler
                adapter.setNotes(notes);
            }
        });
        //For the deleting part with swapping right or left
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            //Created by default don't need to be implemented but can be modified
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this,"Note deleted",Toast.LENGTH_SHORT);
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent= new Intent(MainActivity.this,AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
                startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ADD_NOTE_REQUEST&&resultCode==RESULT_OK){
            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,5);

            Note note=new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(MainActivity.this,"Note saved",Toast.LENGTH_SHORT);

        }
        else if(requestCode==EDIT_NOTE_REQUEST&&resultCode==RESULT_OK){
            int id=data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if (id==-1){
                Toast.makeText(this,"Note not updated",Toast.LENGTH_LONG);
                return;
            }

            String title=data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,5);

            Note note=new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this,"Note updated",Toast.LENGTH_LONG);

        }
        else {
            Toast.makeText(MainActivity.this,"Note not saved",Toast.LENGTH_SHORT);
        }
    }
}
