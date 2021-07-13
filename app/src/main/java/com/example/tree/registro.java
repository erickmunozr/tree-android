package com.example.tree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import OpenHelper.SQLite_OpenHelper;

public class registro extends AppCompatActivity {

    Button regBtnGrabarUsuario;
    EditText regNombre_t, regUsuario_t, regContrasena_t;

    SQLite_OpenHelper helper= new SQLite_OpenHelper(this, "tree", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        regBtnGrabarUsuario=(Button)findViewById(R.id.btnRegistrar);
        regNombre_t=(EditText)findViewById(R.id.txtNombreReg);
        regUsuario_t=(EditText)findViewById(R.id.txtUsuarioReg);
        regContrasena_t=(EditText)findViewById(R.id.txtContrasenaReg);

        regBtnGrabarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(regNombre_t.getText().toString()) &&
                        !TextUtils.isEmpty(regUsuario_t.getText().toString()) &&
                        !TextUtils.isEmpty(regContrasena_t.getText().toString())){

                    helper.abrirBD();
                    helper.insertaUsuariosBD(String.valueOf(regNombre_t.getText()),
                            String.valueOf(regUsuario_t.getText()),
                            String.valueOf(regContrasena_t.getText()),
                            "1");
                    helper.cerrarBD();

                    Snackbar.make(v, "Usuario registrado con éxito!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent i=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }else{
                    Snackbar.make(v, "Por favor regista todos los campos", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }
}