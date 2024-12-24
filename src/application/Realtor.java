package application;


public class Realtor {
    private String name, phone, imgPath;
    private double rating; 
    
    public Realtor(String name, String phone, double rating, String img){
        this.name = name; 
        this.phone = phone;
        this.rating = rating;
        this.imgPath = img;
    }
    
    public String getImgPath(){
        return imgPath;
    }
    
    public String getName(){
        return name; 
    }
    
    public String getPhone(){
        return phone; 
    }
    
    public double getRating(){
        return rating; 
    }
    
}
