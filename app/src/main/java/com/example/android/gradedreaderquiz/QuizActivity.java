package com.example.android.gradedreaderquiz;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends com.example.android.gradedreaderquiz.MainActivity {

    private static final String USER_NAME_INPUT = "name";
    private static final String STATE_SUBMITTED = "submitted";

    private int score = 0;
    private String name = "";
    private Button shareButton;
    private Button submitButton;
    private Button restartButton;
    private View scoreMessageCard;
    private boolean submitted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        name = getIntent().getStringExtra(USER_NAME_INPUT);

        //add onSubmit button click listener
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuiz(v);
            }
        });

        //add share results button click listener
        shareButton = (Button) findViewById(R.id.share_results_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareResults(v);
            }
        });

        //restart button defined
        restartButton = (Button) findViewById(R.id.restart_button);

        //scoreMessage Card defined

        scoreMessageCard = (View) findViewById(R.id.displayScoreCard);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current score state
        savedInstanceState.putBoolean(STATE_SUBMITTED, submitted);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        submitted = savedInstanceState.getBoolean(STATE_SUBMITTED);

        if (submitted) { //results have already submitted
            onSubmit();
        }
    }

    public void submitQuiz(View view) {
        onSubmit();
        submitted = true;
    }

    private void onSubmit() {
        disableButton(R.id.submit_button);
        calculateScore();
        Toast.makeText(this, createScoreToastMessage(), Toast.LENGTH_LONG).show();
        String scoreMessage = createScoreMessage();
        displayMessage(scoreMessage);
        shareButton.setVisibility(View.VISIBLE);
        restartButton.setVisibility(View.VISIBLE);
        scoreMessageCard.setVisibility(View.VISIBLE);

    }

    private void calculateScore() {
        score = 0;
        score += answerCheckBox(R.id.question_1_answer_1, false);
        score += answerCheckBox(R.id.question_1_answer_2, true);
        score += answerCheckBox(R.id.question_1_answer_3, true);
        score += answerCheckBox(R.id.question_1_answer_4, false);
        score += answerCheckBox(R.id.question_1_answer_5, true);
        score += answerCheckBox(R.id.question_1_answer_6, true);
        score += answerRadioGroup(R.id.radio_group_question_2, R.id.question_2_answer_3_correct);
        score += answerRadioGroup(R.id.radio_group_question_3, R.id.question_3_answer_4_correct);
        score += answerRadioGroup(R.id.radio_group_question_4, R.id.question_4_answer_1_correct);
        score += answerRadioGroup(R.id.radio_group_question_5, R.id.question_5_answer_2_correct);
        score += answerRadioGroup(R.id.radio_group_question_6, R.id.question_6_answer_2_correct);
        score += answerRadioGroup(R.id.radio_group_question_7, R.id.question_7_answer_3_correct);

    }

    public void resetQuiz(View view) {
        score = 0;
        finish();
        startActivity(getIntent());
    }

    /**
     * disables a button
     */
    private void disableButton(int buttonID) {
        Button button = findViewById(buttonID);
        button.setEnabled(false);
    }

    private int answerCheckBox(int checkBoxID, boolean isCorrect) {
        CheckBox hasAnswerCheckbox = findViewById(checkBoxID);
        int i = 0;
        if (hasAnswerCheckbox.isChecked() && isCorrect) {
            i = 1;
        }
        if (isCorrect) {
            hasAnswerCheckbox.setTextColor(Color.GREEN);
        }
        hasAnswerCheckbox.setEnabled(false);
        return i;
    }

    /**
     * This method checks status radio group.
     *
     * @return one point if the correct radio button is checked
     */
    private int answerRadioGroup(int radioGroupID, int correctRadioButtonID) {
        RadioGroup hasAnswerRadioGroup = findViewById(radioGroupID);
        int i = 0;
        int checkedAnswerRadioGroup = hasAnswerRadioGroup.getCheckedRadioButtonId();
        RadioButton correctRadioButton = null;
        for (int k = 0; k < hasAnswerRadioGroup.getChildCount(); k++) {
            // disable radio group
            (hasAnswerRadioGroup.getChildAt(k)).setEnabled(false);
            if ((hasAnswerRadioGroup.getChildAt(k)).getId() == correctRadioButtonID) {
                correctRadioButton = (RadioButton) hasAnswerRadioGroup.getChildAt(k);
            }
        }
        if (checkedAnswerRadioGroup != 0 && checkedAnswerRadioGroup == correctRadioButtonID) {
            i = 1;
        }
        correctRadioButton.setTextColor(Color.GREEN);
        return i;
    }

    /**
     * creates score toast message.
     *
     * @return text summary
     */
    private String createScoreToastMessage() {
        Resources res = getResources();
        String scoreMessage = res.getString(R.string.score_message, name, score);
        return scoreMessage;
    }

    /**
     * creates score  message.
     *
     * @return text summary
     */
    private String createScoreMessage() {
        Resources res = getResources();
        String scoreMessage = res.getString(R.string.score_message, name, score);
        if (score <= 3) {
            scoreMessage += res.getString(R.string.message_less_than_4);
        } else if (score <= 7) {
            scoreMessage += res.getString(R.string.message_less_than_8);
        } else {
            scoreMessage += res.getString(R.string.message_more_than_7);
        }
        scoreMessage += res.getString(R.string.message_correct_answers);
        return scoreMessage;
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String scoreMessage) {
        TextView textView = findViewById(R.id.textViewDisplayScore);
        textView.setText(scoreMessage);
    }

    public void shareResults(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message, name, score));
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }
}