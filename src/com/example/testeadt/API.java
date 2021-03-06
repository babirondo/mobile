package com.example.testeadt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;


import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;



public class API extends AsyncTask<String, String, String> {
	
	public JSONObject JSON = new JSONObject();//responseStrBuilder.toString()
	Object  mListener;
	Object instance;
	public boolean Resultado_PUT;
	public String callback_setado = "default" ;

	 
	
    public API(Object obj) {
		// TODO Auto-generated constructor stub
    	instance = obj;
	}
    
    /*
    public boolean LoadFeed(String urlString)
    {
    	Log.d("BrunoAPI","Method LoadFeed");
        String resultToDisplay = "";
        InputStream in = null;
        
        try {

 

                return true;
          
         } catch (Exception e ) {

            Log.e("ERROR",e.toString());
            return false;
         } 
    }
    */
    
    public boolean GetRest(String urlString)
    {
    	Log.d("BrunoAPI","get Rest");
        String resultToDisplay = "";
        InputStream in = null;
        
    	// HTTP Get
        try {

            URL url = new URL(urlString);


            Log.d("BrunoAPI", "getrest Rota pedida:" +urlString);
            HttpURLConnection urlConnection = null;
            
            
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
           // urlConnection.getResponseCode();
            
           // if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
           
                InputStream AquiOuAli =  (urlConnection != null) ? urlConnection.getInputStream() : null;
                in = new BufferedInputStream(AquiOuAli);
                
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8")); 
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null)
                    responseStrBuilder.append(inputStr);
                this.JSON = new JSONObject(responseStrBuilder.toString());
                
                Log.d("BrunoAPI", "JSON pego pelo get: " +JSON.toString());

                return true;
            	
          //  }
          //  else 	return false;
         } catch (Exception e ) {

            
            Log.e("ERROR",e.toString());
            return false;

         } 
    	
    }

    
    public boolean PutRest(String urlString, String Json)
    {
    	 
        Log.d("BrunoAPI","Put Rest");

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(urlString);
            StringEntity se = new StringEntity(Json);
            httpPut.setEntity(se);
            httpPut.addHeader("Accept", "application/json");
            httpPut.addHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httpPut);
            System.out.println(urlString + " Resposta: "+ response.getStatusLine().getStatusCode() + " Json enviado ->" + Json.toString());
            
            if (response.getStatusLine().getStatusCode() == 200){

                BufferedReader br = new BufferedReader(new InputStreamReader(  (response.getEntity().getContent())));
                String output;
                StringBuilder result = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    result.append(output);
                }
                this.JSON = new JSONObject(result.toString());

                System.out.println(" Json respondido ->" + this.JSON.toString());
                return true;
            }
            else 
            	return false;
             
            

        } catch (Exception e) {
            Log.d("BRunoAPI-PUT", e.getLocalizedMessage());
        }
            
    	return true;
    }

    
	@Override
    protected String doInBackground(String... params) {

        String urlString=params[0]; // URL to call
        String comando=params[1]; // URL to call
       
       
        
          
         
        Log.d("BrunoAPI","entrou no doing"); 
        
        switch (comando){
        /*
	    	case ("Feed.LoadFeed"):
	            if (!this.LoadFeed( ) )
	        		Log.d("BrunoAPI", "houve um erro na captura do JSON");
	    	break;
	    	*/
	
	    	case ("get"):
	            if (!this.GetRest(urlString))
	        		Log.d("BrunoAPI", "houve um erro na captura do JSON");
	    	break;
	
	    	case ("put"):
	            if (!this.PutRest(urlString, JSON.toString())){
	            	Log.d("BrunoAPI", "houve um erro no salvar dos dados");
	            	this.Resultado_PUT = false;
	            }
	            else{
	            	this.Resultado_PUT = true;

	            }
	            	
	        		
	    	break;
        }
    	Log.d("BrunoAPI","doing");
		return "1";
    
    }

    protected void onPostExecute(String result) {
    	Log.d("BrunoAPI","post execute"  );


    	switch (this.callback_setado)
    	{
    		case ("SaveFeed"):
                ((CallBackListener) mListener).SaveFeedCallback(this.instance);
                break;
    		
    		default:
                ((CallBackListener) mListener).callback( this.instance);
    	}
    	
    	
    }
    

    public void setListener(Object listener){
      mListener = (CallBackListener) listener;
    }
    
    public void Adicionar( String Chave, String Valor)
    {
    	try {
    	//	Log.d("BrunoAPI", "Chave "+Chave+" Valor "+Valor);
 			JSON.put( Chave, Valor );//
						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
         
    	
    	
    	//System.out.println(JSON.toString());
    }

    
} // end CallAPI 