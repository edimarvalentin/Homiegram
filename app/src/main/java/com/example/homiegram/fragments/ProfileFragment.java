package com.example.homiegram.fragments;

import android.util.Log;

import com.example.homiegram.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostFragment {
    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> retrievedPosts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                }
                for(Post post: posts){
                    Log.i(TAG, "Post user: " + post.getUser().getUsername());
                }
                posts.addAll(retrievedPosts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
