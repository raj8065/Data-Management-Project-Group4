/**
 * Hold data about a Customer
 * @author prj8121@rit.edu
 *
 */
public class Customer {

    //String fName;
    //String lName;
    //String MI;

    String name;
    int id;
    int phoneNumId;

    public Customer(int id, String name, int phoneNumId){
        this.id = id;
        this.name = name;
    }

    public Customer(String[] data){
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.phoneNumId = Integer.parseInt(data[2]);
    }

    public int getId() {
        return id;
    }

//    public String getFirstName() {
//        return fName;
//    }
//
//    public String getLastName() {
//        return lName;
//    }
//
//    public String getMI() {
//        return MI;
//    }
}
