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

import java.util.ArrayList;
import java.util.List;

public class register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText txt_no,txt_username,txt_passwd,txt_con_passwd;
    Button btn_register;
    Spinner spin;
    String utype;
    String usertype="Select-user-type";
    public static String webmethod="register";
    String no,username,passwd,con_password;
    String resp1;
    List<String> Type=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        txt_no = findViewById(R.id.txt_no);
        txt_username= findViewById(R.id.user_name);
        txt_passwd= findViewById(R.id.passwd);
        txt_con_passwd= findViewById(R.id.con_password);
        btn_register=findViewById(R.id.btn_register);
        spin = findViewById(R.id.spin);
        spin.setOnItemSelectedListener(this);
        Type.add("Select-user-type");
        Type.add("Teacher");
        Type.add("Student");
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                no = txt_no.getText().toString();
                username = txt_username.getText().toString();
                passwd = txt_passwd.getText().toString();
                con_password = txt_con_passwd.toString();
                if (no.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter PNR"+1, Toast.LENGTH_SHORT).show();
                } else if (username.equals("") || passwd.equals("")) {
                    Toast.makeText(getApplicationContext(), "username and password can not be blank"+2, Toast.LENGTH_SHORT).show();
                } else if (passwd.equals(con_password)) {
                    Toast.makeText(getApplicationContext(), "Password does not match"+3, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), utype, Toast.LENGTH_SHORT).show();
                } else if (utype.equals("Select-user-type")) {
                    Toast.makeText(getApplicationContext(), "Select the user type"+4, Toast.LENGTH_SHORT).show();
                } else {

                    CallWebservice p = new CallWebservice();
                    p.execute();                }
            }
        });
    }
    public String validateregister()
    {
        SoapObject request = new SoapObject(Data_activity.NAMESPACE, webmethod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        request.addProperty("no",no);
        request.addProperty("username",username);
        request.addProperty("passwd",passwd);
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
            resp1=e.getMessage()+"8";
            return e.getMessage()+"9";
        }
    }



    class  CallWebservice extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... arg0)
        {
            validateregister();
            return null;
        }
        protected void onPostExecute(Void result)
        {

            Toast.makeText(getApplicationContext(), resp1+"pooja", Toast.LENGTH_SHORT).show();
            if(resp1.equals("1")){
                if(utype.equals("Teacher")){
                    Intent i = new Intent(register.this, login.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Welcome Teacher"+44, Toast.LENGTH_SHORT).show();
                }else {
                    if(utype.equals("Student")){
                        Intent i = new Intent(register.this, login.class);
                        startActivity(i);
                        Data_activity.pnr=no;
                        Toast.makeText(getApplicationContext(), "Welcome Student"+5, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "enter a valid username and password"+6, Toast.LENGTH_SHORT).show();
            }

        }
        protected void onPreExecute()
        {

            Toast.makeText(getApplicationContext(), "connecting to sever"+7, Toast.LENGTH_SHORT).show();


        }
    }
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3)
    {
        utype=spin.getItemAtPosition(arg2).toString();
        Toast.makeText(getApplicationContext(), "spinner"+7, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
