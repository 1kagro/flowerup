package com.smarthing.flowerup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    TextInputEditText nombre, apellidos, correo, pass, pass2, username;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView parrafo;
    private Dialog myDialog;
    private CheckBox val;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombre = findViewById(R.id.txI_name);
        apellidos = findViewById(R.id.txI_apellidos);
        correo = findViewById(R.id.txI_email);
        pass = findViewById(R.id.txI_password);
        pass2 = findViewById(R.id.txI_password2);
        username = findViewById(R.id.txI_username);
        val = findViewById(R.id.checkBox);
        myDialog = new Dialog(this);


    }

    public void sign_up(View view) {


        if(!validarCorreo(correo.getText().toString())){
            Toast.makeText(this, "El correo no es valido", Toast.LENGTH_SHORT).show();
        }else{
            if (!valLenPass(pass.getText().toString())){
                Toast.makeText(this, "la contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            }else{
                if (!valPass(pass.getText().toString(),pass2.getText().toString())){
                    Toast.makeText(this, "las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }else{
                    if(!val.isChecked()){
                        Toast.makeText(this, "Debes aceptar la politca de tratamiento de datos", Toast.LENGTH_SHORT).show();
                    }else{
                        create_user("http://" + getResources().getString(R.string.ip_pc) + "/flowerup/php/androidBd/reg_user.php");
                    }
                }
            }
        }



    }

    public void ShowPopup(View v) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.terminos_condiciones);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);


        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void sign_in(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public boolean validarCorreo(String correo) {
        boolean val=true;
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        // El email a validar
        String email = correo;

        Matcher mather = pattern.matcher(email);

        if (mather.find()) {
             val=true;
        } else {
          val = false;

        }

        return val;
    }

    public boolean valPass(String pass, String pass2){

        boolean val = false;

        if (pass.equals(pass2)){
            val = true;
        }else{
            val=false;
        }

        return val;
    }

    public boolean valLenPass(String pass){

        boolean val = true;

        if (pass.length()<8){
            val = false;
        }


        return val;
    }



    private void create_user(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    Toast.makeText(Register.this, "!Usuario creado correctamente, ya puedes iniciar sesion!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Register.this, "Usuario o contraseña incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String,String>();
                parametros.put("nombre",nombre.getText().toString());
                parametros.put("apellidos",apellidos.getText().toString());
                parametros.put("email",correo.getText().toString());
                parametros.put("password",pass2.getText().toString());
                parametros.put("username",username.getText().toString());


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}