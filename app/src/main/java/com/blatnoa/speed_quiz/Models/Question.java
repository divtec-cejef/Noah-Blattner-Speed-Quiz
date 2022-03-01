package com.blatnoa.speed_quiz.Models;

import android.database.Cursor;
import android.util.Log;

import com.blatnoa.speed_quiz.Controllers.QuestionManager;

public class Question {

    // Question string
    private String question;
    // Answer to the question
    private int answer;

    /**
     * Constructor to instantiate a question
     * @param cursor The cursor of the question
     */
    public Question(Cursor cursor){
        question = cursor.getString(cursor.getColumnIndex("question"));
        answer = cursor.getInt(cursor.getColumnIndex("reponse"));
    }

    /**
     * @return The question String
     */
    public String getQuestion() {return question;}

    /**
     * @return The answer to the question
     */
    public int getAnswer() {return answer;}
}
