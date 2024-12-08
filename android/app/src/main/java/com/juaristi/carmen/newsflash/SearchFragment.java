package com.juaristi.carmen.newsflash;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private NewsAdapter newsAdapter;
    private Button retryButton;
    private TextView errorText;
    private CardView itemSearchError;
    private FragmentSearchBinding binding;

    private boolean isError = false;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isScrolling = false;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSearchBinding.bind(view);

        itemSearchError = view.findViewById(R.id.itemSearchError);
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View errorView = inflater.inflate(R.layout.item_error, null);

        retryButton = errorView.findViewById(R.id.retryButton);
        errorText = errorView.findViewById(R.id.errorText);

        newsViewModel = ((NewsActivity) requireActivity()).getNewsViewModel();
        setupSearchRecycler();

        newsAdapter.setOnItemClickListener(article -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("article", article);
            findNavController().navigate(R.id.action_searchFragment_to_articleFragment, bundle);
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        binding.searchEdit.addTextChangedListener(new TextWatcher() {
            private Runnable searchRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> {
                    if (s.length() > 0) {
                        newsViewModel.searchNews(s.toString());
                    }
                };
                handler.postDelayed(searchRunnable, Constants.SEARCH_NEWS_TIME_DELAY);
            }
        });

        newsViewModel.getSearch().observe(getViewLifecycleOwner(), response -> {
            if (response instanceof Resource.Success) {
                hideProgressBar();
                hideErrorMessage();
                List<Article> articles = ((Resource.Success<NewsResponse>) response).getData().getArticles();
                newsAdapter.getDiffer().submitList(new ArrayList<>(articles));

                int totalPages = ((Resource.Success<NewsResponse>) response).getData().getTotalResults() / Constants.QUERY_PAGE_SIZE + 2;
                isLastPage = newsViewModel.getSearchPage() == totalPages;
                if (isLastPage) {
                    binding.recyclerSearch.setPadding(0, 0, 0, 0);
                }
            } else if (response instanceof Resource.Error) {
                hideProgressBar();
                String message = ((Resource.Error<?>) response).getMessage();
                if (message != null) {
                    Toast.makeText(getActivity(), "Sorry, error: " + message, Toast.LENGTH_LONG).show();
                    showErrorMessage(message);
                }
            } else if (response instanceof Resource.Loading) {
                showProgressBar();
            }
        });

        retryButton.setOnClickListener(v -> {
            String query = binding.searchEdit.getText().toString();
            if (!query.isEmpty()) {
                newsViewModel.searchNews(query);
            } else {
                hideErrorMessage();
            }
        });
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
        itemSearchError.setVisibility(View.INVISIBLE);
        isError = false;
    }

    private void showErrorMessage(String message) {
        itemSearchError.setVisibility(View.VISIBLE);
        errorText.setText(message);
        isError = true;
    }

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager == null) return;

            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();



