package application;


public class ApplicationForm {
    private String district;
    private int minPrice, maxPrice, minArea, maxArea, roomsNum;
    
    public void setDistrict(String dis){
        this.district = dis;
    }
    
    public void setMinPrice(int pr){
        this.minPrice = pr;
    }
    
    public void setMaxPrice(int pr){
        this.maxPrice = pr;
    }
    
    public void setMinArea(int ar){
        this.minArea = ar;
    }
    
    public void setMaxArea(int ar){
        this.maxArea = ar;
    }
    
    public void setRoomsNum(int r){
        this.roomsNum = r;
    }
    
    public String getDistrict(){
        return district;
    }
    
    public int getMinPrice(){
        return minPrice;
    }
    
    public int getMaxPrice(){
        return maxPrice;
    }
    
    public int getMinArea(){
        return minArea;
    }
    
    public int getMaxArea(){
        return maxArea;
    }
    
    public int getRoomsNum(){
        return roomsNum;
    }
}
