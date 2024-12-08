package com.juaristi.carmen.newsflash;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Nullable;

public class HeadlinesFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private NewsAdapter newsAdapter;
    private Button retryButton;
    private TextView errorText;
    private CardView itemHeadlinesError;
    private FragmentHeadlinesBinding binding;

    private boolean isError = false;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isScrolling = false;

    public HeadlinesFragment() {
        super(R.layout.fragment_headlines);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentHeadlinesBinding.bind(view);

        itemHeadlinesError = view.findViewById(R.id.itemHeadlinesError);
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View errorView = inflater.inflate(R.layout.item_error, null);

        retryButton = errorView.findViewById(R.id.retryButton);
        errorText = errorView.findViewById(R.id.errorText);

        newsViewModel = ((NewsActivity) requireActivity()).getNewsViewModel();
        setupHeadlinesRecycler();

        newsAdapter.setOnItemClickListener(article -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("article", article);
            findNavController().navigate(R.id.action_headlinesFragment_to_articleFragment, bundle);
        });

        newsViewModel.getHeadlines().observe(getViewLifecycleOwner(), response -> {
            if (response instanceof Resource.Success) {
                hideProgressBar();
                hideErrorMessage();
                NewsResponse newsResponse = (NewsResponse) ((Resource.Success<?>) response).getData();
                if (newsResponse != null) {
                    newsAdapter.getDiffer().submitList(newsResponse.getArticles());
                    int totalPages = newsResponse.getTotalResults() / Constants.QUERY_PAGE_SIZE + 2;
                    isLastPage = newsViewModel.getHeadlinesPage() == totalPages;
                    if (isLastPage) {
                        binding.recyclerHeadlines.setPadding(0, 0, 0, 0);
                    }
                }
            } else if (response instanceof Resource.Error) {
                hideProgressBar();
                String message = ((Resource.Error<?>) response).getMessage();
                if (message != null) {
                    Toast.makeText(getActivity(), "Sorry error: " + message, Toast.LENGTH_LONG).show();
                    showErrorMessage(message);
                }
            } else if (response instanceof Resource.Loading) {
                showProgressBar();
            }
        });

        retryButton.setOnClickListener(v -> newsViewModel.getHeadlines("es"));
    }

    private void hideProgressBar() {
        binding.paginationProgressBar.setVisibility(View.INVISIBLE);
        isLoading = false;
    }

    private void showProgressBar() {
        binding.paginationProgressBar.setVisibility(View.VISIBLE);
        isLoading = true;
    }

    private void hideErrorMessage() {
        itemHeadlinesError.setVisibility(View.INVISIBLE);
        isError = false;
    }

    private void showErrorMessage(String message) {
        itemHeadlinesError.setVisibility(View.VISIBLE);
        errorText.setText(message);
        isError = true;
    }

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null) {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                boolean isNoErrors = !isError;
                boolean isNotLoadingAndNotLastPage = !isLoading && !isLastPage;
                boolean isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount;
                boolean isNotAtBeginning = firstVisibleItemPosition >= 0;
                boolean isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE;
                boolean shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling;

                if (shouldPaginate) {
                    newsViewModel.getHeadlines("es");
                    isScrolling = false;
                }
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }
    };

    private void setupHeadlinesRecycler() {
        newsAdapter = new NewsAdapter();
        binding.recyclerHeadlines.setAdapter(newsAdapter);
        binding.recyclerHeadlines.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerHeadlines.addOnScrollListener(scrollListener);
    }
}

