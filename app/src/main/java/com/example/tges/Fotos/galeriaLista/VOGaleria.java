package com.example.tges.Fotos.galeriaLista;

public class VOGaleria {
    private String Id;
    private String Nombre;
    private String Descripcion;
    private byte[] Foto;


    public VOGaleria(String Id, String Nombre, String Descripcion, byte[] Foto) {
        this.Id = Id;
        this.Nombre = Nombre;
        this.Descripcion = Descripcion;
        this.Foto = Foto;
    }

    public VOGaleria(){
    }

    public String getId() { return Id; }

    public String getNombre() { return Nombre; }

    public String getDescripcion() { return Descripcion; }

    public byte[] getFoto() { return Foto; }

    public void setId(String id) {
        Id = id;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void setFoto(byte[] foto) {
        Foto = foto;
    }
}