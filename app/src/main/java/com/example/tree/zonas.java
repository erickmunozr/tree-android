package com.example.tree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import OpenHelper.SQLite_OpenHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;

public class zonas extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spiLote;
    Button btnGuardaZona, btnExportar;
    SQLite_OpenHelper helper= new SQLite_OpenHelper(this, "tree", null, 1);
    EditText txtTrack, txtArea, txtArboles;
    String strLote_v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zonas);
        spiLote = (Spinner) findViewById(R.id.spiLoteZona);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lotes_zona_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiLote.setAdapter(adapter);

        btnExportar=(Button)findViewById(R.id.btnExportarDatos);
        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate data
                StringBuilder data = new StringBuilder();
                data.append("Lote,Track,Area,Arboles,Arbol,Altura,Diametro,Calidad");
                helper.abrirBD();
                Cursor c = helper.datosExportar();
                if (c != null) {
                    c.moveToFirst();
                    do {
                        String lote = c.getString(c.getColumnIndex("lote_i"));
                        String track = c.getString(c.getColumnIndex("track_t"));
                        String area = c.getString(c.getColumnIndex("area_t"));
                        String arboles = c.getString(c.getColumnIndex("arboles_i"));
                        String arbol = c.getString(c.getColumnIndex("arbol_i"));
                        String altura = c.getString(c.getColumnIndex("altura_i"));
                        String diametro = c.getString(c.getColumnIndex("diametro_i"));
                        String calidad = c.getString(c.getColumnIndex("calidad_i"));
                        data.append("\n"+lote+","+track+","+area+","+arboles+","+arbol+","+altura+","+diametro+","+calidad);
                    } while (c.moveToNext());
                }
                helper.cerrarBD();

                try{
                    //saving the file into device
                    FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    //exporting
                    Context context = getApplicationContext();
                    File filelocation = new File(getFilesDir(), "data.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.tree.fileprovider", filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnGuardaZona=(Button)findViewById(R.id.btnGuardarZona);
        btnGuardaZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTrack=(EditText)findViewById(R.id.txtTrackZona);
                txtArea=(EditText)findViewById(R.id.txtAreaZona);
                txtArboles=(EditText)findViewById(R.id.txtArbolesZona);
                strLote_v = spiLote.getSelectedItem().toString();
                helper.abrirBD();
                if (!TextUtils.isEmpty(txtArea.getText().toString()) && !TextUtils.isEmpty(txtArboles.getText().toString()) && !TextUtils.isEmpty(txtTrack.getText().toString()))
                helper.insertaZonaBD(strLote_v,
                        Integer.parseInt(txtTrack.getText().toString()),
                        Integer.parseInt(txtArea.getText().toString()),
                        Integer.parseInt(txtArboles.getText().toString()),
                        1);
                helper.cerrarBD();
                txtTrack.setText("");
                txtArea.setText("");
                txtArboles.setText("");
                Snackbar.make(v, "Zona guardada con éxito!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i=new Intent(getApplicationContext(), arboles.class);
                startActivity(i);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}