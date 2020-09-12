package com.example.vss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ChangePasswd extends AppCompatActivity {
    EditText txt_username,txt_passwd;
    Button btn_change_passwd;
    String username,password;
    String resp1;
    public static String webmethod="change_password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_passwd_activity);


        txt_username=(EditText)findViewById(R.id.user_name);
        txt_passwd=(EditText)findViewById(R.id.passwd);
        btn_change_passwd=(Button)findViewById(R.id.change_password);
        btn_change_passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=txt_username.getText().toString().trim();
                password=txt_passwd.getText().toString().trim();
                if(username.equals("")||password.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Password and username can not  blank", Toast.LENGTH_SHORT).show();
                }
                else {
                    CallWebservice c= new CallWebservice();
                    c.execute();
                }
            }
        });
    }
    public String change_passwd()
    {
        SoapObject request = new SoapObject(Data_activity.NAMESPACE, webmethod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        request.addProperty("username",username);
        request.addProperty("passwd",password);

        envelope.setOutputSoapObject(request);
        Log.d("my request",String.valueOf(request));
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Data_activity.URL);

        try
        {
            androidHttpTransport.call(Data_activity.NAMESPACE+webmethod, envelope);
            SoapPrimitive response =  (SoapPrimitive)envelope.getResponse();

            resp1=response.toString();
            return  resp1;
        }
        catch (Exception e)
        {
            /*resp1=e.getMessage().toString();
            return e.getMessage().toString();*/
            resp1=e.getMessage()+"p";
            return e.getMessage()+"k";
        }
    }



    class  CallWebservice extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... arg0)
        {
           change_passwd();
            return null;
        }
        protected void onPostExecute(Void result)
        {

            Toast.makeText(getApplicationContext(), resp1, Toast.LENGTH_SHORT).show();
            if(resp1.equals("1")){

                    Intent i = new Intent(ChangePasswd.this, MainActivity.class);
                    startActivity(i);

                    Toast.makeText(getApplicationContext(), "Passwd change", Toast.LENGTH_SHORT).show();
                }
            else
            {
                Toast.makeText(getApplicationContext(), "password not chnaged", Toast.LENGTH_SHORT).show();
            }

        }
        protected void onPreExecute()
        {
            Toast.makeText(getApplicationContext(), "connecting to sever", Toast.LENGTH_SHORT).show();
        }
    }
}

