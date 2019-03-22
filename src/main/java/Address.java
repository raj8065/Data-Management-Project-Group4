public class Address {
    private int streetNum;
    private String streetName;
    private String city;
    private String state;
    private int zip;

    public Address(int streetNum, String streetName, String city, String state, int zip){
        this.streetNum = streetNum;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public int getStreetNum(){
        return streetNum;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getZip() {
        return zip;
    }
}
