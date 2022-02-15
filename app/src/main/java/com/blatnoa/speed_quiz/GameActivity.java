package com.blatnoa.speed_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private TextView player1Name;
    private TextView player2Name;
    private TextView player1Question;
    private TextView player2Question;
    private Button player1AnswerButton;
    private Button player2AnswerButton;
    private Button stopGameButton;
    private Button replayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        player1Name = findViewById(R.id.game_name_player1);
        player2Name = findViewById(R.id.game_name_player2);
        player1Question = findViewById(R.id.game_question_player1);
        player2Question = findViewById(R.id.game_question_player2);

        player1AnswerButton = findViewById(R.id.game_button_player1);
        player2AnswerButton = findViewById(R.id.game_button_player2);
        stopGameButton = findViewById(R.id.game_button_stop);
        replayButton = findViewById(R.id.game_button_replay);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}