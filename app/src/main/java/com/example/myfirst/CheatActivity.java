package com.example.myfirst;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class CheatActivity extends AppCompatActivity {

     private static final String EXTRA_ANSWER_IS_TRUE = "answer";
     public static final String  EXTRA_ANSWER_SHOWN = "false";

     private Button showAnswerButton;
     private TextView answerTextView;
     private boolean answerIsTrue=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cheat);
        answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        showAnswerButton = findViewById(R.id.show_answer_button);

        answerTextView = findViewById(R.id.answer_text_view);

        showAnswerButton.setOnClickListener(v -> {
            int answerText;
            if (answerIsTrue) {
                answerText = R.string.true_button;
            } else {
                answerText = R.string.false_button;
            }
            answerTextView.setText(answerText);
           setAnswerShownResult(true);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }
     private void setAnswerShownResult(boolean isAnswerShown) {
         Intent data = new Intent();
         data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
         setResult(RESULT_OK, data);
     }

}