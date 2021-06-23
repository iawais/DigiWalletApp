package com.studios.digiwallet.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.studios.digiwallet.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends FirebaseRecyclerAdapter<Transaction, TransactionAdapter.TransactionViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TransactionAdapter(@NonNull FirebaseRecyclerOptions<Transaction> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TransactionViewHolder holder, int position, @NonNull Transaction model) {
        holder.tvTitle.setText(model.getTitle());
        holder.tvTimeStamp.setText(model.getTimestamp().toString());
        holder.tvAmount.setText((int) model.getAmount());
        holder.ivDetails.setTag(model);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item_rv, parent, false);

        return new TransactionViewHolder(view);
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTimeStamp, tvAmount;
        ImageView ivDetails;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle_transactionItem);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp_transactionItem);
            tvAmount = itemView.findViewById(R.id.tvAmount_transactionItem);
            ivDetails = itemView.findViewById(R.id.imvDetails_transactionItem);
        }
    }
}
