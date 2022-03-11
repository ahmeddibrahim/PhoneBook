import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        MongoDB mongoDB = new MongoDB();
        HashMap<String,String> numbers = new HashMap<String,String >(){
            {
                put("01141041181", "Mobile");
                put("0112121212", "Work");
                put("026694659", "Home");
            }};
        Contact contact = new Contact("Ahmed","Ibrahim",numbers,true);
        mongoDB.insertContact(contact);
    }
}
