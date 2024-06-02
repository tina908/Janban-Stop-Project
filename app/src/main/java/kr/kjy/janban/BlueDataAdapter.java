package kr.kjy.janban;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class BlueDataAdapter extends RecyclerView.Adapter<BlueDataAdapter.BlueDataViewHolder> {

    private ArrayList<String> blueDataList;

    public BlueDataAdapter(ArrayList<String> blueDataList) {
        this.blueDataList = blueDataList;
    }

    @NonNull
    @Override
    public BlueDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new BlueDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlueDataViewHolder holder, int position) {
        String blueData = blueDataList.get(position);
        holder.textBlueData.setText("BlueData: " + blueData);
    }

    @Override
    public int getItemCount() {
        return blueDataList.size();
    }

    static class BlueDataViewHolder extends RecyclerView.ViewHolder {
        TextView textBlueData;

        BlueDataViewHolder(View itemView) {
            super(itemView);
            textBlueData = itemView.findViewById(R.id.textBlueData);
        }
    }
}

