package edu.fpt.groupproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import edu.fpt.groupproject.R;
import edu.fpt.groupproject.model.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {
    private List<Room> list;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtPrice, txtTime, txtAddress, txtStatus;
        public ImageView imgRoom;
        public Button btnTitle;

        public MyViewHolder(View view) {
            super(view);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            btnTitle = (Button) view.findViewById(R.id.btnTitle);
            imgRoom = (ImageView) view.findViewById(R.id.imgRoom);
        }
    }


    public RoomAdapter(List<Room> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //convert sql date to display
        String sqlFormat = "yyyy-MM-dd'T'HH:mm";
        String displayFormat = "dd/MM/yyyy hh:mm";
        String strTime = list.get(position).getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(sqlFormat);
        Date date = null;
        try {
            date = dateFormat.parse(strTime);
            dateFormat = new SimpleDateFormat(displayFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //show short money
        double price = list.get(position).getPrice();
        String strPrice = (Double)price/1000000 + "M";
        //show image
        String[] listUrl = list.get(position).getImage().split(";");
        Picasso.with(holder.itemView.getContext()).load(listUrl[0]).into(holder.imgRoom);
        holder.txtPrice.setText(strPrice);
        holder.txtTime.setText(dateFormat.format(date));
        holder.txtAddress.setText(String.valueOf(list.get(position).getAddress()));
        holder.btnTitle.setText(list.get(position).getTitle());
        holder.btnTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Room room);
    }
}
