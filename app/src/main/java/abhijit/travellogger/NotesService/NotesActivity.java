package abhijit.travellogger.NotesService;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;

import abhijit.travellogger.ApplicationUtility.InitiateApplication;
import abhijit.travellogger.R;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        editText = (EditText) findViewById(R.id.editText);
        saveButton = (Button) findViewById(R.id.saveNote);

        saveButton.setOnClickListener(this);
    }

    public void saveNote(){

        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String noteName = "NOTE_" + timeStamp;

        try {
//            File f = File.createTempFile(Environment.getExternalStorageDirectory().toString() + "/test", ".txt");
            File f =  new File(InitiateApplication.getAppFolderNotes().toString()+"/" +noteName + ".txt");
//            File f = File.createTempFile(noteName, ".txt", MainActivity.getAppFolderNotes());
            if(!f.exists()){
                f.createNewFile();
            }
            String text = String.valueOf(editText.getText());

            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.write(text);
            outputStreamWriter.close();
            fOut.close();
            editText.setText("");
            Toast.makeText(getApplicationContext(), "Note saved successfully.\nLocation: " + f.getAbsolutePath() , Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
        saveNote();
    }
}
