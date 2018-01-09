package com.example.formation.localsqlapp;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.sm.database.DatabaseHandler;

public class AjoutContact extends AppCompatActivity {
    EditText edtNom;
    EditText prenom;
    EditText email;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_contact);

       // récupération des données du formulaire appelant

        Intent intention = getIntent();
        id = intention.getStringExtra("id");
        String name = intention.getStringExtra("name");
        String first_name = intention.getStringExtra("first_name");
        String emailName = intention.getStringExtra("email");

        // Liaison entre les données du formulaire
        edtNom = (EditText) findViewById(R.id.edtNom);
        prenom = (EditText) findViewById(R.id.edtPrenom);
        email = (EditText) findViewById(R.id.edtMail);

        // Affichage des informations du formulaire appelant
        edtNom.setText(name);
        prenom.setText(first_name);
        email.setText(emailName);

        /**
         *  donne la possibilité d'un retour arrière en affichant une flèche de retour dans la Action bar
         */
        ActionBar actionBar = getActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public void valideMail(View v){
        Button clickedButton = (Button) v;
        long llNum = 0;
        String lsMessage = "";

        // instanciation de la connexion
        DatabaseHandler db = new DatabaseHandler(this);

        // définition des données à insérer dans la table contact
        // utilisation d'un hashMap
        ContentValues hmValeurs = new ContentValues();
        hmValeurs.put("first_name",edtNom.getText().toString());
        hmValeurs.put("name", prenom.getText().toString());
        hmValeurs.put("email",email.getText().toString() );

        // --- Insertion
        try {
            if (id ==null) {
                llNum = db.getWritableDatabase().insert("contact", null, hmValeurs);
            }else{
                String[] params = {id};
                llNum = db.getWritableDatabase().update("contact",  hmValeurs, "id=?",params);
                setResult(RESULT_OK);
                finish();
            }
            lsMessage = String.valueOf(llNum) + " enregistrement effectué";
            Toast.makeText(this,lsMessage, Toast.LENGTH_LONG).show();
            ((EditText) findViewById(R.id.edtNom)).setText("");
            ((EditText) findViewById(R.id.edtPrenom)).setText("");
            ((EditText) findViewById(R.id.edtMail)).setText("");
        }
        catch(SQLiteException ex) {
            Log.e("Sql Exception", ex.getMessage());
            lsMessage = "Insertion ratée";
            Toast.makeText(this,lsMessage, Toast.LENGTH_LONG).show();

        }

    }
}
