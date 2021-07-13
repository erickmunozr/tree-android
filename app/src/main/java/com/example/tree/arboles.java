package com.example.tree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import OpenHelper.SQLite_OpenHelper;

public class arboles extends AppCompatActivity {

    SQLite_OpenHelper helper= new SQLite_OpenHelper(this, "tree", null, 1);
    Button btnSalirArbol, btnGuardarArbol;
    EditText txtArbolArbol, txtAltoArbol, txtDiametroArbol, txtCalidadArbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arboles);

        btnGuardarArbol=(Button)findViewById(R.id.btnGuardarArbol);
        btnSalirArbol=(Button)findViewById(R.id.btnSalirArbol);

        txtArbolArbol=(EditText)findViewById(R.id.txtArbolArbol);
        txtAltoArbol=(EditText)findViewById(R.id.txtAlturaArbol);
        txtDiametroArbol=(EditText)findViewById(R.id.txtDiametroArbol);
        txtCalidadArbol=(EditText)findViewById(R.id.txtCalidadArbol);

        btnGuardarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtArbolArbol.getText().toString()) &&
                        !TextUtils.isEmpty(txtAltoArbol.getText().toString()) &&
                        !TextUtils.isEmpty(txtDiametroArbol.getText().toString()) &&
                        !TextUtils.isEmpty(txtCalidadArbol.getText().toString())){
                    helper.abrirBD();
                    helper.insertaArbolBD(Integer.parseInt(String.valueOf(txtArbolArbol.getText())),
                            Integer.parseInt(String.valueOf(txtAltoArbol.getText())),
                            Integer.parseInt(String.valueOf(txtDiametroArbol.getText())),
                            Integer.parseInt(String.valueOf(txtCalidadArbol.getText())));
                    helper.cerrarBD();

                    txtArbolArbol.setText("");
                    txtAltoArbol.setText("");
                    txtDiametroArbol.setText("");
                    txtCalidadArbol.setText("");
                    txtArbolArbol.findFocus();

                    Snackbar.make(v, "Arbol guardado con éxito!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    Snackbar.make(v, "Por favor regista todos los campos", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        btnSalirArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), zonas.class);
                startActivity(i);
            }
        });
    }
}