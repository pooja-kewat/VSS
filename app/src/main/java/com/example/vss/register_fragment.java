package com.example.vss;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class register_fragment extends Fragment implements AdapterView.OnItemSelectedListener{
    EditText txt_no,txt_username,txt_passwd,txt_con_passwd,txt_email;
    Button btn_register;
    Spinner spin;
    String utype;
    String usertype="Select-user-type";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String webmethod="register";
    String no,username,passwd,con_password,email;
    String resp1;
    List<String> Type=new ArrayList<String>();

    public register_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_fragment, container, false);
        txt_no = view.findViewById(R.id.txt_no);
        txt_username= view.findViewById(R.id.user_name);
        txt_passwd= view.findViewById(R.id.passwd);
        txt_con_passwd= view.findViewById(R.id.con_password);
        txt_email= view.findViewById(R.id.email);
        btn_register=view.findViewById(R.id.btn_register);
        spin = view.findViewById(R.id.spin);
        spin.setOnItemSelectedListener(this);
        Type.add("Select-user-type");
        Type.add("Teacher");
        Type.add("Student");
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,Type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                no = txt_no.getText().toString();
                email=txt_email.getText().toString();
                username = txt_username.getText().toString();
                passwd = txt_passwd.getText().toString();
                con_password = txt_con_passwd.toString();
                if (no.equals("")) {
                    Toast.makeText(getActivity(), "Enter PNR"+1, Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern))
                {
                    Toast.makeText(getActivity(),"Invalid email address", Toast.LENGTH_SHORT).show();
                }
                else if (username.equals("") || passwd.equals("")) {
                    Toast.makeText(getActivity(), "username and password can not be blank"+2, Toast.LENGTH_SHORT).show();
                } else if (passwd.equals(con_password)) {
                    Toast.makeText(getActivity(), "Password does not match"+3, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), utype, Toast.LENGTH_SHORT).show();
                } else if (utype.equals("Select-user-type")) {
                    Toast.makeText(getActivity(), "Select the user type"+4, Toast.LENGTH_SHORT).show();
                } else {

                    CallWebservice p = new CallWebservice();
                    p.execute();                }
            }
        });
        return(view);
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_register_fragment, container, false);
    }
    public String validateregister()
    {
        SoapObject request = new SoapObject(Data_activity.NAMESPACE, webmethod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        request.addProperty("no",no);
        request.addProperty("email",email);
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

            Toast.makeText(getActivity(), resp1, Toast.LENGTH_SHORT).show();
            if(resp1.equals("1")){
                if(utype.equals("Teacher")){

                    Data_activity.tea_id=no;
                    Toast.makeText(getActivity(), "Welcome Teacher", Toast.LENGTH_SHORT).show();
                }else {
                    if(utype.equals("Student")){

                        Data_activity.pnr=no;
                        Toast.makeText(getActivity(), "Welcome Student", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(getActivity(), "enter a valid username and password", Toast.LENGTH_SHORT).show();
            }

        }
        protected void onPreExecute()
        {

            Toast.makeText(getActivity(), "connecting to sever"+7, Toast.LENGTH_SHORT).show();


        }
    }
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3)
    {
        utype=spin.getItemAtPosition(arg2).toString();
        Toast.makeText(getActivity(), "spinner"+7, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
