package com.example.employefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.employefy.Notification.TokenGenerator;
import com.example.employefy.Services.MyFirebaseMessagingService;
import com.example.employefy.Session.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity_Employe extends AppCompatActivity implements GETURL{

    TextInputEditText emailId,password;
    Button loginButton;
    final String url_login="http://192.168.0.103/EmployeFy/login.php";
    SessionManager sessionManager;
    String token=null;
    static String _lid=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__employe);
        emailId=findViewById(R.id.employeId);
        password=findViewById(R.id.employePass);
        loginButton=findViewById(R.id.loginEmploye_Btn);
        sessionManager=new SessionManager(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String EmailId=emailId.getText().toString();
                final String Password=password.getText().toString();
                if(EmailId.equalsIgnoreCase("") || Password.equalsIgnoreCase(""))
                {
                    emailId.setError("Fill Email");
                    password.setError("Fill Password");
                }
                else
                {
                    login(EmailId,Password);
                }
            }
        });
    }
    private void login(final String EmailId, final String Password)
    {
        //final TokenGenerator tokenGenerator=new TokenGenerator();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, login_employee, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("login");
                    if(jsonObject.getString("check").equalsIgnoreCase("success"))
                    {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String _username = obj.getString("username");
                            String _email = obj.getString("email");
                            String _status=obj.getString("status");
                            _lid=obj.getString("lid");
                            Toast.makeText(LoginActivity_Employe.this, "Login Success with " + _email, Toast.LENGTH_LONG).show();
                            /*****************************************/
                            generateTokenOfEmployee();
                            //Toast.makeText(LoginActivity_Employe.this, "Token: "+token, Toast.LENGTH_LONG).show();
                           // updateTokenOfEmployee(_lid,token);
                            /*********************************************/
                            // Create Session
                            sessionManager.createSession(_username, _email);
                            /*********************************************/
                            if(_status.equalsIgnoreCase("0")) //Not Requested
                            {
                                startActivity(new Intent(LoginActivity_Employe.this, requestProfileActivity.class).putExtra("LID",_lid));
                                Toast.makeText(LoginActivity_Employe.this, "Please Request to Manager", Toast.LENGTH_SHORT).show();
                            }
                            else if(_status.equalsIgnoreCase("1")) //Pending Request
                            {
                                startActivity(new Intent(LoginActivity_Employe.this,CardGenerationActivity.class).putExtra("STATUS",_status));
                            }
                            else if(_status.equalsIgnoreCase("2")) //Accepted Request
                            {
                                startActivity(new Intent(LoginActivity_Employe.this,CardGenerationActivity.class).putExtra("STATUS",_status));
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginActivity_Employe.this, "Email or Password is Incorrect", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(LoginActivity_Employe.this, "Email or Password is Incorrect"+e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity_Employe.this, "Login Fail"+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("EMAIL",EmailId);
                params.put("PASSWORD",Password);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void generateTokenOfEmployee()
    {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful())
                        {
                            token=task.getResult().getToken();
                            Toast.makeText(LoginActivity_Employe.this, "Token: "+token, Toast.LENGTH_SHORT).show();
                            updateTokenOfEmployee(_lid,token);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity_Employe.this, "Token Generation Fail", Toast.LENGTH_LONG).show();
                            token="Fail";
                        }
                    }
                });
    }
    private void updateTokenOfEmployee(final String lid,final String token)
    {
        if(!token.isEmpty() || token!=null)
        {
            StringRequest stringRequest=new StringRequest(Request.Method.POST, updateToken_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equalsIgnoreCase("success"))
                    {
                        Toast.makeText(LoginActivity_Employe.this, "Token is Updated", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity_Employe.this, "Token Updation is Fail", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity_Employe.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map=new HashMap<>();
                    map.put("LID",lid);
                    map.put("TOKEN",token);
                    return map;
                }
            };
            RequestQueue requestQueue=Volley.newRequestQueue(LoginActivity_Employe.this);
            requestQueue.add(stringRequest);
        }
    }
}
