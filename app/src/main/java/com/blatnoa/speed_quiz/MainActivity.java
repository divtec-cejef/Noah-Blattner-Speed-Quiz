package com.blatnoa.speed_quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.blatnoa.speed_quiz.Controllers.QuestionManager;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private Button editPlayersButton;
    private Button playButton;
    private Button leaveSettingsButton;
    private Button addQuestionButton;
    private Button confirmQuestionButton;
    private Button abandonQuestionButton;
    private Slider displayTimeSlider;
    private EditText winRequirement;
    private EditText questionText;
    private SwitchCompat questionAnswer;
    private TextInputLayout player1InputLayout;
    private TextInputLayout player2InputLayout;
    private EditText player1Edit;
    private EditText player2Edit;
    ConstraintLayout settingsOverlay;
    ConstraintLayout addQuestionPopup;

    private final int BASE_WIN_REQUIREMENT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        settingsOverlay = findViewById(R.id.settings_overlay);
        settingsOverlay.setVisibility(View.GONE);

        addQuestionPopup = findViewById(R.id.popup_add_question);
        addQuestionPopup.setVisibility(View.GONE);

        questionAnswer = findViewById(R.id.popup_question_answer_switch);

        displayTimeSlider = findViewById(R.id.settings_display_time_slider);
        displayTimeSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return Float.toString(value) + "s";
            }
        });
        winRequirement = findViewById(R.id.settings_win_requirement_edit);
        winRequirement.setText(Integer.toString(BASE_WIN_REQUIREMENT));
        questionText = findViewById(R.id.popup_question_text_edit);

        editPlayersButton = findViewById(R.id.main_button_edit_players);
        playButton = findViewById(R.id.main_button_play);
        leaveSettingsButton = findViewById(R.id.settings_leave_settings);
        addQuestionButton = findViewById(R.id.settings_add_question_button);
        confirmQuestionButton = findViewById(R.id.popup_add_question_confirm_button);
        abandonQuestionButton = findViewById(R.id.popup_exit_button);

        player1InputLayout = findViewById(R.id.input_layout_player1);
        player1InputLayout.setVisibility(View.INVISIBLE);
        player2InputLayout = findViewById(R.id.input_layout_player2);
        player2InputLayout.setVisibility(View.INVISIBLE);

        player1Edit = findViewById(R.id.edit_first_player);
        player2Edit = findViewById(R.id.edit_second_player);
    }

    @Override
    protected void onStart() {
        super.onStart();

        editPlayersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (player1InputLayout.getVisibility() == View.INVISIBLE) {
                   player1InputLayout.setVisibility(View.VISIBLE);
                   player2InputLayout.setVisibility(View.VISIBLE);
               } else {
                   player1InputLayout.setVisibility(View.INVISIBLE);
                   player2InputLayout.setVisibility(View.INVISIBLE);
               }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!player1Edit.getText().toString().isEmpty() && !player2Edit.getText().toString().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("Player1", player1Edit.getText().toString());
                    intent.putExtra("Player2", player2Edit.getText().toString());
                    intent.putExtra("DisplayTime", displayTimeSlider.getValue());
                    intent.putExtra("WinRequirement", Integer.parseInt(winRequirement.getText().toString()));
                    startActivity(intent);
                } else {
                    RelativeLayout contextView = findViewById(R.id.main_layout);
                    Snackbar.make(contextView, getString(R.string.player_error_message), Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestionPopup.setVisibility(View.VISIBLE);
            }
        });

        confirmQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionManager qm = new QuestionManager(MainActivity.this);
                qm.addQuestion(questionText.getText().toString(), questionAnswer.isChecked());
                addQuestionPopup.setVisibility(View.GONE);
            }
        });

        abandonQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionText.setText("");
                questionAnswer.setChecked(false);
                addQuestionPopup.setVisibility(View.GONE);
            }
        });

        leaveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsOverlay.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_action_settings:
                settingsOverlay.setVisibility(View.VISIBLE);
                break;
            case R.id.main_action_night_mode:
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }


}