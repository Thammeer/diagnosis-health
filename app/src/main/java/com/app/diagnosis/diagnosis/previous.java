package com.app.diagnosis.diagnosis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public class previous extends AppCompatActivity {
    ArrayList<HashMap<String, String>> previous;
    ProgressDialog PD;
    SimpleAdapter adapter;
    ListView myList;
    public static final String _ID = "id";
    public static final String _user_id = "user_id";
    public static final String _diagnosis = "diagnosis";
    public static final String _date = "date";
    public static final String _time = "time";
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous);

        previous= new ArrayList<HashMap<String, String>>();


        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        final Intent intent = getIntent();
        id = intent.getStringExtra("id");

        myList = (ListView) findViewById(R.id.list);


      ReadDataFromDB();




    }

    Retrofit previouss = new Retrofit.Builder().
            baseUrl("http://diag.esy.es/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();




    private void ReadDataFromDB() {
        PD.show();

        prev apiService = previouss.create(prev.class);
        Call<Results> reg = apiService.getprev(id);


        reg.enqueue(new Callback<Results>() {

            @Override
            public void onResponse(Response<Results> response, Retrofit retrofit) {


                if (response.body().getMessage().equals("0")) {

                    Toast.makeText(getApplicationContext(), "No Diagnosis", Toast.LENGTH_LONG).show();
                    PD.dismiss();



                } else {


                    try {

                        List<previous_json> info = response.body().getPrevious();

                        for (int i = 0; i < info.size(); i++) {

                            HashMap<String, String> data = new HashMap<String, String>();
                            data.put(_ID, info.get(i).getId());
                            data.put(_user_id,info.get(i).getuser_id());
                            data.put(_diagnosis,info.get(i).getDiagnosis());
                            data.put(_date, info.get(i).getDate());
                            data.put(_time,info.get(i).getTime());




                            previous.add(data);

                        } // for loop ends






                        String [] fromFieldNames = new String[] { _diagnosis , _date, _time };
                        int [] toViewIDs = new int [] { R.id.patient_name, R.id.date,R.id.a_time};

                        adapter = new SimpleAdapter(getApplicationContext(), previous,R.layout.dr_appointments, fromFieldNames, toViewIDs);

                        myList.setAdapter(adapter);
                        PD.dismiss();




                        PD.dismiss();

                    }
                    catch (JsonSyntaxException e)
                    {
                        Toast.makeText(getApplicationContext(), "Exception "+e, Toast.LENGTH_LONG).show();
                        PD.dismiss();
                    }
                    catch (JsonIOException e)
                    {
                        Toast.makeText(getApplicationContext(), "Exception "+e, Toast.LENGTH_LONG).show();
                        PD.dismiss();
                    }

                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "Exception"+e, Toast.LENGTH_LONG).show();
                        PD.dismiss();
                    }




                }


            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), "connection error"+t, Toast.LENGTH_LONG).show();
                PD.dismiss();

            }
        });



    }


    public interface prev {
        @FormUrlEncoded
        @POST("previous.php")
        Call<Results> getprev(@Field("id") String id
        );

    }




}
