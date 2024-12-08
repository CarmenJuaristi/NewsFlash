package com.juaristi.carmen.newsflash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ArticleViewHolder> {

    private AsyncListDiffer<Article> differ;
    private OnItemClickListener onItemClickListener;

    public NewsAdapter() {
        DiffUtil.ItemCallback<Article> differCallback = new DiffUtil.ItemCallback<Article>() {
            @Override
            public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
                return oldItem.getUrl().equals(newItem.getUrl());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
                return oldItem.equals(newItem);  // Ahora funciona porque hemos implementado equals() en Article
            }
        };

        differ = new AsyncListDiffer<>(this, differCallback);
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = differ.getCurrentList().get(position);

        Glide.with(holder.itemView).load(article.getUrlToImage()).into(holder.articleImage);
        holder.articleSource.setText(article.getSource());  // Cambiado para que sea un String
        holder.articleTitle.setText(article.getTitle());
        holder.articleDescription.setText(article.getDescription());
        holder.articleDateTime.setText(article.getPublishedAt());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(article);
            }
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Interface para el listener de clics
    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    // ViewHolder para el artículo
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        public ImageView articleImage;
        public TextView articleSource;
        public TextView articleTitle;
        public TextView articleDescription;
        public TextView articleDateTime;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.articleImage);
            articleSource = itemView.findViewById(R.id.articleSource);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleDescription = itemView.findViewById(R.id.articleDescription);
            articleDateTime = itemView.findViewById(R.id.articleDateTime);
        }
    }
}
