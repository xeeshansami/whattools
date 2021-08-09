package download.whatstatus.savestatus.Model;

import androidx.annotation.Keep;

@Keep
public class StatusModel {

    int statusPath;

    public StatusModel(int statusPath) {
        this.statusPath = statusPath;
    }

    public StatusModel() { }

    public int getStatusPath() {
        return statusPath;
    }

    public void setStatusPath(int statusPath) {
        this.statusPath = statusPath;
    }
}
