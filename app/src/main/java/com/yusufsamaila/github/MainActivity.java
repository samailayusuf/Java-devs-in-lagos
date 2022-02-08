package com.yusufsamaila.github;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ImagesFragment())
                    .commit();
        }
    }


    public static class ImagesFragment extends Fragment {

        private UserRecordsAdapter mAdapter;

        public ImagesFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_users, container, false);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            mAdapter = new UserRecordsAdapter(getActivity());

            ListView listView = (ListView) getView().findViewById(R.id.list1);
            listView.setAdapter(mAdapter);

            fetch();
            passData();


        }


        private void fetch() {

            final ProgressDialog pDialog;
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading...");
            pDialog.show();



            JsonObjectRequest request = new JsonObjectRequest(

                    "https://api.github.com/search/users?q=language:java+location:lagos", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            pDialog.dismiss();
                            try {
                                List<UserRecords> imageRecords = parse(jsonObject);


                                mAdapter.swapImageRecords(imageRecords);
                            }
                            catch(JSONException e) {
                                Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            VolleyApplication.getInstance().getRequestQueue().add(request);
        }

        private List<UserRecords> parse(final JSONObject json) throws JSONException {
            ArrayList<UserRecords> records = new ArrayList<UserRecords>();

            final JSONArray jsonImages = json.getJSONArray("items");

            for(int i =0; i < jsonImages.length(); i++) {
                JSONObject jsonImage = jsonImages.getJSONObject(i);
               final String login = jsonImage.getString("login");
                final String avatar_url = jsonImage.getString("avatar_url");
                final String html_url = jsonImage.getString("html_url");


                UserRecords record = new UserRecords(avatar_url, login, html_url);
                records.add(record);


            }



            return records;
        }

        //Method that pass the intent extra data to the DetailActivity
        public void passData(){


            final ListView listView = (ListView) getView().findViewById(R.id.list1);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    TextView login = (TextView) view.findViewById(R.id.text1);
                    TextView html_url = (TextView) view.findViewById(R.id.text2);
                    TextView avatar_url = (TextView) view.findViewById(R.id.text3);
                    String loginData = login.getText().toString();
                    String html_urldata = html_url.getText().toString();
                    String avatar_urldata = avatar_url.getText().toString();

                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("login", loginData);
                    intent.putExtra("html_url", html_urldata);
                    intent.putExtra("avatar_url", avatar_urldata);
                    startActivity(intent);
                }

            });

        }


    }


}
