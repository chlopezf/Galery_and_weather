package com.example.tges.EstadoTiempo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tges.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class Estado_Tiempo extends AppCompatActivity implements View.OnClickListener {
    EditText ETCiudad, ETPais;
    TextView TVClima, TVTemp;
    Button CmdObtener;
    ImageButton CmdAtras;
    ImageView IVClima;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "ff48bcdd98f8be983fef55f2a85c3ffd";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_tiempo);
        //Imageview
        IVClima=findViewById(R.id.IVClima);
        //Edittext
        ETCiudad=findViewById(R.id.ETCiudad);
        ETPais=findViewById(R.id.ETPais);
        //Textview
        TVClima=findViewById(R.id.TVClima);
        TVTemp=findViewById(R.id.TVTemp);
        //Button
        CmdObtener=findViewById(R.id.CmdObtener);
        //Imagebutton
        CmdAtras=findViewById(R.id.CmdAtras);
        //clicklistener
        CmdObtener.setOnClickListener(this);
        CmdAtras.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CmdObtener:
                String tempurl = "";
                String Ciudad = ETCiudad.getText().toString().trim();
                String Pais = ETPais.getText().toString().trim();
                if(Ciudad.isEmpty()){
                    Toast.makeText(Estado_Tiempo.this, "El campo ciudad es obligatorio",Toast.LENGTH_SHORT).show();
                }else{
                    if(!ETPais.getText().toString().trim().isEmpty()){
                        tempurl = url + "?q=" + Ciudad + "," + Pais + "&appid=" + appid;
                    }else {
                        tempurl = url + "?q=" + Ciudad + "&appid=" + appid;
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response: ", response);
                            String output = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                                String descripcion = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");
                                JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                                double temp = jsonObjectMain.getDouble("temp") - 273.15;
                                double sensacion = jsonObjectMain.getDouble("feels_like") - 273.15;
                                float presion = jsonObjectMain.getInt("pressure");
                                int humedad = jsonObjectMain.getInt("humidity");
                                JSONObject jsonObjectViento = jsonObject.getJSONObject("wind");
                                String viento = jsonObjectViento.getString("speed");
                                JSONObject jsonObjectNubes = jsonObject.getJSONObject("clouds");
                                String nubes = jsonObjectNubes.getString("all");
                                JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                                String NombrePais = jsonObjectSys.getString("country");
                                String NombreCiudad = jsonObject.getString("name");
                                output += " " + NombreCiudad + "(" + NombrePais + ")"
                                        //+ "\n Temperatura: " + df.format(temp) + " °C"
                                        + "\n Sensacion térmica: " + df.format(sensacion) + " °C"
                                        + "\n Humedad: " + humedad + "%"
                                        //+ "\n Descripcion :" + descripcion
                                        + "\n Velocidad del viento: " + viento +"m/s"
                                        + "\n Nubes: " + nubes + "%"
                                        + "\n Presión: " + presion + "hPa";
                                TVClima.setText(output);
                                TVTemp.setText(df.format(temp) + "°C");
                                //icono clima
                                {
                                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                                    if (SDK_INT > 8)
                                    {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                        Bitmap obtener_imagen = get_imagen("http://openweathermap.org/img/wn/" + icon + "@4x.png");
                                        IVClima.setImageBitmap(obtener_imagen);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(Estado_Tiempo.this, error.toString().trim(),Toast.LENGTH_SHORT).show(); //Obtiene el error que se produce
                            Toast.makeText(Estado_Tiempo.this, "Verifique que este bien escrito el nombre de la ciudad",Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
                break;
            case R.id.CmdAtras:
                finish();
                break;
            default:
                break;
        }
    }

    private Bitmap get_imagen(String url) {
        Bitmap bm = null;
        try {
            URL _url = new URL(url);
            URLConnection con = _url.openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Toast.makeText(Estado_Tiempo.this, "Ocurrió un error al obtener la imagen",Toast.LENGTH_SHORT).show();
        }
        return bm;
    }
}