package com.eg.crudmysql;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MantenimientoMysql {

    public void guardar(final Context context, final String codigo, final String descripcion, final String precio) {
        String url = "http://mjgl.com.sv/democrudsis21a/guardar.php";
        //String url = "http://localhost/MysqlAndroid/mysql/guardar.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if (estado.equals("1")){
                        Toast.makeText(context, "registro almacenado en MYSQL.", Toast.LENGTH_SHORT).show();
                    }else if (estado.equals("2")){
                        Toast.makeText(context, "Error no se puede guardar. \n+" + "intentelo mas tarde.", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            protected Map<String, String> getParams () throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("content-Type", "application/json: charset=utf-8");
                map.put("Accept", "applocation/json");
                map.put("codigo", codigo);
                map.put("descripcion", descripcion);
                map.put("precio", precio);
                return map;
            }
        };

    }

    public void consultar(final Context context, String codigo) {
    }

    public void modificar(final Context context, String codigo, String descripcion, String precio) {
    }

    public void eliminar(final Context context, String codigo) {

    }
}
