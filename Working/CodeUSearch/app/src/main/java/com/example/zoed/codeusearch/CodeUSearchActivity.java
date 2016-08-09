package com.example.zoed.codeusearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.widget.*;

public class CodeUSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load controls laid out in activity layout file
        setContentView(R.layout.activity_code_usearch);
        // access button control using its ID and mapping to button object
        Button b = (Button)this.findViewById(R.id.click_btn);
        // associating a click event with the button control and listen for click
        b.setOnClickListener(new Button.onClickListener(){
            // access textview control from activity layout file using ID, response and mapping
            TextView resp = (TextView) findViewById(R.id.response);
            // create string that returns result
            EditText searchTerm = (EditText) findViewById(R.id.user_name);
            String term = "You entered: " + searchTerm.getText().toString() + "."; // search term in string form
            resp.setText(term);
        });
    }
}
