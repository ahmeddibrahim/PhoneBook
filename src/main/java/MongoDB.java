import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
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
    boolean checkDuplicates(Contact contact)
    {
        // check if the full name is in the database
        if(collection.find(eq("fullname", contact.getFullname())).first()!=null) {
            System.out.println("Duplicate name in the system! Please try again");
            return false;
        }

        // check for duplicates phone numbers
        for (Contact doc : collection.find()) {
            HashMap<String, String> databaseNumbers = doc.getNumbers();// phone numbers in the database represented as hashmap
            for (Map.Entry<String, String> entry : contact.getNumbers().entrySet()) {
                String k = entry.getKey();
            // check the contact being inserted with each number in the database
                if (databaseNumbers.containsKey(k)) {
                    System.out.println("Duplicate number in the system! Please try again");
                    return false;
                }
            }
        }
        return true;
    }
    void insertContact(Contact contact) {
        if (!checkDuplicates(contact))
            return;
        else {
            try {
                InsertOneResult result = collection.insertOne(contact);
                System.out.println("Success! Inserted document id: " + result.getInsertedId());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }

        }
    }

}
