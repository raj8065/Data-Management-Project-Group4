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
    //int phoneNumId;
    Address address;
    String gender;
    int annualIncome;

    public Customer(int id, String name, /*int phoneNumId,*/ Address address, String gender, int annualIncome){
        this.id = id;
        this.name = name;
        //this.phoneNumId = phoneNumId;
        this.address = address;
        this.gender = gender;
        this.annualIncome = annualIncome;
    }

    public Customer(String[] data){
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
//        this.phoneNumId = Integer.parseInt(data[2]);
//        this.address = new Address(Integer.parseInt(data[3]), data[4], data[5], data[6], Integer.parseInt(data[7]));
//        this.gender = data[8];
//        this.annualIncome = Integer.parseInt(data[9]);
        this.address = new Address(Integer.parseInt(data[2]), data[3], data[4], data[5], Integer.parseInt(data[6]));
        this.gender = data[7];
        this.annualIncome = Integer.parseInt(data[8]);
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
