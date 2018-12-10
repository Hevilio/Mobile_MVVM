package chen.esiea.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE =
            "chen.esiea.myapplication.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "chen.esiea.myapplication.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "chen.esiea.myapplication.EXTRA_PRIORITY";
    public static final String EXTRA_ID=
            "chen.esiea.myapplication.EXTRA_IF";

    private EditText Title;
    private EditText Description;
    private NumberPicker Priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Title = findViewById(R.id.title_edit);
        Description = findViewById(R.id.text_description_edit);
        //for the priority number picker
        Priority = findViewById(R.id.number_priority_edit);
        Priority.setMinValue(1);
        Priority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        Intent intent=getIntent();//have all the data
        if(intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            Title.setText(intent.getStringExtra(EXTRA_TITLE));
            Description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            Priority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));

        }
        else {
            setTitle("Add Note");
        }
    }

    private void saveNote() {
        String new_title = Title.getText().toString();
        String new_description = Description.getText().toString();
        int new_priority = Priority.getValue();

        if (new_title.trim().isEmpty() || new_description.trim().isEmpty()) {
            Toast.makeText(this, "Please complete all", Toast.LENGTH_SHORT);
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, new_title);
        data.putExtra(EXTRA_DESCRIPTION, new_description);
        data.putExtra(EXTRA_PRIORITY, new_priority);

        int id=getIntent().getIntExtra(EXTRA_ID,-1);
        if(id!=-1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
