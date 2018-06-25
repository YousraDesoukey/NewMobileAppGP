package com.asu.hebagp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.JSONObjectRequestListener;
//import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    ImageView imageView;
    TextView KingName;
    TextView KingDesc;
    TextView trial;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("kingsinfo");
    String retrievedKingName = "Ay";
    String url = "https://en.wikipedia.org/wiki/Text_display";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        Bitmap bm = (Bitmap) intent.getParcelableExtra("pic");
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setImageBitmap(bm);
        KingName = findViewById(R.id.textView);
        KingDesc = findViewById(R.id.textView2);

        myRef.child(retrievedKingName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getKey().toString().equalsIgnoreCase(retrievedKingName)) {
                    String name = snapshot.child("name").getValue(String.class);
                    String desc = snapshot.child("desc").getValue(String.class);
                    KingName.setText(name);
                    KingDesc.setText(desc);
                }

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("onCancelled", " cancelled");
            }
        });
        trial = (TextView) findViewById(R.id.textView4);
        trial.setText(imageconverter(bm));
        System.out.println(trial);
        post();
//    final RequestQueue requestqueue = Volley.newRequestQueue(Main2Activity.this);
//    StringRequest stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            trial.setText(response);
//            requestqueue.stop();
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            trial.setText("ERROR");
//            error.printStackTrace();
//            requestqueue.stop();
//        }
//    });
//    requestqueue.add(stringrequest);

    }
    public String imageconverter(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
    public void post(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplication(),"BRAVO",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),error+"ERROR",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                String x=trial.getText().toString();
                params.put("image",x);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}


//    private void loginUser() {
//        String userEmail, userPassword;
//        //userEmail = userEmailEditText.getText().toString().trim();
//        //userPassword = userPasswordEditText.getText().toString().trim();
//
//
//        if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword))
//        {
//
//            // Android Request
//            AndroidNetworking.post("http://64.52.86.76:5000/api/user/login")
//                    .addHeaders("content-type", "application/json")
//                    .addBodyParameter("email", userEmail)
//                    .addBodyParameter("password", userPassword)
//                    .setTag("Signup")
//                    .setPriority(Priority.MEDIUM)
//                    .build()
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            // do anything with response
//                            Intent moveToHome = new Intent(LogIn.this, NewsFeed.class);
//                            moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                            person= response;
//                            String ID="", name = "", image = "";
//                            try {
//                                ID = person.getJSONObject("user").getString("_id");
//                                name = person.getJSONObject("user").getString("name");
//                                image = person.getJSONObject("user").getString("profileImage");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
//                            SharedPreferences.Editor editor = preferences.edit();
//                            editor.putString("name", name);
//                            editor.putString("id", ID);
//                            editor.putString("image", image);
//                            editor.putBoolean("login", true);
//                            editor.apply();
//
//                            moveToHome.putExtra("_id", ID);
//                            Log.d("3:", "onResponse: "+ID);
//                            startActivity(moveToHome);
//                            mProgressDialog.dismiss();
//                            Log.d(TAG, "onResponse: "+response.toString());
//                        }
//                        @Override
//                        public void onError(ANError error) {
//                            // handle error
//                            Toast.makeText(LogIn.this, "unable to login user", Toast.LENGTH_LONG).show();
//                            mProgressDialog.dismiss();
//                        }
//                    });
//
//        }else
//        {
//            Toast.makeText(LogIn.this, "please enter email and password", Toast.LENGTH_LONG).show();
//            mProgressDialog.dismiss();
//        }
//
//    }





