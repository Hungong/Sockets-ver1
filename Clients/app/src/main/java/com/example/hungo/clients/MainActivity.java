package com.example.hungo.clients;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    ListView lv;
    Context context;

    ArrayList prgmName;
    public int[] prgmImages = {R.drawable.file, R.drawable.folder};
    ArrayList nameFile = new ArrayList();
    ArrayList nameFolder = new ArrayList();

    String hotsName = "192.168.2.30";
    Socket socket = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    public Adapter adapter;

//    Handler mHanler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button butConect = (Button) findViewById(R.id.butConnect);
        butConect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connect();
                    }
                });
                thread.start();
                lv = (ListView) findViewById(R.id.lvView);
                adapter = new CustomAdapter(MainActivity.this, nameFile, prgmImages);
                lv.setAdapter((ListAdapter) adapter);

            }
        });

//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                lv.setVisibility(View.GONE);
//
//                Runnable r = new Runnable() {
//                    @Override
//                    public void run() {
//                        lv.setVisibility(View.GONE);
//                    }
//                };
//                mHanler.post(r);
//
//            }
//        });
    }

    public void Connect() {
        try {
            socket = new Socket(hotsName, 9999);

            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.write("OK");
            out.newLine();
            out.flush();

            String respon;
            respon = in.readLine();
            if (respon != null) {
//                for(String s : respon.split("000")){
//                    for(String s1: s.split("0")){
//                        context = MainActivity.this;
//                        prgmNameList.add(s1.toString());
//                    }
//                }
                JSONObject jsonData = new JSONObject(respon);
                JSONArray dataFile = new JSONArray(jsonData.getString("File"));
                for (int i = 0; i < dataFile.length(); i++) {
                    nameFile.add(dataFile.get(i));
                }
                JSONArray dataFolder = new JSONArray(jsonData.getString("Folder"));
                for (int i = 0; i < dataFolder.length(); i++) {
                    nameFile.add(dataFolder.get(i));
                }

            }
            System.out.println(nameFile);

            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.layout.menu, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        switch (item.getItemId()) {
//            case R.id.delete_item:
//                System.out.print("A");
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }
}
