package fr.sm.database;


import android.content.ContentValues;
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

    public void deleteOneById(Long id){
        String[] params = {id.toString()};
        String sql = "DELETE FROM contact WHERE id=?";
        this.db.getWritableDatabase().execSQL(sql,params);
    }

    public void persist(Contact entity){
        if (entity.getId() == null){
            this.insert(entity);
        }
        else{
            this.update(entity);
        }
    }

    private ContentValues getContentValuesFromEntity(Contact entity){
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("first_name", entity.getFirst_name());
        values.put("eMail", entity.getEmail());
        return values;
    }

    private void insert(Contact contact){
        // la fonction retourne la valeur du dernier auto-incréùente
        Long id = this.db.getWritableDatabase().insert("contact",null,getContentValuesFromEntity(contact));
        contact.setId(id);
    }

    private void update(Contact contact){
        String[] params = {contact.getId().toString()};
        this.db.getWritableDatabase().update("contact", getContentValuesFromEntity(contact),"id=?", params);
    }
}