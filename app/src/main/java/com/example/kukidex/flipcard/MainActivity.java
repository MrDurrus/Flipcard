package com.example.kukidex.flipcard;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CREATE_CARD = 1;
    int currentCardDisplayedIndex = 0;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();


        if (allFlashcards != null) {
            setViewCard(currentCardDisplayedIndex = allFlashcards.size() > 0? 0:-1);
        }

        findViewById(R.id.next_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex++;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = allFlashcards.isEmpty()? -1 : 0;
                }

                // set the question and answer TextViews with data from the database
                setViewCard(currentCardDisplayedIndex);
            }
        });
        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCardDisplayedIndex >= 0) {
                    flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());
                    allFlashcards = flashcardDatabase.getAllCards();
                    if (allFlashcards.size() > 0 && currentCardDisplayedIndex == 0)
                        currentCardDisplayedIndex++;
                    setViewCard(--currentCardDisplayedIndex);
                }
            }
        });
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

                 flashcardDatabase.insertCard(new Flashcard(question, answer));
                 allFlashcards = flashcardDatabase.getAllCards();
                 currentCardDisplayedIndex = allFlashcards.size() - 1;

             }

        }
    }

    void setViewCard(int cardIndex){
        if(cardIndex < 0){
            ((TextView)findViewById(R.id.flashcard_question)).setText("No questions available");
            ((TextView)findViewById(R.id.flashcard_answer)).setText("N/A");
        }
        else{
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(cardIndex).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(cardIndex).getAnswer());
        }
        findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
        findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
    }
}
