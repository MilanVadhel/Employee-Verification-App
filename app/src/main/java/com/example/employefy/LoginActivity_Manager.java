package com.example.employefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity_Manager extends AppCompatActivity implements GETURL {

    TextInputEditText username, password;
    Button loginButton;
    String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_manager);
        username = findViewById(R.id.managerUsername);
        password = findViewById(R.id.managerPassword);
        loginButton = findViewById(R.id.loginManager_Btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString();
                String Passsword = password.getText().toString();
                if (Username.equalsIgnoreCase("") || Passsword.equalsIgnoreCase("")) {
                    username.setError("Fill Username");
                    password.setError("Fill Password");
                } else if (Username.equalsIgnoreCase("manager") && Passsword.equalsIgnoreCase("manager")) {
                    generateTokenOfManager();
                    startActivity(new Intent(LoginActivity_Manager.this, EmployeListActivity.class));
                } else {
                    Toast.makeText(LoginActivity_Manager.this, "Username or Password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void generateTokenOfManager() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                            Toast.makeText(LoginActivity_Manager.this, "Token: " + token, Toast.LENGTH_SHORT).show();
                            updateTokenOfManager(token);
                        } else {
                            Toast.makeText(LoginActivity_Manager.this, "Token Generation Fail", Toast.LENGTH_LONG).show();
                            token = "Fail";
                        }
                    }
                });
    }

    private void updateTokenOfManager(final String token) {
        if (!token.isEmpty() || token != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, updatetoken_manager, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("success")) {
                        Toast.makeText(LoginActivity_Manager.this, "Token is Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity_Manager.this, "Token Updation is Fail", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity_Manager.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("TOKEN", token);
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity_Manager.this);
            requestQueue.add(stringRequest);
        }
    }
}
