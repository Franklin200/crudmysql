package com.eg.crudmysql;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MantenimientoMySQL {

     /*
        public MantoInterface manto;
        public interface MantoInterface {
            //void onSetDatosInput(String codigo, String descripcion, String precio);
            void onSetDatosInput(Dto datos);
        }

        public MantenimientoMySQL(MantoInterface mano) {
            manto = mano;
        }

        public MantenimientoMySQL() {

        }
    */

    Dto datos = new Dto();

    boolean estadoGuardar = false;
    boolean estadoEliminar = false;

    private ProgressDialog pd;
    AlertDialog.Builder dialogo1;
    AlertDialog.Builder dialogo;
    ProgressDialog progressDialog;

    //List<Productos> productosList;
    //List<> productosList;
    ProductsAdapter adapter;

    public void guardar(final Context context, final String codigo, final String descripcion, final String precio){
        String url = Config.urlGuardar;
        //String url = "localhost/democrudsis21a/guardar.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //En este método se recibe la respuesta en json desde el web service o API.

                        try{
                            JSONObject requestJSON = new JSONObject(response.toString());
                            String estado = requestJSON.getString("estado");
                            String mensaje = requestJSON.getString("mensaje");

                            if(estado.equals("1")){
                                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context, "Registro almacenado en MySQL.", Toast.LENGTH_SHORT).show();
                            }else if(estado.equals("2")){
                                Toast.makeText(context, ""+mensaje, Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            //Toast.makeText(context, "Se encontrarón problemas...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //En este método se notifica al usuario acerca de un posible error al tratar de
                //realizar una acción cualquier en la base de datos remota.
                Toast.makeText(context, "No se puedo guardar. \n" +
                        "Verifique su acceso a internet.", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("codigo", codigo);
                map.put("descripcion", descripcion);
                map.put("precio", precio);
                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }


    public boolean guardar1(final Context context, final String codigo, final String descripcion, final String precio) {
        //String url = "http://mjgl.com.sv/mysqlcrud/guardar.php";
        String url  = Config.urlGuardar;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //En este método se recibe la respuesta en json desde el web service o API.

                        try {
                            JSONObject requestJSON = new JSONObject(response.toString());
                            String estado = requestJSON.getString("estado");
                            String mensaje = requestJSON.getString("mensaje");

                            if (estado.equals("1")) {
                                //Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                estadoGuardar = true;
                            } else if (mensaje.equals("2")) {
                                Toast.makeText(context, "Error. No se pudo guardar.\n" +
                                        "Intentelo mas tarde.", Toast.LENGTH_SHORT).show();
                                estadoGuardar = false;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(context, "Se encontrarón problemas...", Toast.LENGTH_SHORT).show();
                            estadoGuardar = false;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //En este método se notifica al usuario acerca de un posible error al tratar de
                //realizar una acción cualquier en la base de datos remota.
                Toast.makeText(context, "No se puedo guardar. \n" +
                        "Verifique su acceso a internet.", Toast.LENGTH_SHORT).show();
                estadoGuardar = false;
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //En este método se colocan o se setean los valores a recibir por el fichero *.php
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("codigo", codigo);
                map.put("descripcion", descripcion);
                map.put("precio", precio);
                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

        return estadoGuardar;
    }


    public void eliminar(final Context context, final String codigo){

        progressDialog = new ProgressDialog(context);

        dialogo = new AlertDialog.Builder(context);
        dialogo.setIcon(R.drawable.ic_delete);
        dialogo.setTitle("¡¡¡Advertencia!!!");
        dialogo.setMessage("¿Realmente desea borrar el registro?\n" +
                "Código: "+codigo);
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

                progressDialog.setCancelable(false);
                progressDialog.setMessage("Espere por favor, Estamos trabajando en el servidor");
                progressDialog.show();

                //String url = "http://mjgl.com.sv/mysqlcrud/eliminar.php";
                String url  = Config.urlEliminar;

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                            JSONObject respuestaJSON = new JSONObject(response.toString());         //Creo un JSONObject a partir del StringBuilder pasado a cadena
                            String resultJSON = respuestaJSON.getString("estado");            // estado es el nombre del campo en el JSON
                            String result_msj = respuestaJSON.getString("mensaje");           // estado es el nombre del campo en el JSON
                            if (resultJSON.equals("1")) {

                                Toast toast = Toast.makeText(context, result_msj, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                            } else if (resultJSON.equals("2")) {
                                Toast toast = Toast.makeText(context, "--> Nothing." +
                                        "\n" + result_msj, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();
                        Toast.makeText(context, "Algo salio mal. Intente mas tarde\n" +
                                "Verifique su acceso a Internet.", Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("Content-Type", "application/json; charset=utf-8");
                        map.put("Accept", "application/json");
                        map.put("codigo", codigo);
                        return map;
                    }
                };

                MySingleton.getInstance(context).addToRequestQueue(request);
            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                Toast toast = Toast.makeText(context, "Operación Cancelada.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        //AlertDialog dialogo = builder.create();
        dialogo.show();

    }


    //public void consultarCodigo(final Context context, final String codigo){
    public void consultarCodigo(final Context context, final String codigo){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Espere por favor, Estamos trabajando en su petición en el servidor");
        progressDialog.show();

        String url  = Config.urlConsultaCodigo;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) {
                            Toast.makeText(context, "No se encontrarón resultados para la búsqueda especificada.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            try {
                                /*
                                Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                */
                                JSONArray jsonArray = new JSONArray(response);
                                String codigo = jsonArray.getJSONObject(0).getString("codigo");
                                String descripcion = jsonArray.getJSONObject(0).getString("descripcion");
                                String precio = jsonArray.getJSONObject(0).getString("precio");


                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("senal", "1");
                                intent.putExtra("codigo", codigo.toString());
                                intent.putExtra("descripcion", descripcion);
                                intent.putExtra("precio", precio);
                                context.startActivity(intent);


                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Toast.makeText(context, "No se ha podido establecer conexión con el servidor. Verifique su acceso a Internet.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("codigo",codigo);
                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


    public void consultarDescripcion(final Context context, final String descripcion){

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Espere por favor, Estamos trabajando en su petición en el servidor");
        progressDialog.show();

        String url  = Config.urlConsultaDescripcion;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) {
                            Toast.makeText(context, "No se encontrarón resultados para la búsqueda especificada.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else{
                            try {
                                /*
                                Toast toast = Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                */
                                JSONArray jsonArray = new JSONArray(response);
                                String codigo = jsonArray.getJSONObject(0).getString("codigo");
                                String descripcion = jsonArray.getJSONObject(0).getString("descripcion");
                                String precio = jsonArray.getJSONObject(0).getString("precio");

                                datos.setCodigo(Integer.parseInt(codigo));
                                datos.setDescripcion(descripcion);
                                datos.setPrecio(Double.parseDouble(precio));

                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("senal", "1");
                                intent.putExtra("codigo", codigo.toString());
                                intent.putExtra("descripcion", descripcion);
                                intent.putExtra("precio", precio);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                context.startActivity(intent);

                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Toast.makeText(context, "No se ha podido establecer conexión con el servidor. Verifique su acceso a Internet.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("descripcion", descripcion);
                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }




    public ArrayList<String> consultarAllArticulos(final Context context){

        final ArrayList productosList = new ArrayList<>();  //ArrayList<String>

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Espere por favor, Estamos trabajando en su petición en el servidor");
        progressDialog.show();

        String url  = Config.urlConsultaAllArticulos;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray array = new JSONArray(response);
                            int totalEncontrados = array.length();
                            Toast.makeText(context, "Total: "+totalEncontrados, Toast.LENGTH_SHORT).show();

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject articulosObject = array.getJSONObject(i);

                                int codigo = articulosObject.getInt("codigo");
                                String descripcion = articulosObject.getString("descripcion");
                                double precio = articulosObject.getDouble("precio");
                                String img = articulosObject.getString("imagen");
                                Productos objeto = new Productos(codigo, descripcion, precio, img);
                                productosList.add(objeto);

                                /*
                                productosList.add(new Productos(
                                        articulosObject.getInt("codigo"),
                                        articulosObject.getString("descripcion"),
                                        articulosObject.getDouble("precio"),
                                        articulosObject.getString("imagen")
                                ));*/
                            }

                            //adapter = new ProductsAdapter(context, productosList);
                            //recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error. Compruebe su acceso a Internet.", Toast.LENGTH_SHORT).show();
            }
        });

        //Volley.newRequestQueue(this).add(stringRequest);
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

        return productosList;
    }


    //public void modificar(final Context context, final String codigo, final String descripcion, final String precio){
    public void modificar(final Context context, final Dto datos){

        progressDialog = new ProgressDialog(context);

        progressDialog.setCancelable(false);
        progressDialog.setMessage("Espere por favor, Estamos trabajando en su petición en el servidor");
        progressDialog.show();

        String url = Config.urlActualizar;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto. Esperando que todo
                            JSONObject respuestaJSON = new JSONObject(response.toString());                 //Creo un JSONObject a partir del StringBuilder pasado a cadena

                            //Accedemos al vector de resultados
                            String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON
                            String result_msj = respuestaJSON.getString("mensaje");   // estado es el nombre del campo en el JSON

                            if (resultJSON.equals("1")) {

                                Toast toast = Toast.makeText(context, ""+result_msj, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                            } else if (resultJSON.equals("2")) {
                                Toast toast = Toast.makeText(context, ""+result_msj, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "Algo salio mal con la conexión al servidor. \nRevise su conexión a Internet.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("codigo", String.valueOf(datos.getCodigo()));
                map.put("descripcion", datos.getDescripcion());
                map.put("precio", String.valueOf(datos.getPrecio()));
                /*
                map.put("codigo", codigo);
                map.put("descripcion", descripcion);
                map.put("precio", precio);
                */
                return map;

            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public String informacion(Dto datos){
        String info;
        info = "Codigo = "+datos.getCodigo() + "\n" ;
        info += "Descripción = "+datos.getDescripcion() + "\n";
        info += "Precio = "+datos.getPrecio() + "\n";
        return info;
    }


    public void createfile(Context context, String codigo, String descripcion, String precio){
        SharedPreferences preferences = context.getSharedPreferences("profeGamez", MODE_PRIVATE);
        //OBTENIENDO LA FECHA Y HORA ACTUAL DEL SISTEMA.
        DateFormat formatodate= new SimpleDateFormat("yyyy/MM/dd");
        String date= formatodate.format(new Date());
        DateFormat formatotime= new SimpleDateFormat("HH:mm:ss a");
        String time= formatotime.format(new Date());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fecha", date);
        editor.putString("hora", time);
        editor.putString("codigo", codigo);
        editor.putString("descripcion", descripcion);
        editor.putString("precio", precio);
        editor.commit();
    }


    /*
    public String obtenerCodigo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("filetime", MODE_PRIVATE);
        String codigo = preferences.getString("codigo","0");
        return codigo;   //return preferences.getString("tiempo", "Sin configurar.");
    }

    public String obtenerDescripcion(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("filetime", MODE_PRIVATE);
        String descripcion = preferences.getString("descripcion","Sin descripción");
        return descripcion;   //return preferences.getString("tiempo", "Sin configurar.");
    }

    public String obtenerPrecio(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("filetime", MODE_PRIVATE);
        String precio = preferences.getString("precio","0.0");
        return precio;   //return preferences.getString("tiempo", "Sin configurar.");
    }
    */

}
