package com.example.usuario.ludiuca.clases;

import android.app.Notification;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Usuario on 17/02/2016.
 */
public class Clase {
    private String asignatura;
    private String curso, letra;
    private String imagenCurso;
    private ArrayList<Alumno> alumnosClase;
    private  ArrayList<Tarea> tareasClase= new ArrayList<>();
    private ArrayList<Actitud> actitudesClase= new ArrayList<>();
    private  ArrayList<Grupo> gruposClase = new ArrayList<>();
    private HashMap<String, Alumno> alumnoId= new HashMap<String, Alumno>();
    private HashMap<Fecha, Notificacion> notificacionesClase;
    private ArrayList<Notificacion> notificacionArray = new ArrayList<>();
    private  ArrayList<Notificacion> buscada;
    private int idClase, idAsignatura, idCurso;


    public Clase(String asignatura,  String curso, String imagenCurso, String letra) {
        this.asignatura = asignatura;
        this.letra = letra;
        this.curso = curso;
        this.imagenCurso = imagenCurso;
    }

    public String getLetra() {
        return letra;
    }

    public ArrayList<Actitud> getActitudesClase() {
        return actitudesClase;
    }

    public void setActitudesClase(ArrayList<Actitud> actitudesClase) {
        this.actitudesClase = actitudesClase;
    }

    public void setIdClase(int idClase) {this.idClase = idClase;}
    public void setIdAsignatura(int idAsignatura) {this.idAsignatura = idAsignatura;}
    public void setIdCurso(int idCurso) {this.idCurso = idCurso;}
    public int getIdClase() {return idClase;}
    public int getIdAsignatura() {return idAsignatura;}
    public int getIdCurso() {return idCurso;}
    public String getImagenCurso() {return imagenCurso;}
    public void setNotificacionesClase(HashMap<Fecha, Notificacion> notificacionesClase) {this.notificacionesClase = notificacionesClase;}
    public String getAsignatura() {return asignatura;}
    public void setAlumnosClase(ArrayList<Alumno> alumnosClase) {this.alumnosClase = alumnosClase;}
    public void setAsignatura(String asignatura) {this.asignatura = asignatura;}
    public void setLetra(String letra) {this.letra = letra;}
    public String getCurso() {return curso;}
    public ArrayList<Grupo> getGruposClase() {return gruposClase;}
    public Alumno getAlumnoId(String idAlumno){
        return this.alumnoId.get(idAlumno);
    }
    public HashMap<String, Alumno> getHashAlumnoId() {
        return alumnoId;
    }
    public void setGruposClase(ArrayList<Grupo> gruposClase) {
        this.gruposClase = gruposClase;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }
    public ArrayList<Alumno> getAlumnosClase(){return alumnosClase;}
    public ArrayList<Tarea> getTareasClase() {return tareasClase;}
    public void setTareasClase(ArrayList<Tarea> tareasClase){this.tareasClase = tareasClase;}
    @Override
    public int hashCode() {
        int hash  = 1;
        return  hash * this.alumnoId.size();
    }


    public void setAlumnoId(Alumno alumno, String idAlumno){this.alumnoId.put(idAlumno,alumno);}
    public void setNotificacionArray(ArrayList<Notificacion> notificacionArray) {this.notificacionArray = notificacionArray;}
    public HashMap<Fecha, Notificacion> getNotificacionesClase() {return notificacionesClase;}
    public ArrayList<Notificacion> getNotificacionArray() {return notificacionArray;}
    public ArrayList<Notificacion> getArrayNotificacionesByDate() {
        return buscada;
    }


    public void getNotificationsByDate(Fecha fecha){
        int j=0;
        buscada = new ArrayList<>();
       for(int i=0; i< notificacionArray.size(); i++){
//            System.out.println(notificacionArray.get(i).getFecha().getFechaString());
//            System.out.println(fecha.getFechaString());
            if(fecha.getFechaString().equalsIgnoreCase(notificacionArray.get(i).getFecha().getFechaString())){
                buscada.add(j,notificacionArray.get(i));
                j++;
            }
        }
    }

}

