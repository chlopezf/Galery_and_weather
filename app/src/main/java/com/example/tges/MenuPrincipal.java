package com.example.tges;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tges.EstadoTiempo.Estado_Tiempo;
import com.example.tges.Fotos.Menu_Fotos;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener {
    Button CmdFotos, CmdEstadoTiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        //Buttons
        CmdFotos=findViewById(R.id.CmdFotos);
        CmdEstadoTiempo=findViewById(R.id.CmdEstadoTiempo);
        //OnclickListener
        CmdFotos.setOnClickListener(this);
        CmdEstadoTiempo.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.CmdFotos:
                intent = new Intent(MenuPrincipal.this, Menu_Fotos.class);
                startActivity(intent);
                break;
            case R.id.CmdEstadoTiempo:
                intent = new Intent(MenuPrincipal.this, Estado_Tiempo.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}