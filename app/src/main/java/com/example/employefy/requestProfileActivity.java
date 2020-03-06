package com.example.employefy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.employefy.Session.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class requestProfileActivity extends AppCompatActivity implements GETURL{

    TextInputEditText firstname,lastname,contactno,address,city,state,country;
    Button requestButton;
    final String url_request="http://192.168.0.103/EmployeFy/request.php";
    SessionManager sessionManager;
    HashMap<String,String> employe;
    ImageButton chooseImageButton;
    CircleImageView circleProfile;
    private Bitmap bitmap;
    public  String lid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_profile);
        circleProfile=findViewById(R.id.setProfile);
        chooseImageButton=findViewById(R.id.chooseImageButton);
        firstname=findViewById(R.id.e_firstname);
        lastname=findViewById(R.id.e_lastname);
        contactno=findViewById(R.id.e_contactno);
        address=findViewById(R.id.e_address);
        city=findViewById(R.id.e_city);
        state=findViewById(R.id.e_state);
        country=findViewById(R.id.e_country);
        requestButton=findViewById(R.id.request_Btn);
        sessionManager=new SessionManager(this);
        sessionManager.checkLogin();
        employe=sessionManager.getUserDetail();
        Intent intent=getIntent();
        lid=intent.getStringExtra("LID");
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Fname=firstname.getText().toString();
                final String Lname=lastname.getText().toString();
                final String ContactNo=contactno.getText().toString();
                final String Address=address.getText().toString();
                final String City=city.getText().toString();
                final String State=state.getText().toString();
                final String Country=country.getText().toString();
                if(Fname.equalsIgnoreCase("") || Lname.equalsIgnoreCase("") || ContactNo.equalsIgnoreCase("")
                || Address.equalsIgnoreCase("") || City.equalsIgnoreCase("") || State.equalsIgnoreCase("")
                        || Country.equalsIgnoreCase(""))
                {
                    firstname.setError("Fill Firstname");
                    lastname.setError("Fill Lastname");
                    contactno.setError("Fill Contactno");
                    address.setError("Fill Address");
                    city.setError("Fill City");
                    state.setError("Fill State");
                    country.setError("Fill Country");
                }
                else
                {
                    request(getStringImage(bitmap),employe.get(sessionManager.EMAIL),Fname,Lname,ContactNo,Address,City,State,Country);
                }

            }
        });

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });


    }
    private void request(final String photo,final String Email,final String Fname, final String Lname, final String ContactNo, final String Address, final String City, final String State, final String Country)
    {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Requesting...");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, request_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Success"))
                {
                    Toast.makeText(requestProfileActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    sendNotificationToManager(lid);
                    startActivity(new Intent(requestProfileActivity.this,CardGenerationActivity.class).putExtra("STATUS","1"));
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(requestProfileActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(requestProfileActivity.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("IMAGE",photo);
                params.put("EMAIL",Email);
                params.put("FNAME",Fname);
                params.put("LNAME",Lname);
                params.put("CONTACTNO",ContactNo);
                params.put("ADDRESS",Address);
                params.put("CITY",City);
                params.put("STATE",State);
                params.put("COUNTRY",Country);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void chooseFile()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            Uri filePath=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                circleProfile.setImageBitmap(bitmap);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByteArray=byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodeImage;
    }
    private void sendNotificationToManager(final String lid)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, notification_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(requestProfileActivity.this,response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requestProfileActivity.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("LIDOFEMPLOYEE",lid);
                return map;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(requestProfileActivity.this);
        requestQueue.add(stringRequest);
    }
}
