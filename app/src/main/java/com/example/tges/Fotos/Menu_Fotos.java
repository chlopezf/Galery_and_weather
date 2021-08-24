package com.example.tges.Fotos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tges.Fotos.galeriaLista.ListGaleria;
import com.example.tges.R;

public class Menu_Fotos extends AppCompatActivity implements View.OnClickListener {
    Button CmdTomarFoto, CmdListaFotos, CmdAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_fotos);
        //Buttons
        CmdTomarFoto=findViewById(R.id.CmdTomarFoto);
        CmdListaFotos=findViewById(R.id.CmdListaFotos);
        CmdAtras=findViewById(R.id.CmdAtras);
        //Onclicklistener
        CmdTomarFoto.setOnClickListener(this);
        CmdListaFotos.setOnClickListener(this);
        CmdAtras.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.CmdTomarFoto:
                intent=new Intent(Menu_Fotos.this, subir_foto.class);
                startActivity(intent);
                break;
            case R.id.CmdListaFotos:
                intent=new Intent(Menu_Fotos.this, ListGaleria.class);
                startActivity(intent);
                break;
            case R.id.CmdAtras:
                finish();
                break;
            default:
                break;
        }
    }
}