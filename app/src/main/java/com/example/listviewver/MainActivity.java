package com.example.listviewver;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;


public class MainActivity extends TabActivity {
    ListView list;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpecPhone = tabHost.newTabSpec("PHONE").setIndicator("전화번호");
        tabSpecPhone.setContent(R.id.tabPop1);
        tabHost.addTab(tabSpecPhone);

        TabHost.TabSpec tabSpecGallery = tabHost.newTabSpec("GALLERY").setIndicator("갤러리");
        tabSpecGallery.setContent(R.id.tabPop2);
        tabHost.addTab(tabSpecGallery);

        TabHost.TabSpec tabSpecMy = tabHost.newTabSpec("MY").setIndicator("MY");
        tabSpecMy.setContent(R.id.tabPop3);
        tabHost.addTab(tabSpecMy);

        tabHost.setCurrentTab(0);


        ll = (LinearLayout) findViewById(R.id.tabPop1);

        list = (ListView) findViewById(R.id.listView1);

        LoadContactsAyscn lca = new LoadContactsAyscn();
        lca.execute();

    }

    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<String>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = ProgressDialog.show(MainActivity.this, "Loading Contacts",
                    "Please Wait");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ArrayList<String> contacts = new ArrayList<String>();

            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            while (c.moveToNext()) {

                String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contacts.add(contactName + "\n" + phNumber);

            }
            Collections.sort(contacts);
            c.close();

            return contacts;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);

            pd.cancel();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.text, contacts);

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                    Toast.makeText(MainActivity.this, contacts.get(i), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}