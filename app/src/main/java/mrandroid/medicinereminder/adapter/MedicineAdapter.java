package mrandroid.medicinereminder.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mrandroid.medicinereminder.R;
import mrandroid.medicinereminder.model.MedicineModel;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private List<MedicineModel> list = new ArrayList<>();
    private OnItemClickListener listener;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MedicineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        MedicineModel item = list.get(holder.getAdapterPosition());

        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());

        holder.ivDelete.setOnClickListener(view -> {
            listener.onItemDelete(item);
        });

        holder.ivCall.setOnClickListener(view -> {
            listener.onItemCall(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<MedicineModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void deleteItem(MedicineModel medicineModel) {
        int position = this.list.indexOf(medicineModel);
        this.list.remove(medicineModel);
        this.notifyItemRemoved(position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvDescription;
        private final ImageView ivDelete;
        private final ImageView ivCall;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivCall = itemView.findViewById(R.id.ivCall);

        }
    }

    public interface OnItemClickListener {
        void onItemDelete(MedicineModel model);

        void onItemCall(MedicineModel model);
    }
}