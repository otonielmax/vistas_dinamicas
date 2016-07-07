package com.example.otoni.preguntasdinamicas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by otoni on 29/6/2016.
 */
public class Encuesta implements Serializable {

    protected int id_encuesta;
    protected String pregunta;
    protected int tipo_pregunta;
    protected int secuencia;
    protected List<String> descripcion_accion;
    protected List<Integer> ejecutar;

    public Encuesta() {
        descripcion_accion = new ArrayList<>();
        ejecutar = new ArrayList<>();
    }

    public int getId_encuesta() {
        return id_encuesta;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public List<Integer> getEjecutar() {
        return ejecutar;
    }

    public Integer getEjecutar(int posicion) {
        return ejecutar.get(posicion);
    }

    public int getTipo_pregunta() {
        return tipo_pregunta;
    }

    public List<String> getDescripcion_accion() {
        return descripcion_accion;
    }

    public String getDescripcion_accion(int posicion) {
        return descripcion_accion.get(posicion);
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setDescripcion_accion(String descripcion_accion) {
        this.descripcion_accion.add(descripcion_accion);
    }

    public void setEjecutar(int ejecutar) {
        this.ejecutar.add(ejecutar);
    }

    public void setId_encuesta(int id_encuesta) {
        this.id_encuesta = id_encuesta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public void setTipo_pregunta(int tipo_pregunta) {
        this.tipo_pregunta = tipo_pregunta;
    }

}
