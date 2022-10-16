package com.program.newspaper.model;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.program.newspaper.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


// Адаптер для recyclerview, также обработчик нажатия на элемент
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private final List<Item> items;
    private ClickListener listener;

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    // Подкласс олицетворяющий элемент списка
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, dot, timestamp;
        Item item;

        public ItemHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            dot = itemView.findViewById(R.id.dot);
            timestamp = itemView.findViewById(R.id.timestamp);
            itemView.setOnClickListener(this);

            itemView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.deleteClick(getBindingAdapterPosition(), v);
                }
            });
        }

        @Override
        public void onClick(View view) {
            listener.itemClick(getBindingAdapterPosition(), view);
        }
    }

    // Генерация списка
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ItemHolder(item);
    }

    // Генерация элементов списка
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.dot.setText(Html.fromHtml("&#8226;"));
        holder.timestamp.setText(formatDate(Calendar.getInstance().getTime()));
        holder.item = items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Вспомогательный метод для установления формата даты
    private String formatDate(Date date){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
        return fmtOut.format(date);
    }

    public interface ClickListener{
        void itemClick(int position, View v);
        void deleteClick(int position, View v);
    }
}