package com.packtpub.springdata.jpa.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * A model object for contacts.
 * @author Petri Kainulainen
 */
@Entity
@SqlResultSetMappings(
@SqlResultSetMapping(name="countMapping",
        columns={
            @ColumnResult(name="contactCount")
        }
)
)
@NamedNativeQueries({
@NamedNativeQuery(name = "Contact.countContacts",
        query = "SELECT COUNT(c.id) as contactCount FROM contacts c WHERE LOWER(c.first_name) LIKE LOWER(:searchTerm) OR LOWER(c.last_name) LIKE LOWER(:searchTerm)",
        resultSetMapping = "countMapping"
),
@NamedNativeQuery(name = "Contact.findContacts",
        query = "SELECT * FROM contacts c WHERE LOWER(c.first_name) LIKE LOWER(:searchTerm) OR LOWER(c.last_name) LIKE LOWER(:searchTerm) ORDER BY c.last_name ASC, c.first_name ASC",
        resultClass = Contact.class)
})
@Table(name = "contacts")
public class Contact {

    public static final int MAX_LENGTH_EMAIL_ADDRESS = 100;
    public static final int MAX_LENGTH_FIRST_NAME = 50;
    public static final int MAX_LENGTH_LAST_NAME = 100;
    public static final int MAX_LENGTH_PHONE_NUMBER = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Address address;

    @Column(name = "email_address", length = MAX_LENGTH_EMAIL_ADDRESS)
    private String emailAddress;

    @Column(name = "first_name", length = MAX_LENGTH_FIRST_NAME)
    private String firstName;

    @Column(name = "last_name", length = MAX_LENGTH_LAST_NAME)
    private String lastName;

    @Column(name = "phone_number", length = MAX_LENGTH_PHONE_NUMBER)
    private String phoneNumber;

    @Version
    private long version;

    public Contact() {

    }

    public Long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public static Builder getBuilder(String firstName, String lastName) {
        return new Builder(firstName, lastName);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getVersion() {
        return version;
    }

    public void update(String firstName, String lastName, String emailAddress, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public void updateAddress(String streetAddress, String postCode, String postOffice, String state, String country) {
        if (address == null) {
            address = new Address();
        }

        address.update(streetAddress, postCode, postOffice, state, country);
    }

    public static class Builder {

        private Contact built;

        public Builder(String firstName, String lastName) {
            built = new Contact();
            built.firstName = firstName;
            built.lastName = lastName;
        }

        public Builder address(String streetAddress, String postCode, String postOffice, String state, String county) {
            Address address = Address.getBuilder(streetAddress, postCode, postOffice)
                    .state(state)
                    .country(county)
                    .build();
            built.address = address;
            return this;
        }

        public Builder emailAddress(String emailAddress) {
            built.emailAddress = emailAddress;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            built.phoneNumber = phoneNumber;
            return this;
        }

        public Contact build() {
            return built;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
