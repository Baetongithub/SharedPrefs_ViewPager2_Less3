package com.geektech.sharedprefs_viewpager2_less3.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.geektech.sharedprefs_viewpager2_less3.R;
import com.geektech.sharedprefs_viewpager2_less3.databinding.ActivityDetailedPostBinding;
import com.geektech.sharedprefs_viewpager2_less3.databinding.LayoutBottomSheetEditBinding;
import com.geektech.sharedprefs_viewpager2_less3.model.Post;
import com.geektech.sharedprefs_viewpager2_less3.remote.RetrofitBuilder;
import com.geektech.sharedprefs_viewpager2_less3.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedPostActivity extends AppCompatActivity {

    private ActivityDetailedPostBinding addPostBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPostBinding = ActivityDetailedPostBinding.inflate(getLayoutInflater());
        setContentView(addPostBinding.getRoot());

        String id = getIntent().getStringExtra(Constants.ID);

        addPostBinding.editPost.setOnClickListener(v -> editPost(id));

        RetrofitBuilder.getInstance().getPost(id).enqueue(new Callback<Post>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.body() != null) {
                    addPostBinding.textViewId.setText(Constants.ID_FOR_TEXT_VIEW + response.body().getId());
                    addPostBinding.textViewTitle.setText(Constants.TITLE + response.body().getTitle());
                    addPostBinding.textViewContent.setText(Constants.CONTENT + response.body().getContent());
                    addPostBinding.textViewGroup.setText(Constants.GROUP + response.body().getGroup());
                    addPostBinding.textViewUser.setText(Constants.USER + response.body().getUser());
                } else {
                    Toast.makeText(DetailedPostActivity.this, R.string.something_went_wrong + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                Toast.makeText(DetailedPostActivity.this, getString(R.string.something_went_wrong)
                        + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPost(String id) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                DetailedPostActivity.this, R.style.BottomSheetDialogTheme);

        LayoutBottomSheetEditBinding dialogBinding = LayoutBottomSheetEditBinding.inflate(getLayoutInflater());
        String title = getIntent().getStringExtra(Constants.TITLE);
        String content = getIntent().getStringExtra(Constants.CONTENT);
        String group = getIntent().getStringExtra(Constants.GROUP);
        String user = getIntent().getStringExtra(Constants.USER);

        dialogBinding.editTextTitle.setText(title);
        dialogBinding.editTextContent.setText(content);
        dialogBinding.editTextGroup.setText(group);
        dialogBinding.editTextUser.setText(user);

        dialogBinding.buttonShare.setOnClickListener(v1 -> {

            String title1 = dialogBinding.editTextTitle.getText().toString();
            String content1 = dialogBinding.editTextContent.getText().toString();
            Integer group1 = Integer.valueOf(dialogBinding.editTextGroup.getText().toString());
            Integer user1 = Integer.valueOf(dialogBinding.editTextUser.getText().toString());

            Post post = new Post(title1, content1, group1, user1);

            RetrofitBuilder.getInstance().editPost(id, post).enqueue(new Callback<Post>() {
                @Override
                public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                    Toast.makeText(DetailedPostActivity.this, R.string.post_edited, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                    Toast.makeText(DetailedPostActivity.this, getString(R.string.something_went_wrong) + t.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(dialogBinding.getRoot());
        bottomSheetDialog.show();
    }
}