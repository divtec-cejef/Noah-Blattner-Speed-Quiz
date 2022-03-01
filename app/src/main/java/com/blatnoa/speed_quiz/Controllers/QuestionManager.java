package com.blatnoa.speed_quiz.Controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blatnoa.speed_quiz.Models.Question;
import com.blatnoa.speed_quiz.Models.SpeedQuizSQLiteOpenHelper;

import java.util.ArrayList;

public class QuestionManager {

    private ArrayList<Question> questionList = new ArrayList<>();
    private SpeedQuizSQLiteOpenHelper helper;

    /**
     * Class constructor to create a new instance of QuestionManager
     * @param context App context
     */
    public QuestionManager(Context context)
    {
        helper = new SpeedQuizSQLiteOpenHelper(context);
        questionList = initQuestionList();
    }

    /**
     * Loads a list from de DB
     * @return A arraylist of questions
     */
    private ArrayList<Question> initQuestionList(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(true,"quiz",new String[]{"idQuiz","question","reponse"},null,null,null,null,"idquiz",null);

        ArrayList<Question> listQuestion = new ArrayList<>();
        while(cursor.moveToNext()){
            listQuestion.add(new Question(cursor));
        }
        cursor.close();
        db.close();
        return listQuestion;
    }

    /**
     * Add a question to the database
     * @param questionText The question's text
     * @param answer The question's answer
     */
    public void addQuestion(String questionText, boolean answer) {
        helper.addQuestionToQuiz(questionText, answer);
    }

    /**
     * Get a random question. Can only get the same question once
     * @return A random question
     */
    public Question getRandomQuestion() {
        if (anyQuestionsRemaining()) {
            int randIndex = (int) (Math.random() * (questionList.size() - 1));
            Question randomQuestion = questionList.get(randIndex);
            questionList.remove(randomQuestion);
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
        return questionList.size() > 0;
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
