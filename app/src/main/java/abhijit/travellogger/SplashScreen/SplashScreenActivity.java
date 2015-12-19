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

import java.util.HashMap;
import java.util.Map;

import abhijit.travellogger.ApplicationUtility.TravelLogger;
import abhijit.travellogger.R;
import abhijit.travellogger.TravelLoggerHomeActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;
    private final static Map<String, Integer> map = new HashMap<String, Integer>();
    static {
        map.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        map.put(Manifest.permission.ACCESS_COARSE_LOCATION,2);
        map.put(Manifest.permission.ACCESS_FINE_LOCATION,3);
    }

    private boolean write = false ;
    private boolean gps1 = false;
    private boolean gps2 = false;

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
        if(checkPermission())
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if( write && gps1) {
                        finish();
                        Intent homeIntent = new Intent(getBaseContext(), TravelLoggerHomeActivity.class);
                        startActivity(homeIntent);
                    }
                }
            }, 1000);

    }

    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(TravelLogger.getAppContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {
                write = true;
            }
            if (!(TravelLogger.getAppContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)) {
                gps1 = true;
            }
                requestPermissions(
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.ACCESS_FINE_LOCATION}
                        , REQUEST_CODE);
            }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE :
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED ) {

//                    Toast.makeText(this, "Permissions granted.", Toast.LENGTH_SHORT).show();
                    write = true;
                    finish();
                    Intent homeIntent = new Intent(getBaseContext(), TravelLoggerHomeActivity.class);
                    startActivity(homeIntent);

                } else {
                    Toast.makeText(this, "Permissions denied.", Toast.LENGTH_SHORT).show();
                    textView.setVisibility(View.VISIBLE);
                    write = false;
                }
        }

    }
}
