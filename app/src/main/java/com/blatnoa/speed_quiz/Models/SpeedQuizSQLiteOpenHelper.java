package com.blatnoa.speed_quiz.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SpeedQuizSQLiteOpenHelper extends SQLiteOpenHelper {
    static String DB_NAME="SpeedQuiz.db";
    static int DB_VERSION=1;

    private static int index = 10;

    public SpeedQuizSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateDatatableQuiz = "CREATE TABLE quiz(idQuiz INTEGER PRIMARY KEY, question TEXT, reponse INTEGER);";
        db.execSQL(sqlCreateDatatableQuiz);
        db.execSQL("INSERT INTO quiz VALUES(1,\"L'anglais est la langue la plus utilisé au monde.\",0)");
        db.execSQL("INSERT INTO quiz VALUES(2,\"La distance Terre-Soleil est d'environ 150 mille kilomètres\",0)");
        db.execSQL("INSERT INTO quiz VALUES(3,\"Le diamètre de la Terre est d'environ 12'000 kilomètres\",1)");
        db.execSQL("INSERT INTO quiz VALUES(4,\"Freddie Mercury s'appelait Farrokh Bulsara lors de sa naissance.\",1)");
        db.execSQL("INSERT INTO quiz VALUES(5,\"John f. Kennedy s'est fait assasiner le 22 novembre 1963.\",1)");
        db.execSQL("INSERT INTO quiz VALUES(6,\"Horizon Forbidden West est développé par Guerilla.\",1)");
        db.execSQL("INSERT INTO quiz VALUES(7,\"Le temps UNIX atteindra sa limite en 2038.\",1)");
        db.execSQL("INSERT INTO quiz VALUES(8,\"L'atterisage lunaire à eu lieu en 1966.\",0)");
        db.execSQL("INSERT INTO quiz VALUES(9,\"AZERTY est un bon layout de clavier.\",0)");
        db.execSQL("INSERT INTO quiz VALUES(10,\"La première guerre mondial à durée de 1914 à 1918.\",1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void addQuestionToQuiz(String questionText, boolean answer) {
        index++;
        getReadableDatabase().execSQL("INSERT INTO quiz VALUES(" + index + ",\"" + questionText + "\"," + (answer ? "1" : "0") + ")");
    }
}
