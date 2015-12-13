package com.example.linquas.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Linquas on 2015/11/27.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    String[] mDataset;
    private int[] mImageID;

    private static ItemClickListener mOnItemClickListener = null;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageButton imageButton;
        public ViewHolder(View  v) {
            super(v);
//            mTextView = (TextView) v.findViewById(R.id.info_text);
            imageButton = (ImageButton) v.findViewById(R.id.info_image_btn);
            imageButton.setOnClickListener(this);

        }
        @Override
        public void onClick(View v){
            mOnItemClickListener.onItemClick(v,this.getLayoutPosition());
        }
    }

    public void setmOnItemClickListener(ItemClickListener listener){
        mOnItemClickListener = listener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, int[] imgID) {
        mDataset = myDataset;
        mImageID = imgID;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public  interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);
        holder.imageButton.setImageResource(mImageID[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void swapData(String[] myDataset, int[] imgID){
        mDataset = myDataset;
        mImageID = imgID;
    }
}
