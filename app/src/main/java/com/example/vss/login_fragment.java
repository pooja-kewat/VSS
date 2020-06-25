package com.example.vss;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
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
public class login_fragment extends Fragment implements AdapterView.OnItemSelectedListener{
    EditText txt_no,txt_username,txt_passwd;
   TextView txt_forget_passwd;
    Button btn_login;
    Spinner spinner;
    String utype;
    String usertype="Select-user-type";
    public static String webmethod="login";
    String no,username,password;
    String resp1;
    List<String> Type=new ArrayList<String>();

    public login_fragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_fragment, container, false);

        txt_no=(EditText) view.findViewById(R.id.no);
        txt_username=(EditText)view.findViewById(R.id.user_name);
        txt_passwd=(EditText) view.findViewById(R.id.passwd);
        btn_login= view.findViewById(R.id.passwd_change);
        txt_forget_passwd=(TextView) view.findViewById(R.id.Forgot_Password);
        spinner=(Spinner)view.findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(this);
        Type.add("Select-user-type");
        Type.add("Teacher");
        Type.add("Student");
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,Type);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        txt_forget_passwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_forget_passwd.setTextColor(Color.GREEN);
                Intent it = new Intent(login_fragment.this.getActivity(),forgot_passwd.class);
                startActivity(it);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            private Context applicationContext;

            @Override
            public void onClick(View v) {
                String item=spinner.getSelectedItem().toString();
                no=txt_no.getText().toString().trim();
                username=txt_username.getText().toString().trim();
                password=txt_passwd.getText().toString().trim();
                if(no.equals(""))
                {
                    Toast.makeText(getActivity(), "Enter PNR/Teacher_id ", Toast.LENGTH_SHORT).show();
                }
                else if(username.equals("")||password.equals(""))
                {
                    Toast.makeText(getActivity(), "Password and username can not  blank", Toast.LENGTH_SHORT).show();
                }
                else if(utype.equals("Select-user-type"))
                {
                    Toast.makeText(getActivity(), "Select the user type", Toast.LENGTH_SHORT).show();
                }
                else {
                    CallWebservice o=new CallWebservice();
                    o.execute();

                }




            }
        });
        return (view);
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_login_fragment, container, false);
    }

    public String validatelogin()
    {
        SoapObject request = new SoapObject(Data_activity.NAMESPACE, webmethod);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        request.addProperty("no",no);
        request.addProperty("username",username);
        request.addProperty("passwd",password);
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
            resp1=e.getMessage().toString();
            return e.getMessage().toString();
        }
    }

   /* public void forget_passwd(View view) {
        txt_forget_passwd=(TextView) view.findViewById(R.id.Forgot_Password);
        txt_forget_passwd.setTextColor(Color.GREEN);
        Intent it = new Intent(login_fragment.this.getActivity(),forgot_passwd.class);
        startActivity(it);
    }*/

    class  CallWebservice extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... arg0)
        {
            validatelogin();
            return null;
        }
        protected void onPostExecute(Void result)
        {

            Toast.makeText(getActivity(), resp1, Toast.LENGTH_SHORT).show();
            if(resp1.equals("1")){
                if(utype.equals("Teacher")){
                    Intent i = new Intent(login_fragment.this.getActivity(), Teacher_Dashboard.class);
                    startActivity(i);
                    Data_activity.tea_id=no;
                    Toast.makeText(getActivity(), "Welcome Teacher", Toast.LENGTH_SHORT).show();
                }else {
                    if(utype.equals("Student")){
                        Intent i = new Intent(login_fragment.this.getActivity(), Student_dashboard.class);
                        startActivity(i);
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
            Toast.makeText(getActivity(), "connecting to sever", Toast.LENGTH_SHORT).show();


        }
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3)
    {
        utype=spinner.getItemAtPosition(arg2).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
