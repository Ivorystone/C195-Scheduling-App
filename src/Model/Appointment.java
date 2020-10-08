package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class Appointment {

    private final SimpleIntegerProperty aptId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty aptCustId = new SimpleIntegerProperty();
    private final SimpleStringProperty aptStart = new SimpleStringProperty();
    private final SimpleStringProperty aptEnd = new SimpleStringProperty();
    private final SimpleStringProperty aptType = new SimpleStringProperty();
    private final SimpleStringProperty aptDescription = new SimpleStringProperty();
    private final SimpleStringProperty aptLocation = new SimpleStringProperty();
    private final SimpleStringProperty aptContact = new SimpleStringProperty();
    private final SimpleStringProperty aptCustomerName = new SimpleStringProperty();

    public Appointment() {}

    //Constructor
    public Appointment (int id, int custId, String start, String end, String type, String description, String location, String contact, String customerName)
    {
        setAptId(id);
        setAptCustId(custId);
        setAptStart(start);
        setAptEnd(end);
        setAptType(type);
        setAptDescription(description);
        setAptLocation(location);
        setAptContact(contact);
        setAptCustomerName(customerName);
    }


    //Getters
    public int getAptId() { return aptId.get(); }
    public int getAptCustId() { return aptCustId.get(); }
    public String getAptStart() { return aptStart.get(); }
    public String getAptEnd() { return aptEnd.get(); }
    public String getAptType() { return aptType.get(); }
    public String getAptDescription() { return aptDescription.get(); }
    public String getAptLocation() { return aptLocation.get(); }
    public String getAptContact() { return aptContact.get(); }
    public String getAptCustomerName() { return aptCustomerName.get(); }

    //Setters
    public void setAptId(int aptId) { this.aptId.set(aptId); }
    public void setAptCustId(int aptCustId) { this.aptCustId.set(aptCustId); }
    public void setAptStart(String aptTimeStart) { this.aptStart.set(aptTimeStart); }
    public void setAptEnd(String aptEnd) { this.aptEnd.set(aptEnd); }
    public void setAptType(String aptTitle) { this.aptType.set(aptTitle); }
    public void setAptDescription(String aptDescription) { this.aptDescription.set(aptDescription); }
    public void setAptLocation(String aptLocation) { this.aptLocation.set(aptLocation); }
    public void setAptContact(String aptContact) { this.aptContact.set(aptContact); }
    public void setAptCustomerName(String aptCustomerName) { this.aptCustomerName.set(aptCustomerName); }



}
