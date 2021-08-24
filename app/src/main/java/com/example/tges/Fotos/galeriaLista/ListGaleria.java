package com.example.tges.Fotos.galeriaLista;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tges.Fotos.Ver_Detalle_Foto;
import com.example.tges.Fotos.subir_foto;
import com.example.tges.R;
import com.example.tges.SQLite.Conexion;
import com.example.tges.Utilidades.Variables;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListGaleria extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    ArrayList<VOGaleria> listablogs;
    Conexion conn;
    SearchView SVBuscador;
    Button CmdAtras;
    ProgressBar progressBar;
    TextView TV_TituloBusqueda;
    FloatingActionButton CmdAgregarFoto;
    RecyclerView recyclerView;
    AdapterGaleria adapter;
    int reload=0;
    int control_reload=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_galeria);
        conn = new Conexion(this, Variables.DB_GALERIA, null, 1);
        //Se inicializa la lista
        listablogs = new ArrayList<>();
        SVBuscador=findViewById(R.id.SVBuscador);
        TV_TituloBusqueda =findViewById(R.id.TV_TituloBusqueda);
        progressBar=findViewById(R.id.progressBar);
        CmdAgregarFoto=findViewById(R.id.CmdAgregarFoto);
        CmdAgregarFoto.setOnClickListener(this);
        CmdAtras=findViewById(R.id.CmdAtras);
        CmdAtras.setOnClickListener(this);
        recyclerView=findViewById(R.id.RecyclerId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);
        ConsultarListado();
        adapter = new AdapterGaleria(ListGaleria.this, listablogs);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListGaleria.this, Ver_Detalle_Foto.class);
                Variables.Id_Foto=listablogs.get(recyclerView.getChildAdapterPosition(v)).getId();
                startActivity(intent);
            }
        });
        TV_TituloBusqueda.setText("Geleria");
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        initListener();
    }

    private void ConsultarListado() {
        final ProgressDialog loading = ProgressDialog.show(this, "Cargando la información...", "Un momento por favor...", false, false);
        SQLiteDatabase db = conn.getReadableDatabase();
        VOGaleria VOGaleria = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Variables.TABLA_GALERIA,null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            listablogs.add(new VOGaleria(id, name, description, image));
        }
        loading.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (control_reload == 1) {
            if (reload == 1) {
                finish();
                startActivity(getIntent());
                reload = 0;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        reload=1;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.CmdAtras:
                finish();
                break;
            case R.id.CmdAgregarFoto:
                intent = new Intent(ListGaleria.this, subir_foto.class);
                startActivity(intent);
                control_reload=1;
                break;
        }
    }

    private void initListener(){
        SVBuscador.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }
}
