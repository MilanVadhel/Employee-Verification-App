package com.example.employefy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileActivity extends AppCompatActivity implements GETURL{

    CircleImageView Profile;
    Toolbar toolbar;
    TextView contactNo,firstname,lastname,email,address,city,country,state;
    Button verifyButton;
    final String url_verify="http://192.168.0.103/EmployeFy/verify.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        toolbar=findViewById(R.id.toolbar_profileActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfileActivity.this,EmployeListActivity.class));
                finish();
            }
        });


        firstname=findViewById(R.id.Firstname);
        lastname=findViewById(R.id.Lastname);
        email=findViewById(R.id.Email);
        address=findViewById(R.id.Address);
        city=findViewById(R.id.City);
        country=findViewById(R.id.Country);
        state=findViewById(R.id.State);
        contactNo=findViewById(R.id.ContactNo);
        Profile=findViewById(R.id.profil_picture);
        Intent intent=getIntent();
        final HashMap<String, String> data= (HashMap<String, String>) intent.getExtras().get("EmployeData");
        Glide.with(this).load(data.get("PROFILEIMAGE")).into(Profile);
        firstname.setText(data.get("FIRSTNAME"));
        lastname.setText(data.get("LASTNAME"));
        email.setText("Email: "+data.get("EMAIL"));
        address.setText("Address: "+data.get("ADDRESS"));
        contactNo.setText("Contact No: "+data.get("CONTACTNO"));
        city.setText("City: "+data.get("CITY"));
        country.setText("Country: "+data.get("COUNTRY"));
        state.setText("State: "+data.get("STATE"));
        verifyButton=findViewById(R.id.Verify_Btn);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify(data.get("EMAIL"));
            }
        });
    }
    private void verify(final String EMAIL)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, verify_profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Success"))
                {
                    startActivity(new Intent(ViewProfileActivity.this,EmployeListActivity.class));
                    Toast.makeText(ViewProfileActivity.this, "Successfully Verified", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewProfileActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("EMAIL",EMAIL);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
