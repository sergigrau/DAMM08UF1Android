package edu.fje.dam2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe que utilitza la classe HTTPURLConnection, per a enviar i rebre dades
 * en format JSON. Els fitxers de backend es troben al directori /assets
 * el backend està desenvolupat en llenguatge Python
 *
 * @author sergi.grau@fje.edu
 * @version 3.0 16.12.2016
 * @version 4.0, 10.1.2021 actualització a API30
 */
public class M36_ComunicacioJSONActivity extends AppCompatActivity {
    //cal desactivar proxy d'Android
    //cal posar a res/xml un fixter  res/xml/network_security_config.xml
    /*
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">172.20.16.207</domain>
    </domain-config>
</network-security-config>

    //i afegir a manifest
     <application
    android:networkSecurityConfig="@xml/network_security_config"
     */
    private static final String URL = "http:/192.168.1.14:8000/?nom=" ;

    private Button botoEnviar;
    private TextView text;
    private EditText nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // per evitar que el fil de la GUI tingui ocupada la comunicació
        // caldria millorar-ho amb un servei
        StrictMode.ThreadPolicy politiques = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(politiques);

        setContentView(R.layout.m34_connexio_http);

        botoEnviar = (Button) findViewById(R.id.botoEnviar) ;
        text = (TextView) findViewById(R.id.text) ;
        nom = (EditText) findViewById(R.id.nom) ;

        botoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enviar dades a Backend
                try {
                    URL urlObj = new URL(URL+nom.getText().toString());
                    text.setText(llegirJSON(urlObj));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        String dadesLLegides = null;
        try {
            URL urlObj = new URL(URL);
            dadesLLegides = llegirJSON(urlObj);
            Log.i(M36_ComunicacioJSONActivity.class.getName(), dadesLLegides);
            text.setText(dadesLLegides);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            //mostrar les dadesl de JSON rebut
            JSONObject objecte = new JSONObject(dadesLLegides);
            Log.i(M36_ComunicacioJSONActivity.class.getName(), "Nombre d'entrades "
                    + objecte.length());
            Log.i(M36_ComunicacioJSONActivity.class.getName(),
                    objecte.getString("nom"));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mètode que llegeix les dades des d'un servidor web o servidor
     * d'aplicacions i crea un objecte JSON per a manipular les dades
     *
     * @return cadena obtinguda
     */


    public String llegirJSON(URL urlObj) throws IOException {
        StringBuilder builder = new StringBuilder();
        HttpURLConnection clientHTTP = (HttpURLConnection) urlObj.openConnection();

        if (clientHTTP.getResponseCode() == 200) {
            InputStream contingut = clientHTTP.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(contingut));
            String linia;
            while ((linia = reader.readLine()) != null) {
                builder.append(linia);
            }
        } else {
            Log.e(M36_ComunicacioJSONActivity.class.toString(), "Problemes HTTP");
        }
        return builder.toString();
    }

    /**
     * Mètode per escriure dades en una extensió de servidor web o servidor
     * d'aplicacions
     */

    public void escriureJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("nom", "Sergi");
            object.put("cognom", "Grau");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}