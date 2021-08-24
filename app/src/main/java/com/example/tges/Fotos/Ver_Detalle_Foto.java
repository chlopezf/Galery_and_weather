package com.example.tges.Fotos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tges.R;
import com.example.tges.SQLite.Conexion;
import com.example.tges.Utilidades.Variables;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Ver_Detalle_Foto extends AppCompatActivity implements View.OnClickListener {
    TextView TVTituloDetalle, TVDescripcionDetalle, TVIdBlog;
    ImageView IVFotoDetalle;
    Button CmdAtras;
    Conexion conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalle_foto);
        //crea conexion DB
        conn = new Conexion(this, Variables.DB_GALERIA, null, 1);
        //Textview
        TVTituloDetalle=findViewById(R.id.TVTituloDetalle);
        TVDescripcionDetalle=findViewById(R.id.TVDescripcionDetalle);
        TVIdBlog=findViewById(R.id.TVIdBlog);
        //Imageview
        IVFotoDetalle=findViewById(R.id.IVFotoDetalle);
        //Button
        CmdAtras=findViewById(R.id.CmdAtras);
        CmdAtras.setOnClickListener(this);
        ConsultarDetalle();
    }

    private void ConsultarDetalle() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {Variables.Id_Foto};
        String[] campos = {Variables.CAMPO_ID, Variables.CAMPO_NOMBRE, Variables.CAMPO_DESCRIPCION, Variables.CAMPO_FOTO};
        Cursor cursor = db.query(Variables.TABLA_GALERIA, campos, Variables.CAMPO_ID+"=?", parametros, null, null, null);
        cursor.moveToFirst();
        try {
            TVIdBlog.setText(cursor.getString(0));
            TVTituloDetalle.setText(cursor.getString(1));
            TVDescripcionDetalle.setText(cursor.getString(2));
            byte[] byteArray = cursor.getBlob(3);
            //Transformacion del bytearray a bitmap y posterior drawable para mostrarlo en el Imageview
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
            Drawable d = new BitmapDrawable(getResources(), bm);
            IVFotoDetalle.setImageDrawable(d);
            cursor.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "No se encontro la foto, verifique de nuevo",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.CmdAtras:
                finish();
                break;
            default:
                break;
        }
    }
}