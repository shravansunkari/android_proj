package com.example.storeit.storeit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private DomainsDataSource dataSource;
    private List<Domain> domainList;
    private ArrayAdapter<String> adapter;
    private List<String> list;
    private  Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Getting SQLite database instance
        dataSource = new DomainsDataSource(this);
        dataSource.open();

        domainList = dataSource.getAllDomains();

        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA_MESSAGE");

        //Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText("Welcome "+message);
        list = new ArrayList<String>();

        //Collections.addAll(list, values);
        for(int i=0; i<domainList.size(); i++){
            list.add(domainList.get(i).getDomain());
            //System.out.println("Domain name = "+domainList.get(i).getDomain());
        }
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        ListView myListView = (ListView)findViewById(R.id.listView1);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(this);
        myListView.setOnItemLongClickListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        String domainName = ((TextView) view).getText().toString();

        for(int i=0; i<domainList.size();i++){
            Domain obj = domainList.get(i);
            if(obj!=null && obj.getDomain().equals(domainName)){
                dialog = new Dialog(this);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setTitle("Domain Details");

                //To access elements from different activity
                View content = getLayoutInflater().inflate(R.layout.my_pop_up, null);
                TextView textDomain = (TextView) content.findViewById(R.id.textDomain);
                textDomain.setText("Details : \nDomain : "+obj.getDomain()+"\nUsername : "+obj.getUserName()+"\nPassword : "+obj.getPassword());
                dialog.setContentView(content);
                dialog.show();
                break;
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long arg3) {

        final String domainName = ((TextView) view).getText().toString();
        AlertDialog.Builder alert = new AlertDialog.Builder(
                Home.this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete record");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                list.remove(domainName);
                int len = domainList.size();
                for(int i=0; i<len; i++){
                    if(domainList.get(i).getDomain().equals(domainName)){
                        domainList.remove(i);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();

        return true;
    }

    public void addDomain(View view){
        EditText domain = (EditText)findViewById(R.id.domain);
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);

        if(domain.length()==0){
            Toast.makeText(getApplicationContext(), "Please add domain!",Toast.LENGTH_SHORT).show();
            return;
        }

        Domain obj = dataSource.createDomain(domain.getText().toString(), username.getText().toString(), password.getText().toString());

        //Update listview and domainList with new data
        list.add(obj.getDomain());
        domainList.add(obj);
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Added Item",Toast.LENGTH_SHORT).show();
        domain.setText("");
        username.setText("");
        password.setText("");
        domain.requestFocus();
    }
    public void exitPopUp(View view){
        dialog.dismiss();
    }
}
