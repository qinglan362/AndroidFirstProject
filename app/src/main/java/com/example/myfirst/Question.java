package com.example.myfirst;

import androidx.annotation.StringRes;
import lombok.Data;

@Data
public class Question {

    @StringRes
    private int textResId;
    private boolean answer;

    public Question(@StringRes int textResId, boolean answer) {
        this.textResId = textResId;
        this.answer = answer;
    }
}
