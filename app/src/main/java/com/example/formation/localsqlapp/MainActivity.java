package com.example.formation.localsqlapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sm.database.DatabaseHandler;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView contactListView;
    private List<Map<String,String>> contactList;
    private Map<String,String> selectedPerson;
    private Integer selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // référence au widget listView sur le layout
        contactListView = findViewById(R.id.contactListView);
        contactListInit();


    }

    private void contactListInit() {
        //récupération de la liste des contacts
        contactList = this.getAllContact();

        ContactArrayAdapter contactAdapter = new ContactArrayAdapter(this,contactList);

        contactListView.setOnItemClickListener(this);
        // définition de l'adapter de notre listView

        contactListView.setAdapter(contactAdapter);
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
        Cursor curseur = db.getReadableDatabase().rawQuery("SELECT first_name,name,email,id FROM contact", null);
        //Instanciation de la liste qui recevra les données

        List<Map<String,String>> listContact = new ArrayList<>();


        // Affectation du résultat de la requete dans la liste
        while (curseur.moveToNext()){
            Map<String,String> contactCol = new HashMap();
            contactCol.put("name", curseur.getString(0));
            contactCol.put("first_name", curseur.getString(1));
            contactCol.put("email", curseur.getString(2));
            contactCol.put("id", curseur.getString(3));
            listContact.add(contactCol);
        }
        return listContact;
    }

    /**
     *
     * Création du menu d'option
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // ajout des entrées du ficheir main_option_menu au
        // menu contextuel des activités
        getMenuInflater().inflate(R.menu.main_option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuItemDelete:
                this.deleteSelectedContact();
                break;
            case R.id.mainMenuOptionEdit:
                break;
        }
        return true;
    }

    private void deleteSelectedContact(){
        int nb = 0;
        if (selectedIndex !=null){
            try {
                // Création de la requete de suppression
                String sql = "DELETE FROM contact WHERE id=?";
                String[] params = {this.selectedPerson.get("id")};

                // instanciation composant à la base de données
                DatabaseHandler db = new DatabaseHandler(this);
                // exécution requete
                db.getWritableDatabase().execSQL(sql, params);

                // 2ème possibilités d'écriture de lka requête
                // possibilité de récupérer le nb d'éléments supprimés
                // db.getWritableDatabase().delete("contact","id=?",params);

                // rafraichissement de la liste
                contactListInit();

            }
            catch(SQLiteException ex){
                Toast.makeText(this, "impossible de supprimer", Toast.LENGTH_SHORT).show();
            }
            // réinitialisation de la liste des contacts

        }else{
            Toast.makeText(this,"Il faut sélectionner un contact",Toast.LENGTH_SHORT).show();
        }

        //return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // obtention des informations globales
        String lsChoix = adapterView.getItemAtPosition(i).toString();
        // Toast.makeText(this, lsChoix, Toast.LENGTH_SHORT).show();

        // obtention des informations depuis un map
        selectedPerson = contactList.get(i);
        Toast.makeText(this, selectedPerson.get("name"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, selectedPerson.get("first_name"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "sélectionné =" + String.valueOf(i +1), Toast.LENGTH_SHORT).show();

        // obtention des informations directement depuis le listView
        lsChoix =  contactList.get(i).get("name");
        Toast.makeText(this, "directement :" + lsChoix, Toast.LENGTH_SHORT).show();
        selectedIndex = i ;
    }
}
