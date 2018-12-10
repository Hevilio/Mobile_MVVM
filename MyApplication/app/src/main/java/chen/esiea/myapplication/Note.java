package chen.esiea.myapplication;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


//this our basic variable that we will use during the project
@Entity(tableName="note_table")
public class Note {
    @PrimaryKey(autoGenerate = true)

    private int id;

    private  String title;

    private  String description;
    //@ColumnInfo(name = "column_priority")
    private  int priority;

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
