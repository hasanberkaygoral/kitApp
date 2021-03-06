package com.example.mstfa.kitapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Kitaplar extends AppCompatActivity {
    TextView txtKitapAdi;
    TextView txtSayfaSayisi;
    TextView txtBaski;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitaplar);

        txtKitapAdi = (TextView) findViewById(R.id.txtKitapAdi);
        txtSayfaSayisi = (TextView) findViewById(R.id.txtSayfaSayisi);
        txtBaski = (TextView) findViewById(R.id.txtBaski);
        StringRequest jsonForGetRequest = new StringRequest(
                Request.Method.GET,"http://192.168.1.102:80/kitapp/kitap.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject jsonBody = obj.getJSONObject("bilgi1");
                            txtKitapAdi.setText(jsonBody.getString("kitapAdi"));
                            txtSayfaSayisi.setText(jsonBody.getString("kitapSayfaSayisi"));
                            txtBaski.setText(jsonBody.getString("kitapBaski"));
                            Toast.makeText(Kitaplar.this, "onResponse", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    JSONObject jsonObject = null;
                    String errorMessage = null;

                    switch(response.statusCode){
                        case 400:
                            errorMessage = new String(response.data);

                            try {

                                jsonObject = new JSONObject(errorMessage);
                                String serverResponseMessage =  (String)jsonObject.get("hataMesaj");
                                Toast.makeText(getApplicationContext(),""+serverResponseMessage,Toast.LENGTH_LONG).show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                return param;
            }


        };


        jsonForGetRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonForGetRequest);


    }
}
