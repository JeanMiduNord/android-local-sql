package com.example.formation.localsqlapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sm.database.DatabaseHandler;

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
    private List<Map<String,String>> getAllContact(){
        // instanciation composant à la base de données
        DatabaseHandler db = new DatabaseHandler(this);
        // exécution requete de sélection
        Cursor curseur = db.getReadableDatabase().rawQuery("SELECT first_name,name,email FROM contact", null);
        //Instanciation de la liste qui recevra les données

        List<Map<String,String>> listContact = new ArrayList<>();
        Map<String,String> contactCol = new HashMap();

        // Affectation du résultat de la requete dans la liste
        while (curseur.moveToNext()){
            contactCol.put("name", curseur.getString(0));
            contactCol.put("first_name", curseur.getString(1));
            contactCol.put("email", curseur.getString(2));
            listContact.add(contactCol);
        }
        return listContact;
    }
}
