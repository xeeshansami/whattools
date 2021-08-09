package download.whatstatus.savestatus.Model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Mysingleton {
    private static Context context;
    private static Mysingleton instance;
    private RequestQueue requestQueue;

    public Mysingleton(Context context) {

        this.context=context;
        requestQueue=getRequestQueue();
    }

    public static synchronized Mysingleton getInstance(Context context) {
        
        if(instance==null)
        {
            instance=new Mysingleton(context);
        }
        return instance;
    }

    public static void setInstance(Mysingleton instance) {
        Mysingleton.instance = instance;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue==null)
        {

            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }



    public <T> void addtorequestqueue(Request<T> request)
    {

        getRequestQueue().add(request);
    }
}
