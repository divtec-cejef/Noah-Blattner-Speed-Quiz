package com.blatnoa.speed_quiz.Controllers;

import android.util.Log;

import com.blatnoa.speed_quiz.Models.Question;

import java.util.ArrayList;

public class QuestionManager {

    private static ArrayList<Question> allQuestions = new ArrayList<>();
    private ArrayList<Question> randomQuestions = new ArrayList<>();

    /**
     * Constructor to instantiate a question manager
     */
    public QuestionManager() {
        randomQuestions = (ArrayList<Question>)allQuestions.clone();
    }

    /**
     * Add question to list of all questions
     * @param question The question to add
     */
    public static void addQuestion(Question question) {
        allQuestions.add(question);
    }

    /**
     * Get a random question. Can only get the same question once
     * @return A random question
     */
    public Question getRandomQuestion() {
        if (anyQuestionsRemaining()) {
            int randIndex = (int) (Math.random() * (randomQuestions.size() - 1));
            Question randomQuestion = randomQuestions.get(randIndex);
            randomQuestions.remove(randomQuestion);
            return randomQuestion;
        } else {
            return null;
        }
    }

    /**
     * Check if there are any random questions remaining
     * @return true if there are any questions remaining
     */
    public boolean anyQuestionsRemaining() {
        return randomQuestions.size() > 0;
    }

    /**
     * Test if the given answer is the correct answer for the given question
     * @param question The asked question
     * @param answer The given answer to the question
     * @return If the answer is correct or not
     */
    public boolean isCorrectAnswer(Question question, int answer) {
        return question.getAnswer() == answer;
    }
}
