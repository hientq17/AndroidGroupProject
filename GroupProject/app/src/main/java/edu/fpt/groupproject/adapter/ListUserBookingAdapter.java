package edu.fpt.groupproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.fpt.groupproject.R;
import edu.fpt.groupproject.model.room.Room;
import edu.fpt.groupproject.model.user.User;
import edu.fpt.groupproject.model.user.UserBooking;

public class ListUserBookingAdapter extends RecyclerView.Adapter<ListUserBookingAdapter.MyViewHolder> {
    private List<UserBooking> list;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtPhoneNumber;
        private TextView txtTime;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtPhoneNumber = (TextView) view.findViewById(R.id.txtPhoneNumber);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
        }
    }

    public ListUserBookingAdapter(List<UserBooking> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_booking_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

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

        holder.txtName.setText("Tên: " + list.get(position).getName());
        holder.txtPhoneNumber.setText("SĐT " +list.get(position).getPhone());
        holder.txtTime.setText("Ngày đặt: " + dateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener{
        void onItemClick(User room);
    }
}
