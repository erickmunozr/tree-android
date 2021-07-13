package com.example.tree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import OpenHelper.SQLite_OpenHelper;

public class MainActivity extends AppCompatActivity {

    TextView tvRegistrate;
    Button btnIngresar;
    SQLite_OpenHelper helper = new SQLite_OpenHelper(this, "tree", null, 1);
    String strNombre_g="", strUsuario_g="";
    Integer intUsuarioId_i;

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this,
                        new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, permission + " is already granted.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String validaSesion=helper.buscaSesion();
        if (validaSesion=="ok"){
            Intent i=new Intent(getApplicationContext(), zonas.class);
            startActivity(i);
        }

        tvRegistrate=(TextView)findViewById(R.id.btnRegistrate);
        tvRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), registro.class);
                startActivity(i);
            }
        });

        btnIngresar=(Button)findViewById(R.id.btnEntrar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUsu=(EditText)findViewById(R.id.txtUsuario);
                EditText txtPass=(EditText)findViewById(R.id.txtContrasena);
                if (!TextUtils.isEmpty(txtPass.getText().toString()) && !TextUtils.isEmpty(txtUsu.getText().toString())){
                    try {
                        String validaSesion=helper.buscaCredenciales(txtUsu.getText().toString(),txtPass.getText().toString());
                        if (validaSesion=="ok"){
                            Intent i=new Intent(getApplicationContext(), zonas.class);
                            startActivity(i);
                        }else{
                            Snackbar.make(v, "Credenciales incorrectas", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            txtPass.setText("");
                            txtUsu.setText("");
                            txtUsu.findFocus();
                        }
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }else{
                    Log.i("ALV", "credenciales vacias");
                    Snackbar.make(v, "Ingresa las credenciales", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }
}