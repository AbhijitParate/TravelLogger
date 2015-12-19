package abhijit.travellogger.NotesService;

import android.content.Intent;
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

import abhijit.travellogger.ApplicationUtility.Helper;
import abhijit.travellogger.ApplicationUtility.InitiateApplication;
import abhijit.travellogger.R;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TEMP_NOTE = "temp_note.txt" ;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_notes);

        editText = (EditText) findViewById(R.id.editText);
        editText.forceLayout();

        Button saveButton = (Button) findViewById(R.id.button_save_text);
        saveButton.setOnClickListener(this);

        Button clearButton = (Button) findViewById(R.id.button_clear_text);
        clearButton.setOnClickListener(this);

    }

    public void saveNote(){

        try {
            File f =  new File(InitiateApplication.getAppFolderTemp().toString()+"/" + TEMP_NOTE);
            if(!f.exists()){
                f.createNewFile();
            }
            String text = String.valueOf(editText.getText());

            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
            outputStreamWriter.write(text);
            outputStreamWriter.close();
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void finishWithResultOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_save_text) {
            saveNote();
            finishWithResultOk();
        } else if (v.getId() == R.id.button_clear_text){
            editText.setText("");
        }
    }
}
