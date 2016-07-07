package com.example.otoni.preguntasdinamicas;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.timroes.axmlrpc.XMLRPCClient;

public class MainActivity extends AppCompatActivity {

    protected ViewGroup layoutGroup;
    protected ScrollView scrollView;
    protected List<Encuesta> lista_preguntas;
    protected ProgressDialog dialog;
    protected int secuencia, posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutGroup = (ViewGroup) findViewById(R.id.contenedor_preguntas);
        scrollView = (ScrollView) findViewById(R.id.scroll_encuesta);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Buscando preguntas");

        WebService hilo = new WebService(dialog, 1, 1);
        Vector params = new Vector();
        hilo.execute(params);
    }

    public void addRigth(View button) {
        addChild();
    }

    public void addLeft(View button) {
        addChild();
    }

    private void addChild() {

        final Encuesta encuesta = lista_preguntas.get(posicion);

        int layout = -1;

        switch (encuesta.getTipo_pregunta()) {
            case 1:
                layout = R.layout.pregunta_botones;
                break;
            case 2:
                layout = R.layout.pregunta_multiple;
                break;
            case 3:
                layout = R.layout.pregunta_multiple_rigth;
                break;
            case 4:
                layout = R.layout.pregunta_boton_rigth;
                break;
            case 5:
                layout = R.layout.pregunta_radio;
                break;
            case 6:
                layout = R.layout.emergente_continuacion;
                break;
            default:
                layout = -1;
                break;
        }

        Log.d("Actividad", encuesta.getPregunta() + " " + encuesta.getTipo_pregunta());
        LayoutInflater inflater = LayoutInflater.from(this);

        if (layout != -1) {
            LinearLayout linearLayout = (LinearLayout) inflater.inflate(layout, null, false);

            TextView textView = (TextView) linearLayout.findViewById(R.id.pregunta_equipo_frio);
            textView.setText(encuesta.getSecuencia() + " - " + encuesta.getPregunta());

            switch (encuesta.getTipo_pregunta()) {
                case 1:
                    if (encuesta.getDescripcion_accion().get(0).equals("SI")) {
                        ImageView si = (ImageView) linearLayout.findViewById(R.id.si_equipo_frio);
                        si.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                secuencia = encuesta.getEjecutar().get(0);
                                buscarPregunta();
                                addChild();
                            }
                        });

                        ImageView no = (ImageView) linearLayout.findViewById(R.id.no_equipo_frio);
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                secuencia = encuesta.getEjecutar().get(1);
                                buscarPregunta();
                                addChild();
                            }
                        });
                    }
                    else {
                        ImageView si = (ImageView) linearLayout.findViewById(R.id.si_equipo_frio);
                        si.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                secuencia = encuesta.getEjecutar().get(1);
                                buscarPregunta();
                                addChild();
                            }
                        });

                        ImageView no = (ImageView) linearLayout.findViewById(R.id.no_equipo_frio);
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                secuencia = encuesta.getEjecutar().get(0);
                                buscarPregunta();
                                addChild();
                            }
                        });
                    }

                    break;

                case 3:
                    Spinner spinner = (Spinner) linearLayout.findViewById(R.id.respuesta_spinner_rigth);
                    spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, encuesta.getDescripcion_accion()));
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            secuencia = encuesta.getEjecutar().get(i);
                            buscarPregunta();
                            addChild();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                case 5:
                    RadioGroup group = (RadioGroup) linearLayout.findViewById(R.id.respuesta_radio);
                    RadioButton button;

                    for (int i = 0; i < encuesta.getDescripcion_accion().size(); i++) {
                        button = new RadioButton(this);
                        button.setText(encuesta.getDescripcion_accion(i));
                        group.addView(button);
                    }

                    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            secuencia = encuesta.getEjecutar().get(i);
                            buscarPregunta();
                            addChild();
                        }
                    });

                    break;

                case 6:
                    final Dialog ventana = new Dialog(MainActivity.this);
                    ventana.setTitle("Aviso");
                    ventana.setContentView(layout);

                    TextView text = (TextView) ventana.findViewById(R.id.pregunta_equipo_frio);
                    text.setText(encuesta.getPregunta());

                    LinearLayout boton = (LinearLayout) ventana.findViewById(R.id.dialog_button_continuar);
                    boton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            secuencia = encuesta.getEjecutar().get(0);
                            buscarPregunta();
                            addChild();
                            ventana.dismiss();
                        }
                    });

                    ventana.show();
                    break;

            }

            if (encuesta.getTipo_pregunta() != 6) {
                layoutGroup.addView(linearLayout);
            }

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Error con los datos", Toast.LENGTH_LONG).show();
        }

    }

    public void buscarPregunta() {
        for (int i = 0; i < lista_preguntas.size(); i++) {
            if (secuencia == lista_preguntas.get(i).getSecuencia()) {
                posicion = i;
                Log.d("Posicion", posicion + "" );
                break;
            }
        }
    }

    private class WebService extends AsyncTask<Vector, Integer, Integer> {

        private ProgressDialog dialog;
        private XMLRPCClient client;
        protected int id_modulo;
        protected int id_rol;

        public WebService(ProgressDialog dialog, int id_modulo, int id_rol) {
            this.dialog = dialog;
            this.id_modulo = id_modulo;
            this.id_rol = id_rol;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Integer doInBackground(Vector... var) {
            try {
                //client = new XMLRPCClient(new URL("http://10.0.2.2:4142"));
                //client = new XMLRPCClient(new URL("http://192.168.1.102:4142"));
                //client = new XMLRPCClient(new URL("http://192.168.0.101:4142"));
                //client = new XMLRPCClient(new URL("http://162.252.58.171:4142"));
                //client = new XMLRPCClient(new URL("http://172.16.0.102:4142"));
                //client = new XMLRPCClient(new URL("http://201.242.192.105:4142"));
                client = new XMLRPCClient(new URL("http://192.168.1.38:4142"));

                Vector paramentros = new Vector();
                paramentros.add(id_rol);
                paramentros.add(id_modulo);

                JSONParser parser = new JSONParser();
                String result = (String) client.call("controlador", "6", paramentros);

                Log.v("MainActivity", result);

                if (result != null) {
                    try {
                        lista_preguntas = new ArrayList<>();

                        Object object = parser.parse(result);
                        JSONObject object1 = (JSONObject) object;

                        JSONArray array = (JSONArray) object1.get("Encuesta");

                        for (int x = 0; x < array.size(); x++) {
                            JSONObject object2 = (JSONObject) array.get(x);
                            Encuesta nueva = new Encuesta();

                            Long id_encuesta = (Long) object2.get("id_encuesta");
                            Long secuencia = (Long) object2.get("secuencia");
                            Long tipo_pregunta = (Long) object2.get("tipo_pregunta");
                            String pregunta = (String) object2.get("pregunta");

                            nueva.setId_encuesta(id_encuesta.intValue());
                            nueva.setPregunta(pregunta);
                            nueva.setTipo_pregunta(tipo_pregunta.intValue());
                            nueva.setSecuencia(secuencia.intValue());

                            JSONArray acciones = (JSONArray) object2.get("Acciones");

                            for (int y = 0; y < acciones.size(); y++) {
                                JSONObject accion = (JSONObject) acciones.get(y);

                                Long ejecutar = (Long) accion.get("ejecutar");
                                String descripcion_accion = (String) accion.get("descripcion");
                                nueva.setEjecutar(ejecutar.intValue());
                                nueva.setDescripcion_accion(descripcion_accion);
                            }

                            lista_preguntas.add(nueva);

                            if (nueva.getSecuencia() == 1) {
                                posicion = lista_preguntas.size() - 1;
                            }
                        }
                    }
                    catch (ParseException exc) {
                        Log.v("MainActivity", exc.toString());
                        //Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_SHORT).show();
                        return 1;
                    }
                }

                return 0;
            }
            catch (Exception exc) {
                Log.v("MainActivity", exc.toString());
                //Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_SHORT).show();
                return 2;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progreso) {
            dialog.setProgress(progreso[0]);
        }

        @Override
        protected void onPostExecute(Integer num) {
            dialog.dismiss();

            if (num == 0) {
                addChild();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
