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

import com.example.formation.localsqlapp.model.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sm.database.ContactDAO;
import fr.sm.database.DatabaseHandler;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView contactListView;
    private List<Contact> contactList;
    private Contact selectedPerson;
    private Integer selectedIndex;
    private final String LIFE_CYCLE = "cycle de vie";
    private ContactDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LIFE_CYCLE, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instanciation de la connexion a la BDD
        dao = new ContactDAO(new DatabaseHandler(this));

        // référence au widget listView sur le layout
        contactListView = findViewById(R.id.contactListView);
        contactListInit();

       // test de la recherche d'un contact par DAO
       // testDAO();

        // récupération des données sauvegardées
        //
        //  cas où on passe du mode
    /*    if (savedInstanceState != null){
            selectedIndex = savedInstanceState.getInt("selectedIndex");
            if (selectedIndex !=null) {

                selectedPerson = contactList.get(selectedIndex);
                contactListView.requestFocusFromTouch();
                contactListView.setSelection(selectedIndex);
            }
        }*/
    }

    private void testDAO() {
        Contact contact = dao.findOneById(1);
        if (contact.getName()!=null){
            Log.i("DAO",contact.getName());
        }
    }

    private void contactListInit() {
        //récupération de la liste des contacts ancienne méthode
        contactList = dao.findAll();

        ContactArrayAdapter contactAdapter = new ContactArrayAdapter(this, contactList);

        contactListView.setOnItemClickListener(this);
        // définition de l'adapter de notre listView

        contactListView.setAdapter(contactAdapter);
    }

    /**
     * Lancement de l'activité formulaore au click sur un bouton
     * appelé par la classe MainActivity
     *
     * @param view
     */
    public void openAddContact(View view) {
        Intent intention = new Intent(this, AjoutContact.class);
        startActivity(intention);
    }

    private List<Map<String, String>> getAllContact() {
        // instanciation composant à la base de données
        DatabaseHandler db = new DatabaseHandler(this);
        // exécution requete de sélection
        Cursor curseur = db.getReadableDatabase().rawQuery("SELECT first_name,name,email,id FROM contact", null);
        //Instanciation de la liste qui recevra les données

        List<Map<String, String>> listContact = new ArrayList<>();


        // Affectation du résultat de la requete dans la liste
        while (curseur.moveToNext()) {
            Map<String, String> contactCol = new HashMap();
            contactCol.put("name", curseur.getString(0));
            contactCol.put("first_name", curseur.getString(1));
            contactCol.put("email", curseur.getString(2));
            contactCol.put("id", curseur.getString(3));
            listContact.add(contactCol);
        }
        return listContact;
    }

    /**
     * Création du menu d'option
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // ajout des entrées du ficheir main_option_menu au
        // menu contextuel des activités
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuItemDelete:
                this.deleteSelectedContact();
                selectedIndex = null;
                break;
            case R.id.mainMenuOptionEdit:
                Intent intention = new Intent(this, AjoutContact.class);
                intention.putExtra("id", String.valueOf(selectedPerson.getId()));
                intention.putExtra("first_name", selectedPerson.getFirst_name());
                intention.putExtra("name", selectedPerson.getName());
                intention.putExtra("email", selectedPerson.getEmail());
                startActivityForResult(intention, 1);
                selectedIndex = null;
                break;
        }
        return true;
    }

    /**
     * @param requestCode = requestCode de l'appelant (ici = 1)
     * @param resultCode
     * @param data        : donnée envoyée par le formulaire appelée
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Toast.makeText(this, "Mise à jour effectuée", Toast.LENGTH_LONG).show();
            contactListInit();
        }
    }

    private void deleteSelectedContact() {
        int nb = 0;
        if (selectedIndex != null) {
            try {
                // Création de la requete de suppression
                String sql = "DELETE FROM contact WHERE id=?";
                String[] params = {String.valueOf(selectedPerson.getId())};

                // instanciation composant à la base de données
                DatabaseHandler db = new DatabaseHandler(this);
                // exécution requete
                db.getWritableDatabase().execSQL(sql, params);

                // 2ème possibilités d'écriture de lka requête
                // possibilité de récupérer le nb d'éléments supprimés
                // db.getWritableDatabase().delete("contact","id=?",params);

                // rafraichissement de la liste
                contactListInit();

            } catch (SQLiteException ex) {
                Toast.makeText(this, "impossible de supprimer", Toast.LENGTH_SHORT).show();
            }
            // réinitialisation de la liste des contacts

        } else {
            Toast.makeText(this, "Il faut sélectionner un contact", Toast.LENGTH_SHORT).show();
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
        //       Toast.makeText(this, selectedPerson.get("name"), Toast.LENGTH_SHORT).show();
        //      Toast.makeText(this, "sélectionné =" + String.valueOf(i +1), Toast.LENGTH_SHORT).show();

        // obtention des informations directement depuis le listView
        lsChoix = contactList.get(i).getName();
        //     Toast.makeText(this, "directement :" + lsChoix, Toast.LENGTH_SHORT).show();
        selectedIndex = i;
    }

    /**
     * Persistance des données avant destruction de l'activité
     *
     * Sauvegarde l'instance, les données pour pouvior réinitialiser l'état du formumaire au retour d'une
     * autre activité
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedIndex != null){
            outState.putInt("selectedIndex", this.selectedIndex);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LIFE_CYCLE, "onstart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LIFE_CYCLE, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LIFE_CYCLE, "onResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LIFE_CYCLE, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LIFE_CYCLE, "onStop");
    }

}