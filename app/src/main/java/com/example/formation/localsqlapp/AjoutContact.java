package com.example.formation.localsqlapp;

import android.app.ActionBar;
import android.content.ContentValues;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_contact);

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
        // récupération des données saisies dans activité ajout_contact
        EditText edtNom = (EditText) findViewById(R.id.edtNom);
        String prenom = ((EditText) findViewById(R.id.edtPrenom)).getText().toString();
        String email = ((EditText) findViewById(R.id.edtMail)).getText().toString();

        // instanciation de la connexion
        DatabaseHandler db = new DatabaseHandler(this);

        // définition des données à insérer dans la table contact
        // utilisation d'un hashMap
        ContentValues hmValeurs = new ContentValues();
        hmValeurs.put("first_name",edtNom.getText().toString());
        hmValeurs.put("name", prenom);
        hmValeurs.put("email",email );

        // --- Insertion
        try {
            llNum = db.getWritableDatabase().insert("contact",null,hmValeurs);
            lsMessage = String.valueOf(llNum) + " enregistrement ajouté";
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
