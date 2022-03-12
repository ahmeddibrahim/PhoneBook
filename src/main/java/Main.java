import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static String getType(Scanner in){
        System.out.println("Enter the type of the phone number if left empty, it will be assigned as Mobile");
        String type = in.nextLine();
        if(type.isEmpty())
            type="Mobile";

        return type;
    }
    public static void main(String[] args) {
        MongoDB mongoDB = new MongoDB();
        Scanner in = new Scanner(System.in);
        System.out.println("PhoneBook, press enter to start");

        String inLine = in.nextLine();
        while(!inLine.equals("q") ){
            System.out.println(" Press 0 to show the menu or press q to quit ");
            inLine = in.nextLine();

            switch (inLine) {
                case "0": {
                    System.out.println(" To insert Contact press : 1\n To update Contact press: 2\n To delete contact press: 3\n To view all contacts press: 4\n " +
                            "To view favorite contacts press: 5\n To search For contact press: 6");
                    break;
                }
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                                                    //Insert Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "1":
                    System.out.println("Enter Firstname");
                    String firstname = in.nextLine();
                    System.out.println("Enter Lastname");
                    String lastname = in.nextLine();

                    HashMap<String,String> numbers = new HashMap<String,String>();
                    System.out.println("Enter a Phone number");
                    String number = in.nextLine();// should be validated ...

                    String type = getType(in);

                    numbers.put(number,type);// add number to the hashmap

                    System.out.println("Do you want to add more phone numbers? Press y for YES ");
                    String input = in.nextLine();
                    while(input.equals("y")){
                        System.out.println("Enter another Phone number");
                        number = in.nextLine();// should be validated ...

                        while(numbers.containsKey(number))// check in the hashmap if it's duplicated
                        {
                            System.err.println("Number is already available, please Enter another phone number again ");
                            number = in.nextLine();
                        }

                        type= getType(in);
                        while(numbers.containsValue(type)) // check in the hashmap if the type is duplicated
                        {
                            System.err.println("type: " +type +" was used before, please enter another type");
                            type = in.nextLine();
                            if(type.isEmpty())
                                type="Mobile";
                        }

                        numbers.put(number,type);
                        System.out.println("Do you want to add more phone numbers? Press y for YES ");
                        input = in.nextLine();
                    }
                    System.out.println("Do you want to add "+ firstname + " as favorite? press y for YES and n for NO");
                    input=in.nextLine();
                    boolean favorite = false;
                    if(input.equals("y"))
                        favorite = true;
                    Contact contact = new Contact(firstname,lastname,numbers,favorite);
                    mongoDB.insertContact(contact);
                    System.out.println("Contact Inserted!");
                    System.out.println(contact);
                    break;
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                                                    //Update Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "2":

                    break;
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                                                    //Delete Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "3":

                    break;
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                                                    //View Contacts
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "4":

                    break;
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                                                    //View Favorite Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "5":

                    break;
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                                                    // Search Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "6":

                    break;
            }
        }

//        mongoDB.displayAllFavorites();
//        mongoDB.displayAllContacts();

    }
}
