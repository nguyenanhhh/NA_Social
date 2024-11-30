package com.kt.na_social.comment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kt.na_social.adapters.CommentAdapter;
import com.kt.na_social.R;

import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Object> commentList;  // Danh sách chứa cả bình luận và reply
    private TextView noCommentsTextView; // TextView thông báo khi không có bình luận

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.item_comment);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = fetchCommentsFromSomewhere();

        if (commentList.isEmpty()) {
            noCommentsTextView.setVisibility(View.VISIBLE);
        } else {
            noCommentsTextView.setVisibility(View.GONE);
        }
        adapter = new CommentAdapter(this, commentList);
        recyclerView.setAdapter(adapter);
    }

    private List<Object> fetchCommentsFromSomewhere() {

        return null;
    }
}
