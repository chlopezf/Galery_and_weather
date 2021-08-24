package com.example.tges.Utilidades;

public class Variables {
    public static String Id_Foto; //Obtiene el id de la foto
    //Bases de datos
    public static final String DB_GALERIA="bd_galeria";
    //Tablas
    public static final String TABLA_GALERIA="T_Galeria";
    //Campos
    public static final String CAMPO_ID="Id";
    public static final String CAMPO_NOMBRE="Nombre";
    public static final String CAMPO_DESCRIPCION="Descripcion";
    public static final String CAMPO_FOTO="Foto";
    //Sentencias
    public static final String CREA_TABLA_GALERIA = "CREATE TABLE " + TABLA_GALERIA + "("
            + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_NOMBRE+" TEXT, "+CAMPO_DESCRIPCION+
            " TEXT, "+CAMPO_FOTO+" BLOB)";
}
