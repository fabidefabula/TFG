package com.example.usuario.ludiuca.fragmentos;



import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.usuario.ludiuca.PrincipalActivity;
import com.example.usuario.ludiuca.R;
import com.example.usuario.ludiuca.clases.Actitud;
import com.example.usuario.ludiuca.clases.Alumno;
import com.example.usuario.ludiuca.clases.Avatar;
import com.example.usuario.ludiuca.clases.Clase;
import com.example.usuario.ludiuca.clases.Fecha;
import com.example.usuario.ludiuca.clases.Grupo;
import com.example.usuario.ludiuca.clases.Medalla;
import com.example.usuario.ludiuca.clases.Notificacion;
import com.example.usuario.ludiuca.clases.Privilegio;
import com.example.usuario.ludiuca.clases.Profesor;
import com.example.usuario.ludiuca.clases.DatosUsuario;
import com.example.usuario.ludiuca.clases.Tarea;
import com.example.usuario.ludiuca.clases.Webservice;

import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Usuario on 17/02/2016.
 */
public class FragmentoLogin extends Fragment {
    View rootView;
    String username, password;
    Profesor profesor;
    ArrayList<Alumno> alumnos;
    ArrayList<Medalla> listaMedallasAlumnos= new ArrayList<>();
    ArrayList<Medalla> listaMedallasProfesor= new ArrayList<>();
    ArrayList<Avatar> listaAvataresAlumnos = new ArrayList<>();
    ArrayList<Avatar> listaAvataresProfesor= new ArrayList<>();
    ArrayList<Avatar> listaAvataresGrupos= new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_main, container, false);
        Button botonLogin = (Button) rootView.findViewById(R.id.botonLogin);
        botonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // lo que haga cuando pulse
                EditText cUser = (EditText) rootView.findViewById(R.id.editUser);
                EditText cPass = (EditText) rootView.findViewById(R.id.editPass);
                (new RealizarLogin(cUser.getText().toString(), cPass.getText().toString())).execute();


            }
        });

        return rootView;
    }


    class RealizarLogin extends AsyncTask<Void, Void, Void> {
        private String user, pass, response;
        private HashMap<String, String> requestBody;

        public RealizarLogin(String u, String p) {
            user = u;
            pass = p;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            requestBody = new HashMap<>();
            requestBody.put("username", user);
            String password = md5(pass);
            requestBody.put("password", password);
            requestBody.put("operacion", "login");


            Iterator it = requestBody.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry)it.next();
                System.out.println(e.getKey() + " " + e.getValue());
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = Webservice.getInstancia().operacionPost(requestBody);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayList<Clase> clasesProfe = new ArrayList<>();
            Clase clase1;

            if (response != null) {
                System.out.println(response);
                try {
                    JSONObject json = new JSONObject(response);
                    boolean exito = json.getBoolean("Exito");
                    String mensaje = json.getString("Mensaje");
                    if (exito) {
                        JSONObject respuesta = json.getJSONObject("Respuesta");
                        profesor = new Profesor(respuesta.getString("Name"), respuesta.getString("Surname"));
                        //Obtengo las actitudes
                        JSONArray actitudes = respuesta.getJSONArray("Actitudes");
                        ArrayList<Actitud> actitudesBuenas = new ArrayList<>();
                        ArrayList<Actitud> actitudesMalas = new ArrayList<>();

                        for(int i=0; i< actitudes.length(); i++){
                            JSONObject actitudJson = actitudes.getJSONObject(i);
                            Actitud actitud = new Actitud(actitudJson.getString("Name"), actitudJson.getInt("Experience"));
                            actitud.setTipo(actitudJson.getInt("Type"));
                            if(actitud.getTipo()==1){
                                actitudesBuenas.add(actitud);
                            }
                            else{
                                actitudesMalas.add(actitud);
                            }
                        }
                        DatosUsuario.getInstance().setActitudesBuenas(actitudesBuenas);
                        DatosUsuario.getInstance().setActitudesMalas(actitudesMalas);

                        JSONArray clases = respuesta.getJSONArray("Clases");
                        //Obtengo la lista completa de medallas disponibles para alumnos
                        JSONArray listaMedallasJSONalumnos = respuesta.getJSONArray("Medallas Alumnos");
                        for(int t=0 ; t < listaMedallasJSONalumnos.length(); t++){
                            JSONObject medallaJSON = listaMedallasJSONalumnos.getJSONObject(t);
                            Medalla medalla1 = new Medalla(medallaJSON.getInt("idMedallaAlumno"), medallaJSON.getString("Name"), medallaJSON.getString("Photo"));
                            medalla1.setDescripcion(medallaJSON.getString("Description"));
                            listaMedallasAlumnos.add(medalla1);
                        }
                        DatosUsuario.getInstance().setListaMedallasAlumnos(listaMedallasAlumnos);

                        //Obtengo los avatares para los grupos de alumnos
                        JSONArray listaAvataresGrupoJSON = respuesta.getJSONArray("Avatares Grupos");
                        for(int t=0 ; t < listaAvataresGrupoJSON.length(); t++){
                            JSONObject avatarJSON = listaAvataresGrupoJSON.getJSONObject(t);
                            Avatar avatar = new Avatar(avatarJSON.getInt("idAvatar"), avatarJSON.getString("Photo"));
                            listaAvataresGrupos.add(avatar);
                        }
                        DatosUsuario.getInstance().setAvataresGrupos(listaAvataresGrupos);

                        //Obtengo los avatares para los alumnos
                        JSONArray listaAvataresAlumnosJSON = respuesta.getJSONArray("Avatares Alumno");
                        for(int t=0 ; t < listaAvataresAlumnosJSON.length(); t++){
                            JSONObject avatarJSON = listaAvataresAlumnosJSON.getJSONObject(t);
                            Avatar avatar = new Avatar(avatarJSON.getInt("idAvatar"), avatarJSON.getString("Photo"));
                            listaAvataresAlumnos.add(avatar);
                        }
                        DatosUsuario.getInstance().setAvataresAlumnos(listaAvataresAlumnos);


                        //Obtengo los avatares para los profesores
                        JSONArray listaAvataresProfesorJSON = respuesta.getJSONArray("Avatares Profesor");
                        for(int t=0 ; t < listaAvataresProfesorJSON.length(); t++){
                            JSONObject avatarJSON = listaAvataresProfesorJSON.getJSONObject(t);
                            Avatar avatar = new Avatar(avatarJSON.getInt("idAvatar"), avatarJSON.getString("Photo"));
                            listaAvataresProfesor.add(avatar);
                        }

                        DatosUsuario.getInstance().setAvataresProfe(listaAvataresProfesor);
                        //Obtenemos los privilegios disponibles de la clase
                        ArrayList<Privilegio> privilegiosClase = new ArrayList<>();
                        JSONArray privilegios= respuesta.getJSONArray("Privilegios");
                        for(int r=0; r < privilegios.length(); r++){
                            JSONObject privilegioJson = privilegios.getJSONObject(r);
                            Privilegio privilegio = new Privilegio(privilegioJson.getString("Description"), privilegioJson.getString("Name"), privilegioJson.getInt("idPrivilegio"));
                            privilegiosClase.add(privilegio);
                        }
                        DatosUsuario.getInstance().setPrivilegios(privilegiosClase);


                        //Obtengo la lista completa de medallas disponibles para alumnos
                        JSONArray listaMedallasJSONprofesor = respuesta.getJSONArray("Medallas Profesor");
                        for(int t=0 ; t < listaMedallasJSONprofesor.length(); t++){
                            JSONObject medallaJSON = listaMedallasJSONprofesor.getJSONObject(t);
                            Medalla medalla1 = new Medalla(medallaJSON.getInt("idMedallaProfesor"), medallaJSON.getString("Name"), medallaJSON.getString("Photo"));
                            medalla1.setDescripcion(medallaJSON.getString("Description"));
                            listaMedallasProfesor.add(medalla1);
                        }
                        DatosUsuario.getInstance().setListaMedallasProfesor(listaMedallasProfesor);

                        //Obtengo las medallas del profesor
                        JSONArray medallasProfe = respuesta.getJSONArray("Medallas");
                        ArrayList<Medalla> medallasProfeArray = new ArrayList<>();
                        for (int i = 0; i < medallasProfe.length(); i++) {
                            JSONObject medalla = medallasProfe.getJSONObject(i);
                            Medalla medallaProfe = new Medalla(medalla.getInt("idMedallaProfesor"), medalla.getString("Name"), medalla.getString("Photo"));
                            medallaProfe.setDescripcion(medalla.getString("Description"));
                            medallasProfeArray.add(medallaProfe);
                        }
                        profesor.setMedallasProfe(medallasProfeArray);

                        //Obtengo las clases del profesor
                        for (int i = 0; i < clases.length(); i++) {
                            JSONObject clase = clases.getJSONObject(i);
                            ArrayList<Tarea> tareasClase = new ArrayList<>();
                            ArrayList<Alumno> alumnosClase = new ArrayList<>();
                            clase1 = new Clase(clase.getString("SubjectName"), clase.getString("CourseName"), clase.getString("SubjectPicture"), clase.getString("Group"));
                            clase1.setIdAsignatura(clase.getInt("idAsignatura"));
                            clase1.setIdClase(clase.getInt("idClase"));
                            clase1.setIdCurso(clase.getInt("idCurso"));
                            clase1.setLetra(clase.getString("Group"));
                            JSONArray tareas = clase.getJSONArray("Tareas");
                            clase1.setTareasClase(tareasClase);
                            JSONArray alumnos = clase.getJSONArray("Alumnos");
                            clase1.setAlumnosClase(alumnosClase);
                            //De cada clase tomamos las tareas puestas por el profesor
                            for(int r=0; r < tareas.length(); r++){
                                JSONObject tarea = tareas.getJSONObject(r);
                                Tarea tarea2;
                                tarea2 = new Tarea(tarea.getString("Description"), tarea.getString("Createdate"), tarea.getString("Finishdate"));
                                tarea2.setIdTarea(tarea.getInt("idTarea"));
                                tareasClase.add(tarea2);
                            }


                            //Tomamos las notificaciones de cada clase
                            HashMap<Fecha, Notificacion> notificacionesHash = new HashMap<>();

                            JSONArray notificaciones = clase.getJSONArray("Notificaciones");
                            ArrayList<Notificacion> notificacionArray = new ArrayList<>();
                            clase1.setNotificacionesClase(notificacionesHash);
                            for(int q = 0; q < notificaciones.length(); q++){
                                JSONObject notificacion = notificaciones.getJSONObject(q);
                                Notificacion notificacion1 = new Notificacion(notificacion.getString("Description"), notificacion.getString("Date"), notificacion.getInt("idNotification"));
                                notificacion1.setEmoji(notificacion.getString("Emoji"));
                                notificacionesHash.put(notificacion1.getFecha(), notificacion1);
                                notificacionArray.add(notificacion1);
                            }
                            clase1.setNotificacionArray(notificacionArray);
                            clase1.setNotificacionesClase(notificacionesHash);
                            //Tomamos los alumnos
                           HashMap<String,Alumno> hmClase = clase1.getHashAlumnoId();
                            for (int j = 0; j < alumnos.length(); j++) {
                                JSONObject alumno = alumnos.getJSONObject(j);
                                Alumno alumno2;
                                alumno2 = new Alumno(alumno.getString("Name"), alumno.getString("Surname"));
                                clase1.getAlumnosClase().add(j, alumno2);
                                alumno2.setFotoPerfil(alumno.getString("Photo"));
                                alumno2.setNickName(alumno.getString("Nickname"));
                                alumno2.setLevel(alumno.getInt("Level"));
                                alumno2.setExp(alumno.getInt("Experience"));
                                alumno2.setMonedas(alumno.getInt("Coins"));
                                alumno2.setIdAlumno(alumno.getInt("idAlumno"));
                                hmClase.put(alumno.getString("idAlumno"), alumno2);

                                JSONArray medallas = alumno.getJSONArray("Medallas");
                                ArrayList<Medalla> arrayMedallas = new ArrayList<>();
                                //De cada alumno tomamos las medallas que tiene
                                for (int k = 0; k < medallas.length(); k++) {
                                    JSONObject medallaJson = medallas.getJSONObject(k);
                                    Medalla medallica = new Medalla(medallaJson.getInt("idMedallaAlumno"), medallaJson.getString("Name"), medallaJson.getString("Photo"));
                                    medallica.setIdAsignatura(medallaJson.getString("idAsignatura"));
                                    medallica.setDescripcion(medallaJson.getString("Description"));
                                    arrayMedallas.add(medallica);
                                }
                                alumno2.setMedallasAlumno(arrayMedallas);

                                //De cada alumno tomamos los privilegios que tiene
                                JSONArray privilegiosAlumno = alumno.getJSONArray("Privilegios");
                                ArrayList<Privilegio> arrayPrivilegios = new ArrayList<>();
                                for (int m = 0; m < privilegiosAlumno.length(); m++) {
                                    JSONObject privilegioJson = privilegiosAlumno.getJSONObject(m);
                                    Privilegio privilegio = new Privilegio(privilegioJson.getString("Description"), privilegioJson.getString("Name"), privilegioJson.getInt("idPrivilegio"));
                                    arrayPrivilegios.add(privilegio);
                                }
                                alumno2.setPrivilegiosAlumno(arrayPrivilegios);
                            }
                            //De cada clase tomamos los grupos de alumnos
                            ArrayList<Grupo> gruposClase = new ArrayList<>();
                            JSONArray gruposJson = clase.getJSONArray("Grupos");

                            for(int w = 0; w<gruposJson.length(); w++) {
                                JSONObject grupoJson = gruposJson.getJSONObject(w);
                                Grupo grupo1 = new Grupo(clase1, grupoJson.getString("Name"), grupoJson.getString("Photo"));
                                grupo1.setIdGrupo(grupoJson.getInt("idGrupo"));
                                ArrayList<Alumno> alumnosGrupo = new ArrayList<>();

                                JSONArray alumnosGrupoJson = grupoJson.getJSONArray("Alumnos");

                                for(int a=0; a<alumnosGrupoJson.length(); a++){
                                    JSONObject alumnoJson = alumnosGrupoJson.getJSONObject(a);
                                    alumnosGrupo.add(hmClase.get(alumnoJson.getString("idAlumno")));
                                }

                                grupo1.setAlumnosGrupo(alumnosGrupo);
                                gruposClase.add(grupo1);
                                clase1.setGruposClase(gruposClase);

                            }
                            clasesProfe.add(clase1);
                        }
                        //Tomamos los datos del profesor para llenar el objeto
                        profesor.setClasesProfe(clasesProfe);
                        profesor.setNickName(respuesta.getString("Nickname"));
                        profesor.setExp(respuesta.getInt("Experience"));
                        profesor.setLevel(respuesta.getInt("Level"));
                        profesor.setFotoPerfil(respuesta.getString("Photo"));
                        profesor.setIdProfesor(respuesta.getString("idProfesor"));


                        //Change the string value and launch intent to ActivityB
                        DatosUsuario.getInstance().setProfesor(profesor);
                        Intent intent = new Intent(getContext(), FragmentoPerfil.class);
                        Intent intent2 = new Intent(getContext(), FragmentoClases.class);
                        Intent intent3 = new Intent(getContext(), FragmentoClase.class);
                        getActivity().startActivity(new Intent(rootView.getContext(), PrincipalActivity.class));


                    } else {
                        Toast.makeText(rootView.getContext(), mensaje, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }
    }
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
