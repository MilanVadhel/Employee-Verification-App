package com.example.employefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.employefy.Models.Card;
import com.example.employefy.Session.SessionManager;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CardGenerationActivity extends AppCompatActivity implements GETURL{

    CardView cardView;
    Button downloadButton;
    Toolbar toolbar;
    CardView sorrycard;
    SessionManager sessionManager;
    HashMap<String,String> emp;
    final String url_preview="http://192.168.0.103/EmployeFy/previewcard.php";
    ImageView profileView;
    TextView firstNameView,lastNameView,contactNoView,addressView,cityView,stateView,countryView;
    Card card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_generation);
        cardView=findViewById(R.id.employeCardLayout);

        profileView=findViewById(R.id.card_ProfileImage);
        firstNameView=findViewById(R.id.card_firstname);
        lastNameView=findViewById(R.id.card_lastname);
        contactNoView=findViewById(R.id.card_contactno);
        addressView=findViewById(R.id.card_address);
        cityView=findViewById(R.id.card_city);
        stateView=findViewById(R.id.card_state);
        countryView=findViewById(R.id.card_country);

        ActivityCompat.requestPermissions(CardGenerationActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        downloadButton=findViewById(R.id.download_Btn);
        sorrycard=findViewById(R.id.sorryCard);
        toolbar=findViewById(R.id.toolbar_cardgenerationactivity);

        //get all detail if user is login
        sessionManager=new SessionManager(this);
        sessionManager.checkLogin();
        emp=sessionManager.getUserDetail();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardGenerationActivity.this,LoginActivity_Employe.class));
                finish();
            }
        });



        Intent intent=getIntent();
        String STATUS=intent.getStringExtra("STATUS");
        if(STATUS.equalsIgnoreCase("1")) ///for pending show sorry Message
        {
            sorrycard.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Not Accepted", Toast.LENGTH_SHORT).show();
        }
        else
        {
            previewCard(emp.get(sessionManager.EMAIL));
            cardView.setVisibility(View.VISIBLE);
            downloadButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Accepted", Toast.LENGTH_SHORT).show();
        }
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdf(card.getProfile(),card.getFirstname(),card.getLastname(),card.getContactno(),card.getAddress(),card.getCity(),card.getState(),card.getCountry());
            }
        });

        /*Check here if logged in user stattus is 1 then shoe Pending else Show Card*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logoutEmploye:
                sessionManager.logout();
                break;
        }
        return true;
    }

    private void previewCard(final String empEmailId)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, preview_card, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //set all the things
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("Card");
                    if(jsonArray.length()!=0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            card=new Card(object.getString("PROFILEIMAGE"),object.getString("FIRSTNAME"),object.getString("LASTNAME"),
                                    object.getString("CONTACTNO"),object.getString("ADDRESS"),
                                    object.getString("CITY"),object.getString("STATE"),object.getString("COUNTRY"));

                            Glide.with(CardGenerationActivity.this).load(card.getProfile()).into(profileView);
                            firstNameView.setText(card.getFirstname());
                            lastNameView.setText(card.getLastname());
                            contactNoView.setText(" Contact No: "+card.getContactno());
                            addressView.setText("Address: "+card.getAddress());
                            cityView.setText("                "+card.getCity());
                            stateView.setText(","+card.getState());
                            countryView.setText(","+card.getCountry());




//                            Glide.with(CardGenerationActivity.this).load(object.getString("PROFILEIMAGE")).into(profileView);
//                            firstNameView.setText(object.getString("FIRSTNAME"));
//                            lastNameView.setText(object.getString("LASTNAME"));
//                            contactNoView.setText(" Contact No: "+object.getString("CONTACTNO"));
//                            addressView.setText("Address: "+object.getString("ADDRESS"));
//                            cityView.setText("                "+object.getString("CITY"));
//                            stateView.setText(","+object.getString("STATE"));
//                            countryView.setText(","+object.getString("COUNTRY"));
                        }
                    }
                    else
                    {
                        Toast.makeText(CardGenerationActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(CardGenerationActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CardGenerationActivity.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("EMAIL",empEmailId);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void createPdf(String profile,String fname,String lname,String contact,String address,String city,String state,String country)
    {
        Document doc=new Document(PageSize.A5);
        String outpath= Environment.getExternalStorageDirectory()+"/"+fname+".pdf";
        try
        {
            PdfWriter.getInstance(doc, new FileOutputStream(outpath));
            doc.open();
            doc.add(new Paragraph(fname+" "+lname));
            doc.add(new Paragraph("Contact No: "+contact));
            doc.add(new Paragraph("Address: "+address+","+city));
            doc.add(new Paragraph("State: "+state+"  Country: "+country));
            Toast.makeText(this, "Success Created", Toast.LENGTH_LONG).show();
            doc.close();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Fail to create: "+e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
