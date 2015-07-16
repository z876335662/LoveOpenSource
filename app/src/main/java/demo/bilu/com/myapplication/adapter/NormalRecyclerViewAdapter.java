package demo.bilu.com.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import demo.bilu.com.myapplication.R;
import demo.bilu.com.myapplication.model.Code;
import demo.bilu.com.myapplication.net.Api;

/**
 * Created by yucong on 15/7/14.
 */
public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<Code> codes;

    public NormalRecyclerViewAdapter(Context context, ArrayList<Code> codes) {
        this.codes = codes;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.main_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.mTitle.setText(codes.get(position).name);
        holder.mContent.setText(codes.get(position).info);
        ImageLoader.getInstance().displayImage(Api.SENERADDRESS + codes.get(position).imageUrl, holder.imageView);
        // holder.imageView.startAnimation();
    }

    @Override
    public int getItemCount() {
        return codes == null ? 0 : codes.size();
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle, mContent;
        ImageView imageView;

        NormalTextViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.main_list_item_title);
            mContent = (TextView) view.findViewById(R.id.main_list_item_content);
            imageView = (ImageView) view.findViewById(R.id.main_list_item_gifimg);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }
}