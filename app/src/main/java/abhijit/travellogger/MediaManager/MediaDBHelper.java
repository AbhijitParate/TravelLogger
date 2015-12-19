package abhijit.travellogger.MediaManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Created by abhijit on 12/14/15.
 */
public class MediaDBHelper extends SQLiteOpenHelper {

    //DB Constants
    public static final String DATABASE_NAME = "media.db";
    public static final int VERSION = 2;

    //Table Name
    public static final String TABLE_NAME = "MEDIA";

    //Column Names
    public static final String COL_ID = "MEDIA_ID";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_LATITUDE = "LATITUDE";
    public static final String COL_LONGITUDE = "LONGITUDE";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_FILENAME = "FILENAME";
    public static final String COL_TRIPNAME = "TRIPNAME";
    public static final String COL_CREATE_DATE = "CREATE_TIME";
    public static final String COL_UPDATE_DATE = "UPDATE_TIME";

    //All Columns
    public static final String[] ALL_COLUMNS
            = { COL_ID,
                COL_TITLE,
                COL_LATITUDE,
                COL_LONGITUDE,
                COL_LOCATION,
                COL_FILENAME,
                COL_TRIPNAME,
                COL_CREATE_DATE,
                COL_UPDATE_DATE };

    public MediaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE " + TABLE_NAME + " (");
        stringBuilder.append(COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuilder.append(COL_TITLE + " TEXT, ");
        stringBuilder.append(COL_LATITUDE + " TEXT, ");
        stringBuilder.append(COL_LONGITUDE + " TEXT, ");
        stringBuilder.append(COL_LOCATION + " TEXT, ");
        stringBuilder.append(COL_FILENAME + " TEXT, ");
        stringBuilder.append(COL_TRIPNAME + " TEXT, ");
        stringBuilder.append(COL_CREATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, ");
        stringBuilder.append(COL_UPDATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP );");
        try {
            sqLiteDatabase.execSQL(stringBuilder.toString());
        } catch (android.database.SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }
}
