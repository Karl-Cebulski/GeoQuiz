package com.gmail.cebulski.simple.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private boolean answerIsTrue;

    private TextView answerTextView;
    private Button showAnswerButton;

    private static final String EXTRA_ANSWER_IS_TRUE = "android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "android.geoquiz.answer_shown";

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        showAnswerButton = (Button) findViewById(R.id.showAnswerButton);
        answerTextView = (TextView) findViewById(R.id.answerTextView);

        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answerIsTrue) {
                    answerTextView.setText(R.string.trueButton);
                }
                else {
                    answerTextView.setText(R.string.falseButton);
                }
                setAnswerShownResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = showAnswerButton.getWidth() / 2;
                    int cy = showAnswerButton.getHeight() / 2;
                    float radius = showAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(showAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            showAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    showAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        }); // end of On Click Listener
    }


    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
