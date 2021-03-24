package com.geektech.sharedprefs_viewpager2_less3.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geektech.sharedprefs_viewpager2_less3.R;
import com.geektech.sharedprefs_viewpager2_less3.databinding.ItemPostBinding;
import com.geektech.sharedprefs_viewpager2_less3.model.Post;
import com.geektech.sharedprefs_viewpager2_less3.utils.Constants;
import com.geektech.sharedprefs_viewpager2_less3.utils.OnItemClickListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostVH> {

    private final List<Post> posts;
    private OnItemClickListener onItemClickListener;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostVH holder, int position) {
        holder.onBind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void remove(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
    }

    public Post getItem(int position) {
        return posts.get(position);
    }

    public class PostVH extends RecyclerView.ViewHolder {

        private final ItemPostBinding itemPostBinding;

        public PostVH(@NonNull View itemView) {
            super(itemView);
            itemPostBinding = ItemPostBinding.bind(itemView);
            itemView.setOnLongClickListener(v -> {
                onItemClickListener.onLongClick(getAdapterPosition());
                return true;
            });
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }

        @SuppressLint("SetTextI18n")
        private void onBind(Post post) {
            itemPostBinding.textViewContent.setText(Constants.CONTENT + post.getContent());
            itemPostBinding.textViewId.setText(Constants.ID_FOR_TEXT_VIEW + post.getId());
            itemPostBinding.textViewTitle.setText(Constants.TITLE + post.getTitle());
            itemPostBinding.textViewGroup.setText(Constants.GROUP + post.getGroup());
            itemPostBinding.textViewUser.setText(Constants.USER + post.getUser());
        }
    }
}
