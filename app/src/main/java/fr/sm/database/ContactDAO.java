package fr.sm.database;


import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.example.formation.localsqlapp.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private DatabaseHandler db;

    public ContactDAO(DatabaseHandler db) {
        this.db = db;
    }

    /**
     * Récupération d'un contact en fonction de sa clé primaire
     * @param id
     * @return
     */
    public Contact findOneById(long id) throws SQLiteException {
        // exécution de la requête
        String sql = "Select id, name, first_name,email from contact where id=?";
        String[] params = {String.valueOf(id)};
        Cursor cursor = this.db.getReadableDatabase()
                .rawQuery(sql, params);
        // Instanciation d'un contact
        Contact contact = new Contact();
        // Hydratation du contact
        if (cursor.moveToNext()) {
            hydrateContact(cursor);
        }
        cursor.close();
        return contact;
    }

    public List<Contact> findAll() throws SQLiteException{
        // Instanciation de la liste de contacts
        List<Contact> contactList = new ArrayList<>();
         // exécution de la requête sql

        String sql = "Select id, name, first_name,email from contact";
        Cursor cursor = this.db.getReadableDatabase().rawQuery(sql,null);

        //boucle sur le curseur
        while (cursor.moveToNext()){
            contactList.add(hydrateContact(cursor));
        }

        cursor.close();
        return contactList;
    }

    private Contact hydrateContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getLong(0));
        contact.setName(cursor.getString(1));
        contact.setFirst_name(cursor.getString(2));
        contact.setEmail(cursor.getString(3));
        return contact;
    }

}