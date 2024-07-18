package com.example.myfirst;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button prevButton;
    private Button cheatButton;
    private TextView questionTextView;

    private  List<Question> questionBank = new ArrayList<>();
    private int currentIndex = 0;
    private List<Integer> isEnable = new ArrayList<>();

    private int rightAnswerCount = 0;

     private static final int  REQUEST_CODE_CHEAT = 0;

     private boolean isCheater=false;

     private int cheatCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        addQuestion();

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        cheatButton = findViewById(R.id.cheat_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(v -> {
            // Code here executes on main thread after user presses button
                 checkAnswer(true);
             });
        falseButton.setOnClickListener(v -> {
            // Code here executes on main thread after user presses button
                    checkAnswer(false);
          });
        nextButton.setOnClickListener(v -> {
            // Code here executes on main thread after user presses button
            currentIndex = (currentIndex + 1) % questionBank.size();
            updateQuestion();
        });
        cheatButton.setOnClickListener(v -> {
            // Code here executes on main thread after user presses button
            Intent intent = CheatActivity.newIntent(MainActivity.this, questionBank.get(currentIndex).isAnswer());
            startActivityForResult(intent, REQUEST_CODE_CHEAT);
        });
        prevButton.setOnClickListener(v -> {
            // Code here executes on main thread after user presses button
            currentIndex = (currentIndex - 1) % questionBank.size();
            if (currentIndex < 0) {
                currentIndex = questionBank.size() - 1;
            }
            updateQuestion();
        });
        questionTextView.setOnClickListener(v -> {
            // Code here executes on main thread after user presses button
            currentIndex = (currentIndex + 1) % questionBank.size();
            updateQuestion();
        });

        updateQuestion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateQuestion() {
        int questionTextResId = questionBank.get(currentIndex).getTextResId();
        questionTextView.setText(questionTextResId);
        if (isEnable.contains(currentIndex)) {
            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
        } else {
            trueButton.setEnabled(true);
            falseButton.setEnabled(true);
        }
        if (cheatCount==3){
            cheatButton.setEnabled(false);
        }
        cheatButton.setText("Cheat (" + (3-cheatCount) + ")");
    }

    private void addQuestion() {
        questionBank.add(new Question(R.string.question_oceans, true));
        questionBank.add(new Question(R.string.question_mideast, false));
        questionBank.add(new Question(R.string.question_africa, false));
        questionBank.add(new Question(R.string.question_americas, true));
        questionBank.add(new Question(R.string.question_asia, true));
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = questionBank.get(currentIndex).isAnswer();
        int messageResId = 0;
        isEnable.add(currentIndex);

        if (isCheater) {
            messageResId = R.string.judgment_toast;
        } else if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            rightAnswerCount++;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        currentIndex = (currentIndex + 1) % questionBank.size();
        isCheater = false;

        updateQuestion();
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        if (isEnable.size() == questionBank.size()) {
            Toast.makeText(this, "Right answer count: " + rightAnswerCount, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
          else{
                isCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
                if (isCheater) {
                    cheatCount++;
                    updateQuestion();
                }
            }
        }
    }
}
