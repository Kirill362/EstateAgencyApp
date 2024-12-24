package application;


public class Address {
    private String city, district, street;
    private int house, corpus = 0, flatNumber;
    
    public Address(String city, String district, String street, int house, int flatNum){
        this.city = city;
        this.district = district;
        this.street = street;
        this.house = house;
        this.flatNumber = flatNum;
    }
    
    public Address(String city, String district, String street, int house, int corpus, int flatNum){
        this.city = city;
        this.district = district;
        this.street = street;
        this.house = house;
        this.corpus = corpus;
        this.flatNumber = flatNum;
    }
    
    public String getFullAddress(){
        String ans;
        if (corpus == 0){
            ans = city + ", " + district + " район, " + street + ", д. " + house + ", кв. " + flatNumber;
        }
        else {
            ans = city + ", " + district + " район, " + street + ", д. " + house + ", к. " + corpus + ", кв. " + flatNumber;
        }
        return ans;
    }
    
    public String getDistrict(){
        return district;
    }
}

