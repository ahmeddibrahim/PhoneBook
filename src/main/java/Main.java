import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static Contact findContactByName(Scanner in,MongoDB mongoDB){
        ArrayList<Contact> contacts;
        System.out.println("Please enter the name you want to search for: ");
        String input = in.nextLine();
        while(input.isEmpty())
        {
            System.out.println("No name was entered, please try again .. ");
            input = in.nextLine();
        }
        contacts = mongoDB.findByName(input);
        if(contacts.isEmpty())
            System.out.println("No contact was found with such a name");
        else if(contacts.size()==1)
        {
            System.out.println("Is this the contact you are searching for? Press y for Yes");
            input = in.nextLine();
            if(input.equals("y"))
                return contacts.get(0);
        }
        else{
            System.out.println("Please enter the correct index number next to the contact info you are search for :");
            input = in.nextLine();
            int index = 0;
            try{
                index = Integer.parseInt(input);
            }catch (NumberFormatException ex){
                System.err.println("Index error, try again");
                return null;
            }
            if(index<contacts.size())
                return contacts.get(index);
            else
                System.err.println("Index error, try again");
        }
        return null;
    }
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
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                // case 0 : PhoneBook menu
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "0": {
                    System.out.println(" To insert Contact press : 1\n To Search Contact for Update or Delete press: 2\n To view all contacts press: 3\n " +
                            "To view favorite contacts press: 4\n ");
                    break;
                }
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                // case 1 : Insert Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "1": {
                    System.out.println("Enter Firstname");
                    String firstname = in.nextLine();
                    System.out.println("Enter Lastname");
                    String lastname = in.nextLine();

                    HashMap<String, String> numbers = new HashMap<String, String>();
                    System.out.println("Enter a Phone number");
                    String number = in.nextLine();// should be validated ...

                    String type = getType(in);

                    numbers.put(number, type);// add number to the hashmap

                    System.out.println("Do you want to add more phone numbers? Press y for YES ");
                    String input = in.nextLine();
                    while (input.equals("y")) {
                        System.out.println("Enter another Phone number");
                        number = in.nextLine();// should be validated ...

                        while (numbers.containsKey(number))// check in the hashmap if it's duplicated
                        {
                            System.err.println("Number is already available, please Enter another phone number again ");
                            number = in.nextLine();
                        }

                        type = getType(in);
                        while (numbers.containsValue(type)) // check in the hashmap if the type is duplicated
                        {
                            System.err.println("type: " + type + " was used before, please enter another type");
                            type = in.nextLine();
                            if (type.isEmpty())
                                type = "Mobile";
                        }

                        numbers.put(number, type);
                        System.out.println("Do you want to add more phone numbers? Press y for YES ");
                        input = in.nextLine();
                    }
                    System.out.println("Do you want to add " + firstname + " as favorite? press y for YES and n for NO");
                    input = in.nextLine();
                    boolean favorite = false;
                    if (input.equals("y"))
                        favorite = true;
                    Contact contact = new Contact(firstname, lastname, numbers, favorite);
                    if (mongoDB.insertContact(contact)) {
                        System.out.println("Contact Inserted!");
                        System.out.println(contact);
                    } else
                        System.err.println("Operation failed, please try again..");
                    break;
                }
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                // case 2 : Find Contact ( Update, Delete , Mark as favorite)
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "2": {
                    System.out.println("How would you like to search for a contact? Press 1 to search by name or Press 2 to search by number ");
                    String input2 = in.nextLine();
                    Contact contact1 = null;
                    if (input2.equals("1")) {
                        contact1 = findContactByName(in, mongoDB);
                    } else if (input2.equals("2")) {
                        System.out.println("Please enter the number you are searching for :");
                        input2 = in.nextLine();
                        contact1 = mongoDB.findNumber(input2);
                    }

                    if (contact1 == null) {// contact not found and will break of switch cases and go back to the menu
                        System.err.println("Search failed, please try again");
                        break;
                    }

                    System.out.println(" To update Contact press: 1\n To delete contact press: 2\n");
                    input2 = in.nextLine();
                    String fullname = contact1.getFullname(); // get the fullname as it's an integral part for updating or deleting from the database, before any updates occur.
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                    //  Updating Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                    if (input2.equals("1")) {
                        String status = "y"; // status to apply more than one update to the same contact.
                        while (status.equals("y")) { // while yes, user can update multiple times.
                            System.out.println("What would like to Update?\n Press 1 for Firstname \n Press 2 for Lastname\n Press 3 to update numbers \n press 4 to mark contact as favorite\n");
                            input2 = in.nextLine();
                            switch (input2) {
                                case "1": // update Firstname
                                    System.out.println("The current Firstname is : " + contact1.getFirstname() + ", please enter the new Firstname ");
                                    String name = in.nextLine();
                                    contact1.setFirstname(name);
                                    break;

                                case "2": // update Lastname
                                    System.out.println("The current Lastname is : " + contact1.getLastname() + ", please enter the new Lastname ");
                                    name = in.nextLine();
                                    contact1.setLastname(name);

                                    break;

                                case "3"://update numbers or add numbers to the contact's phone numbers
                                    System.out.println("These are the current numbers for the contact :\n" + contact1.getNumbers() + "\n Please enter the type you want add or change :");
                                    String numberType = in.nextLine();
                                    System.out.println("Please enter the new number you want to add : ");
                                    String phoneNumber = in.nextLine();
                                    contact1.addNumber(phoneNumber, numberType);
                                    break;

                                case "4": // mark contact as favorite or remove it from the favorite list
                                    if (contact1.isFavorite()) {
                                        System.out.println("Contact is already favorite. Do you want to remove this contact from the favorite list? Press Y for yes");
                                        input2 = in.nextLine();
                                        if (input2.equals("y")) {
                                            contact1.setFavorite(false);
                                            System.out.println("Contact was removed from the favorite list!");
                                        }
                                    } else {
                                        contact1.setFavorite(true);
                                        System.out.println("Contact was set as favorite");
                                    }
                                    break;
                            }
                            System.out.println(" Would you like to add more updates to this contact? Press y for yes");
                            status = in.nextLine();
                        }

                        if (mongoDB.updateContact(fullname, contact1)) // After all the updates, add the changes to the database
                            System.out.println("Contact updated Successfully!");
                        else
                            System.err.println("Operation Failed, please try again");
                    }
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                    //  Delete Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                    else if(input2.equals("2")){
                        if(mongoDB.deleteContact(fullname))
                            System.out.println("Contact deleted Successfully!");
                        else
                            System.err.println("Operation Failed, please try again");
                    }

                    break;
                }
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                                                    // case 4 : View all Contacts
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "3":
                    mongoDB.displayAllContacts();
                    break;
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                                                    // case 5 : View Favorite Contact
//--------------------------------------------------------------------------------------------------------------------------------------------------//
                case "4":
                    mongoDB.displayAllFavorites();
                    break;
            }
        }


    }
}
