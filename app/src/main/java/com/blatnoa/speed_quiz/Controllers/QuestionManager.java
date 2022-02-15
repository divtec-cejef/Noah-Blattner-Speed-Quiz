package com.blatnoa.speed_quiz.Controllers;

import com.blatnoa.speed_quiz.Models.Question;

import java.util.ArrayList;

public class QuestionManager {

    private static ArrayList<Question> allQuestions = new ArrayList<>();
    private static ArrayList<Question> randomQuestions = new ArrayList<>();

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
    public static Question getRandomQuestion() {
        int randIndex = (int)(Math.random()*(randomQuestions.size()-1));
        Question randomQuestion = randomQuestions.get(randIndex);
        randomQuestions.remove(randIndex);
        return randomQuestion;
    }

}
