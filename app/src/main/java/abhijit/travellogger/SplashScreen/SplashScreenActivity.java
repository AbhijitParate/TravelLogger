package abhijit.travellogger.SplashScreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import abhijit.travellogger.R;
import abhijit.travellogger.TravelLoggerHomeActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_splash_screen);
        ImageView imageView = (ImageView) findViewById(R.id.splash_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
        textView = (TextView) findViewById(R.id.splash_text);
        textView.setVisibility(View.GONE);
        if(checkPermission()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Intent homeIntent = new Intent(getBaseContext(), TravelLoggerHomeActivity.class);
                    startActivity(homeIntent);
                }
            }, 1000);
        }
    }

    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent homeIntent = new Intent(getBaseContext(), TravelLoggerHomeActivity.class);
                    startActivity(homeIntent);
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                    textView.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
