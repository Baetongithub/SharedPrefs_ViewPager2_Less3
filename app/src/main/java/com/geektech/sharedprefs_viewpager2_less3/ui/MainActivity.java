package com.geektech.sharedprefs_viewpager2_less3.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.geektech.sharedprefs_viewpager2_less3.R;
import com.geektech.sharedprefs_viewpager2_less3.adapter.PostAdapter;
import com.geektech.sharedprefs_viewpager2_less3.databinding.ActivityMainBinding;
import com.geektech.sharedprefs_viewpager2_less3.databinding.LayoutBottomSheetDialogBinding;
import com.geektech.sharedprefs_viewpager2_less3.model.Post;
import com.geektech.sharedprefs_viewpager2_less3.network.RetrofitBuilder;
import com.geektech.sharedprefs_viewpager2_less3.utils.Constants;
import com.geektech.sharedprefs_viewpager2_less3.utils.OnItemClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding ui;
    private PostAdapter adapter;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());
        posts = new ArrayList<>();
        recyclerOnScrollListener();

        getPosts();

        ui.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts();
            }
        });
    }

    private void getPosts() {
        ui.swipeRefresh.setRefreshing(true);
        RetrofitBuilder.getInstance().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                if (response.body() != null) {
                    ui.swipeRefresh.setRefreshing(false);
                    posts = response.body();
                    adapter = new PostAdapter(posts);
                    ui.recyclerViewMain.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            Post post = adapter.getItem(position);
                            openDetailPosts(post);
                        }

                        @Override
                        public void onLongClick(int position) {
                            showDialogg(position);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, R.string.something_went_wrong + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addPostButton(View view) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
        LayoutBottomSheetDialogBinding dialogBinding = LayoutBottomSheetDialogBinding.inflate(getLayoutInflater());

        dialogBinding.buttonShare.setOnClickListener(v -> {

            String title = dialogBinding.editTextTitle.getText().toString();
            String content = dialogBinding.editTextContent.getText().toString();
            Integer group = Integer.valueOf(dialogBinding.editTextGroup.getText().toString());
            Integer user = Integer.valueOf(dialogBinding.editTextUser.getText().toString());

            Post post = new Post(title, content, group, user);
            RetrofitBuilder.getInstance().createPost(post).enqueue(new Callback<Post>() {
                @Override
                public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                    Toast.makeText(MainActivity.this, R.string.post_added, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, getString(R.string.post_not_added) + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(dialogBinding.getRoot());
        bottomSheetDialog.show();
    }

    private void openDetailPosts(Post post) {
        Intent intent = new Intent(MainActivity.this, DetailedPostActivity.class);
        intent.putExtra(Constants.ID, post.getId());
        intent.putExtra(Constants.TITLE, post.getTitle());
        intent.putExtra(Constants.CONTENT, post.getContent());
        intent.putExtra(Constants.GROUP, String.valueOf(post.getGroup()));
        intent.putExtra(Constants.USER, String.valueOf(post.getUser()));
        startActivity(intent);
    }


    private void showDialogg(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.delete);
        alertDialog.setMessage(R.string.remove_this_item);
        alertDialog.setPositiveButton(R.string.cancel, (dialog, which) -> dialog.cancel());

        alertDialog.setNegativeButton(R.string.yes, (dialog, which) -> {
            RetrofitBuilder.getInstance().deletePost(posts.get(position).getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Toast.makeText(MainActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.not_deleted + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            adapter.remove(position);
            adapter.notifyDataSetChanged();
        });
        AlertDialog dialog = alertDialog.create();
        dialog.setIcon(R.drawable.ic_delete_forever_24);
        dialog.show();
    }


    private void recyclerOnScrollListener() {
        ui.recyclerViewMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if (ui.fab.isShown()) {
                        ui.fab.hide();
                    }
                } else if (dy < 0) {
                    if (!ui.fab.isShown()) {
                        ui.fab.show();
                    }
                }
            }
        });
    }
}