package com.geektech.sharedprefs_viewpager2_less3.remote;

import com.geektech.sharedprefs_viewpager2_less3.model.Post;
import com.geektech.sharedprefs_viewpager2_less3.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API {

    @GET(Constants.POSTS)
    Call<List<Post>> getPosts();

    @GET(Constants.POST_BY_ID)
    Call<Post> getPost(
            @Path(Constants.POST_ID)
                    String id
    );

    @POST(Constants.POSTS)
    Call<Post> createPost(
            @Body Post post
    );

    @PUT(Constants.POST_BY_ID)
    Call<Post> editPost(
            @Path(Constants.POST_ID) String id,
            @Body Post post
    );

    @DELETE(Constants.POST_BY_ID)
    Call<Void> deletePost(
            @Path(Constants.POST_ID)
                    String id
    );
}
