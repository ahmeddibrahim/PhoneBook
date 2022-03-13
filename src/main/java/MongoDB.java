import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class MongoDB {
    public static MongoClient mongoClient;
    public static MongoCollection<Contact> collection;
    public static MongoDatabase database;
//    private String uri = "mongodb+srv://admin:admin12345@cluster0.6p5q3.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";

    public MongoDB(){

        String uri = "mongodb+srv://admin:admin12345@cluster0.6p5q3.mongodb.net/pls?retryWrites=true&w=majority";
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("Phonebook").withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection("ContactList", Contact.class);

    }
    Contact findNumber(String number){
        for (Contact doc : collection.find()) {
            HashMap<String, String> databaseNumbers = doc.getNumbers();
            if(databaseNumbers.containsKey(number))
                return doc;
        }
        return null;
    }

    ArrayList<Contact> findByName(String name){

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String pattern = ".*"+name+".*";
        int count = 0;
        for (Contact doc : collection.find(regex("fullname",pattern))) {
                System.out.println( " Index = "+count +" : "+ doc);
                contacts.add(doc);
                count++;
        }
        return contacts;
    }
    boolean checkDuplicates(Contact contact)
    {
        // check if the full name is in the database
        if(collection.find(eq("fullname", contact.getFullname())).first()!=null) {
            System.err.println("Duplicate name in the system! Please try again");
            return false;
        }

        // check for duplicates phone numbers
        for (Contact doc : collection.find()) {
            HashMap<String, String> databaseNumbers = doc.getNumbers();// phone numbers in the database represented as hashmap
            for (Map.Entry<String, String> entry : contact.getNumbers().entrySet()) {
                String k = entry.getKey();
            // check the contact being inserted with each number in the database
                if (databaseNumbers.containsKey(k)) {
                    System.err.println("Duplicate number in the system! Please try again");
                    return false;
                }
            }
        }
        return true;
    }
    boolean insertContact(Contact contact) {
        if (!checkDuplicates(contact))
            return false;
        else {
            try {
                InsertOneResult result = collection.insertOne(contact);
                System.out.println("Success! Inserted document id: " + result.getInsertedId());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
                return false;
            }
            return true;
        }
    }

    boolean updateContact(String fullname,Contact newContact){
        if(!checkDuplicates(newContact))
            return false;
        try {
            System.out.println(fullname);
            UpdateResult result= collection.updateOne(eq("fullname",fullname),
                    combine (set("firstname", newContact.getFirstname()),
                            set("lastname", newContact.getLastname()),
                            set("fullname",newContact.getFullname()),
                            set("numbers",newContact.getNumbers()),
                            set("favorite",newContact.isFavorite())));

            System.out.println("Modified document count: " + result.getModifiedCount());
            if(result.getModifiedCount()>0)
                return true;
            else
                return false;
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
    }

    boolean deleteContact(String fullname){
        try{
            DeleteResult result = collection.deleteOne(eq("fullname",fullname));
           if(result.getDeletedCount()>0)
                return true;
           else
               return false;
        }
        catch (MongoException me){
            System.err.println("Unable to delete contact due to an error: " + me);
            return false;
        }
    }
    void displayAllFavorites(){
        try {
            collection.find(eq("favorite", true)).forEach(contact -> System.out.println(contact));
        }
        catch (MongoException me){
            System.err.println("Unable to diplay favorite contacts due to an error: " + me);
        }
    }
    void displayAllContacts(){
        try{
            collection.find().forEach(contact -> System.out.println(contact));
        }
        catch (MongoException me){
            System.err.println("Unable to display contacts due to an error: " + me);
        }

    }
}
