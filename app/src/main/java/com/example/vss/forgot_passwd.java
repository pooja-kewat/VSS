package com.example.vss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class forgot_passwd extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText txt_no;
    Spinner spin;
    Button btn_next;
    String no;
    public static String webmethod="next";
    String resp1;
    String utype;
    String usertype="Select-user-type";
    List<String> Type=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passwd_activity);
        txt_no=findViewById(R.id.no);
        spin=(Spinner)findViewById(R.id.spinner1);
        btn_next=findViewById(R.id.btn_next);
        spin.setOnItemSelectedListener(this);
        Type.add("Select-user-type");
        Type.add("Teacher");
        Type.add("Student");
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no=txt_no.getText().toString().trim();
                if(no.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Enter PNR/Teacher_id "+1, Toast.LENGTH_SHORT).show();
                }
                else if(utype.equals("Select-user-type"))
                {
                    Toast.makeText(getApplicationContext(), "Select the user type"+2, Toast.LENGTH_SHORT).show();
                }
                else {
                    CallWebservice c = new CallWebservice();
                    c.execute();

                }
            }
        });


    }
    public String next_verify()
    {
        SoapObject request = new SoapObject(Data_activity.NAMESPACE, webmethod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        request.addProperty("no",no);

        request.addProperty("utype",utype);

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
            resp1=e.getMessage()+"p";
            return e.getMessage()+"k";
        }
    }

    class  CallWebservice extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... arg0)
        {
            next_verify();
            return null;
        }
        protected void onPostExecute(Void result)
        {

            Toast.makeText(getApplicationContext(), resp1, Toast.LENGTH_SHORT).show();
            if(resp1.equals("1")){
                if(utype.equals("Teacher")){
                    Intent i = new Intent(forgot_passwd.this,email_verification.class);
                    startActivity(i);
                    Data_activity.tea_id=no;
                    Toast.makeText(getApplicationContext(), "Welcome Teacher", Toast.LENGTH_SHORT).show();
                }else {
                    if(utype.equals("Student")){
                        Intent i = new Intent(forgot_passwd.this,email_verification.class);
                        startActivity(i);
                        Data_activity.pnr=no;
                        Toast.makeText(getApplicationContext(), "Welcome Student", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "enter a valid username and password", Toast.LENGTH_SHORT).show();
            }

        }
        protected void onPreExecute()
        {
            Toast.makeText(getApplicationContext(), "connecting to sever", Toast.LENGTH_SHORT).show();


        }
    }
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3)
    {
        utype=spin.getItemAtPosition(arg2).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
