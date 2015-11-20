package abhijit.travellogger;

import android.view.View;
import android.widget.Toast;

/**
 * Created by abhijit on 11/19/15.
 */
public class CustomClickHandler implements View.OnClickListener, View.OnLongClickListener {

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Button clicked.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(v.getContext(), "Button long pressed.", Toast.LENGTH_SHORT).show();
        return false;
    }
}
