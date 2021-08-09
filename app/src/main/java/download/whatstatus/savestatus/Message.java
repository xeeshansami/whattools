package download.whatstatus.savestatus;

public class Message {

    String title,date,sms;

    public Message(String title, String date, String sms) {
        this.title = title;
        this.date = date;
        this.sms = sms;
    }

    public Message() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}
