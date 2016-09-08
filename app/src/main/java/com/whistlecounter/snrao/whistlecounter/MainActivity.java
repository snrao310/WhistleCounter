package com.whistlecounter.snrao.whistlecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= (EditText) findViewById(R.id.numberOfWhistles);
    }


    public void start(View view){
        Intent intent=new Intent(MainActivity.this,CountingActivity.class);
        int i=Integer.parseInt(editText.getText().toString());
        Bundle bundle=new Bundle();
        bundle.putInt("value",i);
        intent.putExtras(bundle);
        startActivity(intent);


    }
}
