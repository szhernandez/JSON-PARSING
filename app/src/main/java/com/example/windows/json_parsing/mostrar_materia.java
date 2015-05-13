package com.example.windows.json_parsing;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class mostrar_materia extends ActionBarActivity {

    // JSON node keys
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "nombre";
    private static final String TAG_PROFESOR = "profesor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_materia);
        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String id = in.getStringExtra(TAG_ID);
        String nombre = in.getStringExtra(TAG_NAME);
        String profesor = in.getStringExtra(TAG_PROFESOR);


        // Displaying all values on the screen
        TextView lblid = (TextView) findViewById(R.id.txtid);
        TextView lblnombre = (TextView) findViewById(R.id.txtonmbre);
        TextView lblprofesor = (TextView) findViewById(R.id.txtprofesor);


        lblid.setText(id);
        lblnombre.setText(nombre);
        lblprofesor.setText(profesor);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mostrar_materia, menu);
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
}
