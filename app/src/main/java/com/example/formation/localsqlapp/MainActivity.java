package com.example.formation.localsqlapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Lancement de l'activité formulaore au click sur un bouton
     * appelé par la classe MainActivity
     * @param view
     */
    public void openAddContact(View view) {
        Intent intention = new Intent(this, AjoutContact.class);
        startActivity(intention);
    }
}
