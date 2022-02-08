package com.yusufsamaila.github;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


/**
 * Created by SAMAILA Yusuf D on 3/8/17.
 */
public class DetailActivity extends AppCompatActivity {
    private ImageLoader mImageLoader;
    private TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        username = (TextView)findViewById(R.id.text1);

        String data = getIntent().getExtras().getString("login");
        username.setText(data);

        String html = getIntent().getExtras().getString("html_url");

        TextView textView = (TextView) findViewById(R.id.textlink);
        textView.setText(html);
        Linkify.addLinks(textView, Linkify.WEB_URLS);

        String avatar = getIntent().getExtras().getString("avatar_url");

        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.image1);
        mImageLoader = new ImageLoader(VolleyApplication.getInstance().getRequestQueue(), new BitmapLruCache());
        imageView.setImageUrl(avatar, mImageLoader);


    }

    public void createIntent(View v){
        String html = getIntent().getExtras().getString("html_url");
        String login = getIntent().getExtras().getString("login");
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer @"+login+", "+html);
        startActivity(Intent.createChooser(share, "Share using"));
    }





}
