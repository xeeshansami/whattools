package download.whatstatus.savestatus.Model;

import androidx.annotation.Keep;

@Keep
public class VideoStatusModel {

    int VideoStatusPath;

    public VideoStatusModel(int videoStatusPath) {
        VideoStatusPath = videoStatusPath;
    }

    public VideoStatusModel() {
    }

    public int getVideoStatusPath() {
        return VideoStatusPath;
    }

    public void setVideoStatusPath(int videoStatusPath) {
        VideoStatusPath = videoStatusPath;
    }
}
