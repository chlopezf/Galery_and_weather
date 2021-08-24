package com.example.tges.Fotos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tges.R;
import com.example.tges.SQLite.Conexion;
import com.example.tges.Utilidades.Variables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class subir_foto extends AppCompatActivity implements View.OnClickListener {
    Button CmdAtras, CmdTomar, CmdSubir;
    ImageView IVImagen;
    EditText ETNombreArchivo, ETDescripcion;
    //Elementos foto inicio
    String currentPhotoPath;
    private Bitmap bitmap;
    Uri photoURI;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private  static final int REQUEST_PERMISSION_CAMERA=100;
    //Elementos foto fin
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_foto);
        //Buttons
        CmdAtras=findViewById(R.id.CmdAtras);
        CmdTomar=findViewById(R.id.CmdTomar);
        CmdSubir=findViewById(R.id.CmdSubir);
        //Imageviews
        IVImagen=findViewById(R.id.IVImagen);
        //Edittexts
        ETNombreArchivo=findViewById(R.id.ETNombreArchivo);
        ETDescripcion=findViewById(R.id.ETDescripcion);
        //Onclicklistener
        CmdAtras.setOnClickListener(this);
        CmdTomar.setOnClickListener(this);
        CmdSubir.setOnClickListener(this);
        //permisos
        checkPermissions();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CmdTomar:
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(subir_foto.this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                        tomarFoto();
                    }else{
                        ActivityCompat.requestPermissions(subir_foto.this, new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA);
                    }
                }else {
                    tomarFoto();
                }
                break;
            case R.id.CmdSubir:
                SubirFoto();
                break;
            case R.id.CmdAtras:
                finish();
                break;
            default:
                break;
        }
    }

    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Crea el archivo donde debe ir la foto
            File photoFile = null;
            try {
                photoFile = creaArchivoImagen();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.tges.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File creaArchivoImagen() throws IOException {
        //Crea el archivo de la foto
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefijo */
                ".jpg",         /* sufijo */
                storageDir      /* directorio */
        );
        // guarda el archivo
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        //cambia el tamaño de la foto
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("Error", "Permisos no otorgados a la camara");
            ActivityCompat.requestPermissions(subir_foto.this, new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri filePath=null;

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            IVImagen.setImageBitmap(bitmap);
        }

    }

    private void SubirFoto() {
        //establece la conexion db
        Conexion conn=new Conexion(this, Variables.DB_GALERIA,null,1);
        //Abrir db
        SQLiteDatabase db = conn.getWritableDatabase();
        //preparacion de los datos a enviar
        ContentValues values = new ContentValues();
        values.put(Variables.CAMPO_ID, (Integer) null);
        values.put(Variables.CAMPO_NOMBRE, ETNombreArchivo.getText().toString());
        values.put(Variables.CAMPO_DESCRIPCION, ETDescripcion.getText().toString());
        //convierte la imagen a byte para almacenamiento en la db (ImageViewToByte(IVImagen))
        values.put(Variables.CAMPO_FOTO, ImageViewToByte(IVImagen));
        //envio de los datos
        Long idResult = db.insert(Variables.TABLA_GALERIA, Variables.CAMPO_ID, values);
        Toast.makeText(getApplicationContext(), "Imagen " + idResult + " almacenada correctamente", Toast.LENGTH_SHORT).show();
        db.close();
        finish();

    }

    public static byte[] ImageViewToByte(ImageView image) {
        //Convierte la imagen a byte para almacenamiento en la DB
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        bitmap = getResizedBitmap(bitmap, 1000);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}