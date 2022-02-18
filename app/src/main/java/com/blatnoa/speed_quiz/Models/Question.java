package com.blatnoa.speed_quiz.Models;

import android.util.Log;

import com.blatnoa.speed_quiz.Controllers.QuestionManager;

public class Question {

    // Question string
    private String question;
    // Answer to the question
    private int answer;

    /**
     * Constructor to instantiate a question
     * @param question The question String
     * @param answer The answer to the question
     */
    public Question(String question, int answer) {
        // Verify question
        if (!question.isEmpty()) {
            this.question = question;

            // Verify answer
            if (answer == 0 || answer == 1) {
                this.answer = answer;
                QuestionManager.addQuestion(this);
            } else {
                Log.e("Question", "Answer can only be True (1) or False (0)");
            }
        } else {
            Log.e("Question", "Question cannot be empty");
        }

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
