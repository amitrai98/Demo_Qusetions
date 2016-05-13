package android.com.demo_qusetions.adapter;

import android.com.demo_qusetions.R;
import android.com.demo_qusetions.bin.UserBin;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amitrai on 13/5/16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewClass>{
private List<UserBin> listUser = new ArrayList<>();

    public MyAdapter(List<UserBin> listUser){
        this.listUser = listUser;
    }

    @Override
    public myViewClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_cell,parent, false);
        return new myViewClass(view);
    }

    @Override
    public void onBindViewHolder(myViewClass holder, int position) {
        UserBin user = listUser.get(position);
        holder.txtName.setText(user.getUserName());
        holder.txtAddress.setText(user.getUserAddress());
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class myViewClass extends RecyclerView.ViewHolder{
        public TextView txtName, txtAddress;
        public myViewClass(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
        }
    }
}
