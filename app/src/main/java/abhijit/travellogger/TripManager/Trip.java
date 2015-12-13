package abhijit.travellogger.TripManager;

/*
 * Created by abhijit on 12/7/15.
 */
public class Trip {

    private int trip_id;
    private String title;
    private String createDate;
    private String updateDate;

    public void setTripId(int id) {
        this.trip_id = id;
    }

    public void setTitle(String title) {
        this.title = String.valueOf(title);
    }

    public void setCreateDate(String createDate) {
        this.createDate = String.valueOf(createDate);
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = String.valueOf(updateDate);
    }

    public int getTripId() {
        return trip_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }
}
