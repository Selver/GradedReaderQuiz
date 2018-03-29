package com.example.android.gradedreaderquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.gradedreaderquiz.QuizActivity;
import com.example.android.gradedreaderquiz.R;

public class MainActivity extends AppCompatActivity {
    public static final String USER_NAME_INPUT = "name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find button to start quiz
        Button buttonStart = findViewById(R.id.start_button);

        //Assign a listener to your button
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = getName(R.id.edit_text_name);
                if (name != null && name.length() > 0) {
                    //Start quiz activity
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra(USER_NAME_INPUT, name);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Extracts the name from edit text if it exists.
     */
    private String getName(int editTextID) {
        EditText userNameInput = findViewById(editTextID);
        if (userNameInput.getText().length() > 0) {
            return userNameInput.getText().toString();
        } else {
            Toast.makeText(this, R.string.user_name_empty_warning, Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}





