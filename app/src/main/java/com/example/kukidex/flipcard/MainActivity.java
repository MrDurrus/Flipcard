package com.example.kukidex.flipcard;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CREATE_CARD = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //hide question, show answer
        findViewById(R.id.flashcard_question).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                        findViewById(R.id.flashcard_answer).setVisibility(View.VISIBLE);

                    }
                }
        );

        //hide answer, show question
        findViewById(R.id.flashcard_answer).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
                        findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
                    }
                }
        );

        //opens create card activity whe clicked
        findViewById(R.id.create_card_btn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent createCard = new Intent(MainActivity.this , CreateCardActivity.class);
                        startActivityForResult(createCard , REQUEST_CREATE_CARD);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUEST_CREATE_CARD ){
             if(resultCode == RESULT_OK){
                // get question and answer if they are provided through intent
                String question = data.getStringExtra("question");
                String answer = data.getStringExtra("answer");
                Log.i(TAG, "onCreate: question: " + question);
                Log.i(TAG, "onCreate: answer: " + answer);
                ((TextView)findViewById(R.id.flashcard_question)).setText(question);
                ((TextView)findViewById(R.id.flashcard_answer)).setText(answer);
            }

        }
    }
}
