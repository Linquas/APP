package com.example.linquas.myapplication;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Linquas on 2015/12/11.
 */
public class MyAdaptor  extends RecyclerView.Adapter<MyAdaptor.ViewHolder> {

    private List<BluetoothDevice> sDeviceList;

    protected static OnRecyclerViewItemClickListener mOnItemClickListener = null;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView, name;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.device_address);
            name = (TextView) v.findViewById(R.id.device_name);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            mOnItemClickListener.onItemClick(v,this.getLayoutPosition());
        }
    }


    public void swapData(List<BluetoothDevice> myDataset){
        sDeviceList = myDataset;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdaptor(List<BluetoothDevice> myDataset) {
        sDeviceList = myDataset;
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener){
        MyAdaptor.mOnItemClickListener = listener;
    }

    public  interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, int position);
    }

    public void clearData(){
        int size = this.sDeviceList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.sDeviceList.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType)  {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_device, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);
//        holder.imageButton.setImageResource(mImageID[position]);

        BluetoothDevice device = sDeviceList.get(position);
        if(device == null){
            holder.name.setText("DEMO");
        }else if (device.getName() != null && device.getName().length() > 0){
            holder.name.setText(sDeviceList.get(position).getName());
            holder.mTextView.setText(sDeviceList.get(position).getAddress());
        }
        else {
            holder.name.setText(R.string.unknown_device);
            holder.mTextView.setText(sDeviceList.get(position).getAddress());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sDeviceList.size();
    }


}
