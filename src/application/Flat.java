package application;


public class Flat {
    private int price, area, rooms;
    private Address address;
    private String imgPath;
    private Realtor realtor1, realtor2;
    
    public Flat(Address adr, int price, int area, int rooms, String imgPath, Realtor r1, Realtor r2){
        address = adr;
        this.price = price;
        this.area = area;
        this.imgPath = imgPath;
        this.rooms = rooms;
        this.realtor1 = r1;
        this.realtor2 = r2;
    }
    
    public Realtor getRealtor1(){
        return this.realtor1;
    }
    
    public Realtor getRealtor2(){
        return this.realtor2;
    }
    
    public String getAddress(){
        return address.getFullAddress();
    }
    
    public int getPrice(){
        return price;
    }
    
    public int getArea(){
        return area;
    }
    
    public int getRooms(){
        return rooms;
    }
    
    public String getPath(){
        return imgPath;
    }
    
    public Address getFullAddress(){
        return address;
    }
}
