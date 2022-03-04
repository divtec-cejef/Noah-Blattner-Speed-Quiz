package com.blatnoa.speed_quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blatnoa.speed_quiz.Controllers.QuestionManager;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.BaseTransientBottomBar;
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
    private EditText maxRoundsEdit;
    private EditText winRequirementEdit;
    private EditText questionTextEdit;
    private SwitchCompat nightMode;
    private SwitchCompat questionAnswerSwitch;
    private TextInputLayout player1InputLayout;
    private TextInputLayout player2InputLayout;
    private EditText player1Edit;
    private EditText player2Edit;
    ConstraintLayout settingsOverlay;
    ConstraintLayout addQuestionPopup;
    RelativeLayout contextView;

    private final int BASE_WIN_REQUIREMENT = 5;
    private final int BASE_MAX_ROUNDS = 10;

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

        contextView = findViewById(R.id.main_layout);

        nightMode = findViewById(R.id.settings_app_night_mode);
        questionAnswerSwitch = findViewById(R.id.popup_question_answer_switch);

        displayTimeSlider = findViewById(R.id.settings_display_time_slider);
        displayTimeSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return Float.toString(value) + "s";
            }
        });
        winRequirementEdit = findViewById(R.id.settings_win_requirement_edit);
        winRequirementEdit.setText(Integer.toString(BASE_WIN_REQUIREMENT));
        maxRoundsEdit = findViewById(R.id.settings_max_rounds_edit);
        maxRoundsEdit.setText((Integer.toString(BASE_MAX_ROUNDS)));
        questionTextEdit = findViewById(R.id.popup_question_text_edit);

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

                    int winRequirement = Integer.parseInt(winRequirementEdit.getText().toString());
                    if (winRequirement <= 0) {
                        winRequirement = 5;
                    }
                    intent.putExtra("WinRequirement", winRequirement);

                    int maxRounds = Integer.parseInt(maxRoundsEdit.getText().toString());
                    if (maxRounds <= 0) {
                        maxRounds = 5;
                    }
                    intent.putExtra("MaxRounds", maxRounds);
                    startActivity(intent);
                } else {
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

                // Try adding question to the database
                if (qm.addQuestion(questionTextEdit.getText().toString(), questionAnswerSwitch.isChecked())) {
                    Snackbar.make(contextView, R.string.question_added_message, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(contextView, R.string.question_error_message, Snackbar.LENGTH_SHORT).show();
                }
                addQuestionPopup.setVisibility(View.GONE);
            }
        });

        abandonQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTextEdit.setText("");
                questionAnswerSwitch.setChecked(false);
                addQuestionPopup.setVisibility(View.GONE);
            }
        });

        leaveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsOverlay.setVisibility(View.GONE);
            }
        });

        nightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleNightMode(nightMode.isChecked());
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
                toggleNightMode(!getNightModeIsToggled());
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * @return Returns if the night mode is toogled
     */
    public boolean getNightModeIsToggled() {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    /**
     * Toggles the night mode on or off
     * @param state True to turn on night mode, false to turn off.
     */
    private void toggleNightMode(boolean state) {
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


}