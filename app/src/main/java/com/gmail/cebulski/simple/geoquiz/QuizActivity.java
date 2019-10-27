package com.gmail.cebulski.simple.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button cheatButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionTextView;

    // for debugging purposes
    private static final String TAG = "QuizActivity";
    // for saving data across rotation
    private static final String KEY_INDEX = "index";
    private static final String ANSWERED_KEY = "key";

    private static final int REQUEST_CODE_CHEAT = 0;


    private Question[] questionBank = new Question[] {
            new Question(R.string.questionAustralia, true),
            new Question(R.string.questionOceans, true),
            new Question(R.string.questionMidEast, false),
            new Question(R.string.questionAfrica, false),
            new Question(R.string.questionAmericas, true),
            new Question(R.string.questionAsia, true),
    };

    private int questions = questionBank.length;
    private int correctAnswers = 0, answers = 0;
    private int currentIndex = 0;

    private boolean isCheater;

    private boolean[] questionsAnswered = new boolean[questionBank.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        trueButton = (Button) findViewById(R.id.trueButton);
        falseButton = (Button) findViewById(R.id.falseButton);
        cheatButton = (Button) findViewById(R.id.cheatButton);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        prevButton = (ImageButton) findViewById(R.id.prevButton);
        questionTextView = (TextView) findViewById(R.id.questionTextView);


        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        }); // end of trueButton On Click Listener

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        }); // end of falseButton On Click Listener

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questionBank.length;
                isCheater = false;
                updateQuestion();
            }
        }); // end of nextButton On Click Listener

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentIndex == 0) {
                    currentIndex = questionBank.length - 1;
                }
                else {
                    currentIndex = (currentIndex - 1) % questionBank.length;
                }
                updateQuestion();
            }
        });

        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });


        // restore data
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX);
            questionsAnswered = savedInstanceState.getBooleanArray(ANSWERED_KEY);
        }

        updateQuestion();

    } // end of onCreate Method


    // handling a result by holding the value that CheatActivity is passing back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if(data == null) {
                return;
            }
            isCheater = CheatActivity.wasAnswerShown(data);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSavedInstanceState");
        savedInstanceState.putInt(KEY_INDEX, currentIndex);
        savedInstanceState.putBooleanArray(ANSWERED_KEY, questionsAnswered);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


    private void updateQuestion() {
        trueButton.setEnabled(!questionsAnswered[currentIndex]);
        falseButton.setEnabled(!questionsAnswered[currentIndex]);

        int question = questionBank[currentIndex].getQuestion();
        questionTextView.setText(question);
    }


    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();

        int messageResId = 0;

        if (isCheater) {
            messageResId = R.string.judgment_toast;
        }
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                correctAnswers++;
                answers++;
            } else {
                messageResId = R.string.incorrect_toast;
                answers++;
            }
        }
        questionsAnswered[currentIndex] = true;
        trueButton.setEnabled(false);
        falseButton.setEnabled(false);

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        if (answers == questions) {
            calcScore();
        }
    }


    private void calcScore() {
        int percent = (correctAnswers * 100) / 6;
        Toast.makeText(this, Integer.toString(percent)+"%", Toast.LENGTH_LONG).show();
    }

}
