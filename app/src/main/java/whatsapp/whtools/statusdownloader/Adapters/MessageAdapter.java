package whatsapp.whtools.statusdownloader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import whatsapp.whtools.statusdownloader.Message;
import whatsapp.whtools.statusdownloader.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myView> {
    private LayoutInflater inflater;
    private Context context;
    Message CallLogsModel;
    ArrayList<Message> list;
    private int lastPosition = -1;
    ArrayList<Integer> strings;

    public MessageAdapter(Context context, ArrayList<Message> arraylist) {
        inflater = LayoutInflater.from(context);
        this.list = arraylist;
        this.context = context;
    }

    @Override
    public myView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listviewdatalayout, parent, false);
        strings = new ArrayList<>();
        return new myView(view);
    }


    @Override
    public void onBindViewHolder(final myView holder, final int position) {
       /* Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);*/
        lastPosition = position;
//        holder.bind(position);
        CallLogsModel = list.get(position);

       /* if (CallLogsModel.getCallername() != null) {
            holder.callTitle.setText(CallLogsModel.getCallername());
        } else {
            holder.callTitle.setText("Unknown");
        }*/

//        String durationInSec = milliSecondsToTimer(CallLogsModel.getCallerdate());
        holder.textViewNAME.setText(CallLogsModel.getTitle());
        holder.textViewSMS.setText(CallLogsModel.getSms());
        holder.textViewDATE.setText(CallLogsModel.getDate());

//        if (CallLogsModel.getCallertype() == 1) {
//            holder.imageView.setImageResource(R.drawable.ic_call_missed);
//        } else if (CallLogsModel.getCallertype() == 2) {
//            holder.imageView.setImageResource(R.drawable.ic_call_missed_outgoing);
//        } else if (CallLogsModel.getCallertype() == 3) {
//            holder.imageView.setImageResource(R.drawable.ic_phone_missed);
//        }
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + CallLogsModel.getCallnumber()));
                if (ActivityCompat.checkSelfPermission(((Activity) context).getApplication(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(callIntent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();

    }

    public class myView extends RecyclerView.ViewHolder {
        public TextView textViewNAME, textViewSMS, textViewDATE;


        public myView(final View itemView) {
            super(itemView);

            textViewNAME = (TextView) itemView.findViewById(R.id.textViewNAME);
            textViewSMS = (TextView) itemView.findViewById(R.id.textViewSMS);
            textViewDATE = (TextView) itemView.findViewById(R.id.textViewDATE);

           /* new Font(context, textViewNAME);
            new Font(context, textViewSMS);
            new Font(context, textViewDATE);*/
        }

     /*   void bind(int listIndex) {
            callTitle.setText(String.valueOf(listIndex));
        }*/
    }


    public String milliSecondsToTimer(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        String timeString = String.format("%02d:%02d", minutes, seconds);
        return timeString;
    }
}

