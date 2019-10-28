package com.eg.crudmysql;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private EditText etcodigo, etdescripcion, etprecio;
    private Button btnguardar, btnconsultarcodigo, btnconsultardescripcion, btneliminar, btnactualizar;
    private TextView tvresultado;
    //Admin admin = new
    boolean inputC = false;
    boolean inputD = false;
    boolean inputP = false;
    int resultadoInsert = 0;

    MantenimientoMysql manto = new MantenimientoMysql();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        etcodigo = (EditText) findViewById(R.id.etcodigo);
        etdescripcion = (EditText) findViewById(R.id.etdetalle);
        etprecio = (EditText) findViewById(R.id.etprecio);
        btnconsultarcodigo = (Button) findViewById(R.id.btnconsultacodigo);
        btnconsultardescripcion = (Button) findViewById(R.id.btnconsultardescripcion);
        btnguardar = (Button) findViewById(R.id.btnguardar);
        btneliminar = (Button) findViewById(R.id.btneliminar);
        btnactualizar = (Button) findViewById(R.id.btnactualizar);

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etcodigo.getText().toString().length()==0){
                    etcodigo.setError("Campo obligatorio");
                    inputC = false;
                }else{
                    inputC = true;
                }
                if (etdescripcion.getText().toString().length()==0){
                    etdescripcion.setError("Campo obligatorio");
                    inputD = false;
                }else{
                    inputD = true;
                }
                if (etprecio.getText().toString().length()==0){
                    etprecio.setError("Campo obligatorio");
                    inputP = false;
                }else{
                    inputP = true;
                }

                if (inputC && inputD && inputP){
                    String codigo = etcodigo.getText().toString();
                    String descripcion = etdescripcion.getText().toString();
                    String precio = etprecio.getText().toString();
                    manto.guardar(Main2Activity.this,codigo,descripcion,precio);
                }
            }
        });
    }

}
