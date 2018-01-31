package sku.jvj.seatcock.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import sku.jvj.seatcock.Model.StoreMenu;
import sku.jvj.seatcock.R;

/**
 * Created by Android Hong on 2016-08-07.
 */
public class StoreMenuAdapter extends RecyclerView.Adapter<StoreMenuAdapter.ViewHolder>{

    private ArrayList<StoreMenu> storeMenuArrayList;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public TextView menuTitleTextView;
        public TextView menuPriceTextVIew;
        public ImageView menuImageView;

        public ViewHolder(View v) {
            super(v);
            menuTitleTextView = (TextView) v.findViewById(R.id.menuTitleTextView);
            menuPriceTextVIew = (TextView)v.findViewById(R.id.menuPriceTextVIew);
            menuImageView = (ImageView) v.findViewById(R.id.menuImageView);
        }
    }

    public StoreMenuAdapter() {
        storeMenuArrayList = new ArrayList<>();
    }

    @Override
    public StoreMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_storemenucarditems, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(StoreMenuAdapter.ViewHolder holder, int position) {

        holder.menuTitleTextView.setText(storeMenuArrayList.get(position).getMenuName());
        holder.menuPriceTextVIew.setText(storeMenuArrayList.get(position).getMenuPrice());
        Glide.with(holder.menuImageView.getContext())
                .load(storeMenuArrayList.get(position).getImageResourceId())
                .into(holder.menuImageView);
    }

    @Override
    public int getItemCount() {
        return storeMenuArrayList.size();
    }

    public void addData(ArrayList<StoreMenu> listitems){
        this.storeMenuArrayList = listitems;
    }
}