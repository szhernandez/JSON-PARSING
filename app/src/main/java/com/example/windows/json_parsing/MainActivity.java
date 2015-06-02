package com.example.windows.json_parsing;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity  {

    private ProgressDialog pDialog;

    // URL para obtener materias del JSON
    private static String url = "http://app.szhernandez.dx.am/materias2.json";
    // Nombres de los nodos del JSON
    private static final String TAG_MATERIAS = "materias";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "nombre";
    private static final String TAG_PROFESOR = "profesor";
    // Arreglo Json para materias
    JSONArray Amaterias = null;
    // Hashmap para el ListView
    ArrayList<HashMap<String, String>> materiasList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materiasList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Clic listenr al elemento
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // obteniendo valores del elemento seleccionado
                String idmateria = ((TextView) view.findViewById(R.id.id)).getText().toString();
                String nombre = ((TextView) view.findViewById(R.id.nombre)).getText().toString();
                String profesor = ((TextView) view.findViewById(R.id.profesor)).getText().toString();

                // Comenzando la actividad indiviidual
                Intent in = new Intent(getApplicationContext(), mostrar_materia.class);
                in.putExtra(TAG_ID, idmateria);
                in.putExtra(TAG_NAME, nombre);
                in.putExtra(TAG_PROFESOR, profesor);
                startActivity(in);
            }
        });
        // Calling async task to get json
        new GetMaterias().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetMaterias extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Mostrar ProgressDialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.progress));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creando un instancia de la clase ServiceHandler
            ServiceHandler sh = new ServiceHandler();
            //  Realizando una peticion a la URL del JSON
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //Obteniendo nodos del arrego JSON
                    Amaterias = jsonObj.getJSONArray(TAG_MATERIAS);
                    // Ciclo para obtener todas las materias
                    for (int i = 0; i < Amaterias.length(); i++) {
                        JSONObject c = Amaterias.getJSONObject(i);
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String profesor = c.getString(TAG_PROFESOR);
                        //  hashmap temporar para una materia
                        HashMap<String, String> contact = new HashMap<String, String>();
                        // Agregando nodos hijos, HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_PROFESOR, profesor);
                        // Agregando la materia a la lista de materias
                        materiasList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //En caso de haber problemas para conectar y obtener la URL
                Log.e("ServiceHandler", "No se pueden encontrar ningunos datos en la url seleccionada");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, materiasList,
                    R.layout.list_item, new String[] { TAG_ID, TAG_NAME, TAG_PROFESOR
                  }, new int[] { R.id.id,
                    R.id.nombre,R.id.profesor });
            setListAdapter(adapter);
        }
    }
}
//Modificado SZHG