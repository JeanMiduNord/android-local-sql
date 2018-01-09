package com.example.formation.localsqlapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sm.database.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    private ListView contactListView;
    private List<Map<String,String>> contactList;
    private int toto, titi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // référence au widget listView sur le layout
        contactListView = findViewById(R.id.contactListView);

        //récupération de la liste des contacts
        contactList = this.getAllContact();

        ContactArrayAdapter contactAdapter = new ContactArrayAdapter(this,contactList);

        // définition de l'adapter de notre listView

        toto = 1;
        titi = 2;
        try {
            contactListView.setAdapter(contactAdapter);
        } catch (Exception ex){
            Log.d("DEBUG", ex.getMessage());
        }

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


        // Affectation du résultat de la requete dans la liste
        while (curseur.moveToNext()){
            Map<String,String> contactCol = new HashMap();
            contactCol.put("name", curseur.getString(0));
            contactCol.put("first_name", curseur.getString(1));
            contactCol.put("email", curseur.getString(2));
            listContact.add(contactCol);
        }
        return listContact;
    }
}
