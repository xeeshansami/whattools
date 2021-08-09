package download.whatstatus.savestatus;


public class FolderEntity {

    private static FolderEntity instance=null;
    String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    String path;

    public static synchronized FolderEntity getInstance() {
        if(instance==null)
        {
            instance=new FolderEntity();
        }
        return instance;
    }


}
