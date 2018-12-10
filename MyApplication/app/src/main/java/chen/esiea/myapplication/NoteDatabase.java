package chen.esiea.myapplication;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Note.class},version = 2)

public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();
    //synchronized for not letting another service acces this. For just getting the instance of the database if this exist
    //If not will create a new one
    public static  synchronized NoteDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }
    //Those are use to add to populate our database on the creation
    private static RoomDatabase.Callback roomCallback= new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    //This will create with the data we put inside sending to roolcallback.this later with then send it in the inscance for build
    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private NoteDao noteDao;
        private  PopulateDbAsyncTask(NoteDatabase db){
            noteDao=db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Swipe Right or Left","To delete this note",1));
            noteDao.insert(new Note("The Rose-tree","There was once upon a time a good man who had two children: a girl by a first wife, and a boy by the second. The girl was as white as milk, and her lips were like cherries. Her hair was like golden silk, and it hung to the ground. Her brother loved her dearly, but her wicked stepmother hated her. \"Child,\" said the stepmother one day, \"go to the grocer's shop and buy me a pound of candles.\" She gave her the money; and the little girl went, bought the candles, and started on her return. There was a stile to cross. She put down the candles whilst she got over the stile. Up came a dog and ran off with the candles.\n" +
                    "\n" +
                    "She went back to the grocer's, and she got a second bunch. She came to the stile, set down the candles, and proceeded to climb over. Up came the dog and ran off with the candles.\n" +
                    "\n" +
                    "She went again to the grocer's, and she got a third bunch; and just the same happened. Then she came to her stepmother crying, for she had spent all the money and had lost three bunches of candles.\n" +
                    "\n" +
                    "The stepmother was angry, but she pretended not to mind the loss. She said to the child: \"Come, lay your head on my lap that I may comb your hair.\" So the little one laid her head in the woman's lap, who proceeded to comb the yellow silken hair. And when she combed the hair fell over her knees, and rolled right down to the ground.\n" +
                    "\n" +
                    "Then the stepmother hated her more for the beauty of her hair; so she said to her, \"I cannot part your hair on my knee, fetch a billet of wood.\" So she fetched it. Then said the stepmother, \"I cannot part your hair with a comb, fetch me an axe.\" So she fetched it.",5));
            noteDao.insert(new Note("Click on me to edit me","i can edit myself and changemy priority",5));
            noteDao.insert(new Note("Click on the add below to add a note","",10));

            return null;
        }
    }
}
