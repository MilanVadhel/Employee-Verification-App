package com.example.employefy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.employefy.Adapters.EmployeAdapter;
import com.example.employefy.Models.Employe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeListActivity extends AppCompatActivity implements GETURL{

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
   // Button requestedEmployeButton,verifiedEmployeButton;
    final String url_pendingemloyes="http://192.168.0.103/EmployeFy/pendingProfiles.php";
    final String url_verifiedemployes="";
    List<Employe> pendingEmployeList,verifiedEmployeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employelist);
        swipeRefreshLayout=findViewById(R.id.Refresh);
        toolbar=findViewById(R.id.toolbar_employeListActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmployeListActivity.this,LoginActivity_Manager.class));
                finish();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requests();
            }
        });


        recyclerView=findViewById(R.id.recyclerView_Employe);
        requests();
        //requestedEmployeButton=findViewById(R.id.requestEmp_Btn);
//        requestedEmployeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requests();
//            }
//        });
//        verifiedEmployeButton=findViewById(R.id.verifiedEmp_Btn);
//        verifiedEmployeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                verified();
//            }
//        });
    }
    public void requests()
    {
        swipeRefreshLayout.setRefreshing(true);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.show();
        pendingEmployeList=new ArrayList<>();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, pending_employee_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("Employes");
                    if(jsonArray.length() != 0)
                    {
                        pendingEmployeList.clear();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object=jsonArray.getJSONObject(i);
                            Employe employe=new Employe(object.getInt("ID"),
                                    object.getString("EMAIl"),
                                    object.getString("PROFILEIMAGE"),
                                    object.getString("FIRSTNAME"),
                                    object.getString("LASTNAME"),
                                    object.getString("CONTACTNO"),
                                    object.getString("ADDRESS"),
                                    object.getString("CITY"),
                                    object.getString("STATE"),
                                    object.getString("COUNTRY"),
                                    object.getString("STATUS"));
                            pendingEmployeList.add(employe);
                        }
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(EmployeListActivity.this);
                        EmployeAdapter employeAdapter=new EmployeAdapter(EmployeListActivity.this,pendingEmployeList);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(employeAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                        //employeAdapter.notifyDataSetChanged();
                        //startActivity(new Intent(EmployeListActivity.this,EmployeListActivity.class).putExtra("LIST", pendingEmployeList));
                    }
                }catch (Exception e)
                {
                    progressDialog.dismiss();
                    Toast.makeText(EmployeListActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EmployeListActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
//    public  void verified()
//    {
//        verifiedEmployeList=new ArrayList<>();
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, url_verifiedemployes, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    JSONArray jsonArray=jsonObject.getJSONArray("PENDING");
//                    if(jsonArray.length() != 0)
//                    {
//                        verifiedEmployeList.clear();
//                        for(int i=0;i<jsonArray.length();i++)
//                        {
//                            JSONObject object=jsonArray.getJSONObject(i);
//                            Employe employe=new Employe(object.getInt("ID"),
//                                    object.getString("EMAIl"),
//                                    object.getString("PROFILEIMAGE"),
//                                    object.getString("FIRSTNAME"),
//                                    object.getString("LASTNAME"),
//                                    object.getString("CONTACTNO"),
//                                    object.getString("ADDRESS"),
//                                    object.getString("CITY"),
//                                    object.getString("STATE"),
//                                    object.getString("COUNTRY"),
//                                    object.getString("STATUS"));
//                            verifiedEmployeList.add(employe);
//                        }
//                        startActivity(new Intent(EmployeListActivity.this,EmployeListActivity.class).putExtra("LIST", (Serializable) verifiedEmployeList));
//                    }
//                }catch (Exception e)
//                {
//                    Toast.makeText(EmployeListActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(EmployeListActivity.this, "Response Error: "+error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue requestQueue= Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
}
