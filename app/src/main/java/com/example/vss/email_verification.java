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

public class email_verification extends AppCompatActivity {
EditText txt_code;
Button btn_verify;
String code,resp1;
    public static String webmethod="verify";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_verification_activity);
        txt_code=findViewById(R.id.code);
        btn_verify=findViewById(R.id.login);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=txt_code.getText().toString().trim();
                if(code.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Enter Verification code", Toast.LENGTH_SHORT).show();
                }
                else {
                    CallWebservice c= new CallWebservice();
                    c.execute();


                }

            }
        });
    }

    public String verify()
    {
        SoapObject request = new SoapObject(Data_activity.NAMESPACE, webmethod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        request.addProperty("code",code);


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
            resp1=e.getMessage().toString();
            return e.getMessage().toString();
        }
    }



    class  CallWebservice extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... arg0)
        {
            verify();
            return null;
        }
        protected void onPostExecute(Void result)
        {

            Toast.makeText(getApplicationContext(), resp1, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(email_verification.this,ChangePasswd.class);
                startActivity(i);

                Toast.makeText(getApplicationContext(), "Verify", Toast.LENGTH_SHORT).show();
        }
        protected void onPreExecute()
        {
            Toast.makeText(getApplicationContext(), "connecting to sever", Toast.LENGTH_SHORT).show();


        }
    }
}
