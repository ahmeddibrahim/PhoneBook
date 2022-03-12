import java.util.HashMap;

public class Contact {
    private String firstname;
    private String lastname;
    private String fullname;
    private HashMap <String, String> numbers;
    private boolean favorite;

    public Contact(){}
    public Contact(String firstname, String lastname, HashMap<String,String> numbers, boolean favorite){
        this.firstname = firstname;
        this.lastname = lastname;
        this.numbers = numbers;
        this.favorite = favorite;
        this.fullname = firstname + " " + lastname;
    }

    public HashMap<String, String> getNumbers() {
        return this.numbers;
    }

    public void setNumbers(HashMap<String, String> numbers) {
        this.numbers = numbers;
    }

    public String getFullname() {return this.fullname;}

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
        this.fullname = firstname + " " + lastname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
        this.fullname = this.firstname + " " + this.lastname;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Contacts [\n  Firstname= " + firstname + ",\n  Lastname= " + lastname + ",\n  Favorite= " + favorite + ",\n  Numbers = " + numbers +"\n]";
    }
}
