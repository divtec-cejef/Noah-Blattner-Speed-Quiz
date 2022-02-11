package com.blatnoa.speed_quiz;

import androidx.appcompat.app.AppCompatActivity;
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

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private Button editPlayersButton;
    private Button playButton;
    private TextInputLayout player1InputLayout;
    private TextInputLayout player2InputLayout;
    private EditText player1Edit;
    private EditText player2Edit;

    ConstraintLayout settingsOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        settingsOverlay = findViewById(R.id.settings_overlay);
        settingsOverlay.setVisibility(View.GONE);

        editPlayersButton = findViewById(R.id.main_button_edit_players);
        playButton = findViewById(R.id.main_button_play);

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

                }
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