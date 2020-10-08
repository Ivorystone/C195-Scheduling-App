package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public final class Customer {

    private final SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleStringProperty customerAddress = new SimpleStringProperty();
    private final SimpleStringProperty customerCity = new SimpleStringProperty();
    private final SimpleStringProperty customerPhone = new SimpleStringProperty();
    private final SimpleStringProperty customerZip = new SimpleStringProperty();

    public Customer() { }

    //Constructor
    public Customer(int customerId, String customerName, String customerAddress, String customerCity,
                    String customerPhone, String customerZip) {
        setCustomerId(customerId);
        setCustomerName(customerName);
        setCustomerAddress(customerAddress);
        setCustomerCity(customerCity);
        setCustomerPhone(customerPhone);
        setCustomerZip(customerZip);
    }

    //Getters
    public int getCustomerId(){ return customerId.get(); }
    public String getCustomerName(){
        return customerName.get();
    }
    public String getCustomerAddress() { return customerAddress.get(); }
    public String getCustomerCity() { return customerCity.get(); }
    public String getCustomerPhone() { return customerPhone.get(); }
    public String getCustomerZip() { return customerZip.get(); }

    //Setters
    public void setCustomerId(int customerId){ this.customerId.set(customerId); }
    public void setCustomerName(String customerName){
        this.customerName.set(customerName);
    }
    public void setCustomerAddress(String customerAddress) { this.customerAddress.set(customerAddress); }
    public void setCustomerCity(String customerCity) {
        this.customerCity.set(customerCity);
    }
    public void setCustomerPhone(String customerPhone) { this.customerPhone.set(customerPhone); }
    public void setCustomerZip(String customerZip) { this.customerZip.set(customerZip); }

}
