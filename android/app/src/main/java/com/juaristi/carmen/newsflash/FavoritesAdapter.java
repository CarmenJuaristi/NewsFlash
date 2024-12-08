package com.juaristi.carmen.newsflash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private final List<Article> favoriteArticleList;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public FavoritesAdapter(List<Article> favoriteArticleList, OnItemClickListener onItemClickListener) {
        this.favoriteArticleList = favoriteArticleList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_news, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Article article = favoriteArticleList.get(position);
        holder.bind(article, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return favoriteArticleList.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.favorite_news_title);
        }

        public void bind(Article article, OnItemClickListener listener) {
            titleTextView.setText(article.getTitle());
            itemView.setOnClickListener(v -> listener.onItemClick(article));
        }
    }
}

