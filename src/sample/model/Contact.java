package sample.model;

public class Contact {

    private int contactId;
    private String name;
    private String email;

    public Contact(int contactId, String name, String email) {

        this.contactId = contactId;
        this.name = name;
        this.email = email;

    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {

        return name;

    }
}
