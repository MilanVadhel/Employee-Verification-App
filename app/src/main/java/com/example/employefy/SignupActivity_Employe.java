package com.example.employefy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity_Employe extends AppCompatActivity implements GETURL{

    TextView loginLink;
    Button signupButton;
    TextInputEditText username,email,password,confirmpassword;
    final String url_signup="http://192.168.0.103/EmployeFy/signup.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__employe);
        username=findViewById(R.id.employeUsername);
        email=findViewById(R.id.employeEmail);
        password=findViewById(R.id.employePassword);
        confirmpassword=findViewById(R.id.employeConfirmPassword);
        loginLink=findViewById(R.id.login_Link);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity_Employe.this,LoginActivity_Employe.class));
            }
        });
        signupButton=findViewById(R.id.singupEmploye_Btn);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String EmpUsername=username.getText().toString();
                final String EmpEmail=email.getText().toString();
                final String EmpPassword=password.getText().toString();
                final String EmpConfirmPassword=confirmpassword.getText().toString();
                if(EmpEmail.equalsIgnoreCase("") || EmpUsername.equalsIgnoreCase("") || EmpConfirmPassword.equalsIgnoreCase("") || EmpPassword.equalsIgnoreCase(""))
                {
                    username.setError("Fill username");
                    email.setError("Fill email");
                    password.setError("Fill password");
                    confirmpassword.setError("Fill confirm password");
                }
                else if(!(EmpPassword.equals(EmpConfirmPassword)))
                {
                    Toast.makeText(SignupActivity_Employe.this,"Password does not match",Toast.LENGTH_LONG).show();
                }
                else
                {
                    signup(EmpUsername,EmpEmail,EmpPassword);
                }
            }
        });
    }
    private void signup(final String EmpUsername, final String EmpEmail, final String EmpPassword)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, signup_employee, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(response.toString().equals("Success"))
                    {
                        Toast.makeText(SignupActivity_Employe.this,"Signup Successfully",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(SignupActivity_Employe.this,"Signup Error: "+e.toString(),Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(SignupActivity_Employe.this,LoginActivity_Employe.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity_Employe.this,"Signup Error: "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("USERNAME",EmpUsername);
                params.put("EMAIL",EmpEmail);
                params.put("PASSWORD",EmpPassword);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
