package com.markeisjones.myusers.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.markeisjones.myusers.R;
import com.markeisjones.myusers.adapters.RecyclerViewAdapter;
import com.markeisjones.myusers.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity {

    private Button Post_Button;
    private EditText Post_ID;
    private EditText Post_FN;
    private EditText Post_LN;
    private Button Choose_Button;
    private ImageView Post_Avatar;
    Bitmap bitmap;
    ArrayList <UserModel> userModels = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRecyclerViewAdapter;
    RequestQueue mRequestQueue;
    RecyclerViewAdapter adapter;


    final int CODE_GALLARY_REQUEST = 999;
    String urlUpload = "https://reqres.in/api/users?page=2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Post_Button = (Button) findViewById(R.id.postBTN);
        Choose_Button = (Button) findViewById(R.id.chooseBTN);
        Post_ID = (EditText) findViewById(R.id.postID);
        Post_FN = (EditText) findViewById(R.id.postFN);
        Post_LN = (EditText) findViewById(R.id.postLN);
        Post_Avatar = (ImageView) findViewById(R.id.postAvatar) ;
        mRecyclerView = findViewById(R.id.recycler_view);


        Choose_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddUser.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLARY_REQUEST
                );




            }
        });

        Post_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {{

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray jsonArray = object.optJSONArray("data");
                            for(int i = 0; i < jsonArray.length();i++){

                                JSONObject data = jsonArray.getJSONObject(i);
                                String id = data.getString("id");
                                String first_name = data.getString("first_name");
                                String last_name = data.getString("last_name");
                                String imageurl = data.getString("avatar");

                                object.put("data", jsonArray.toString());
                                object.put("id",object.toString());
                                object.put("first_name",object.toString());
                                object.put("last_name",object.toString());
                                object.put("avatar",object.toString());

                                userModels.add(new UserModel(
                                        id,
                                        first_name,
                                        last_name,
                                        imageurl )
                                );




                            }
                            mRecyclerViewAdapter = new RecyclerViewAdapter(AddUser.this, userModels);
                            mRecyclerView.setAdapter(mRecyclerViewAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("data", "");
                        return headers;
                    }
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String>params = new HashMap<>();
                        String imageData = imageToString(bitmap);

                        params.put("avatar", imageData);
                        params.put("id", "");
                        params.put("first_name", "");
                        params.put("last_name", "");



                        return  params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(AddUser.this);
                requestQueue.add(stringRequest);


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLARY_REQUEST){
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"select Image"), CODE_GALLARY_REQUEST);

            }else{
                Toast.makeText(getApplicationContext(),"You dont have permission to access gallery!", Toast.LENGTH_LONG).show();
            }return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_GALLARY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filePath= data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                Post_Avatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
}
