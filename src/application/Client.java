package application;


public class Client {
    private String name, phone;
    private ApplicationForm appForm = new ApplicationForm();
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setPhone(String ph){
        this.phone = ph;
    }
    
    public ApplicationForm getAppForm(){
        return appForm;
    }
}

