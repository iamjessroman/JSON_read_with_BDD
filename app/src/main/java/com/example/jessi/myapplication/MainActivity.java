package com.example.jessi.myapplication;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQL bdd;
    private SQLiteDatabase database;
    private Cursor cursor;
    private ListView lv;
    private SimpleCursorAdapter adapter;
    ListView problemsList;
    String url = "http://problemonute.herokuapp.com/api/problems";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        problemsList = findViewById(R.id.problemsList);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando....");
        dialog.show();

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Some error occurred!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }

    void parseJsonData(String jsonString) {
        try {
            bdd = new SQL(this);
            database = bdd.getWritableDatabase();

            JSONArray problemsArray = new JSONArray (jsonString);
            ArrayList al = new ArrayList();
            for(int i = 0; i < problemsArray.length(); ++i) {
                String sql = String.format("insert into problemas values('%s', '%s','%s', '%s','%s', '%s','%s', '%s','%s')",
                        problemsArray.getJSONObject(i).getInt("id"),
                        problemsArray.getJSONObject(i).getString("respuesta_erronea3"),
                        problemsArray.getJSONObject(i).getString("respuesta_erronea2"),
                        problemsArray.getJSONObject(i).getString("respuesta_erronea1"),
                        problemsArray.getJSONObject(i).getString("respuesta_correcta"),
                        problemsArray.getJSONObject(i).getString("lon"),
                        problemsArray.getJSONObject(i).getString("lat"),
                        problemsArray.getJSONObject(i).getString("enunciado"),
                        problemsArray.getJSONObject(i).getInt("category_id")
                        );
                        database.execSQL(sql);

                al.add(problemsArray.getJSONObject(i).getInt("id"));
                al.add(problemsArray.getJSONObject(i).getString("respuesta_erronea3"));
                al.add(problemsArray.getJSONObject(i).getString("respuesta_erronea2"));
                al.add(problemsArray.getJSONObject(i).getString("respuesta_erronea1"));
                al.add(problemsArray.getJSONObject(i).getString("respuesta_correcta"));
                al.add(problemsArray.getJSONObject(i).getString("lon"));
                al.add(problemsArray.getJSONObject(i).getString("lat"));
                al.add(problemsArray.getJSONObject(i).getString("enunciado"));
                al.add(problemsArray.getJSONObject(i).getInt("category_id"));
            }

            Cursor cursor = database.rawQuery("select * from problemas", null);
            int filas = cursor.getCount();

            for(int i = 0; i < filas; i++) {

                cursor.moveToPosition(i);

                Log.d("CURSOR", "FILA: " + i);
                Log.d("CURSOR", Integer.toString(cursor.getInt(0)));
                Log.d("CURSOR", cursor.getString(1));
                Log.d("CURSOR", cursor.getString(2));
                Log.d("CURSOR", cursor.getString(3));
                Log.d("CURSOR", cursor.getString(4));
                Log.d("CURSOR", cursor.getString(5));
                Log.d("CURSOR", cursor.getString(6));
                Log.d("CURSOR", cursor.getString(7));
                Log.d("CURSOR", Integer.toString(cursor.getInt(8)));
            }

            cursor.close();
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
            problemsList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }
}