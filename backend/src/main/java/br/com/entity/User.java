package br.com.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    private String name;
    private String documentNumber;
    private String email;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    public User() {
    }

    public User(String name, String documentNumber, String email) {
        this.name = name;
        this.documentNumber = documentNumber.replaceAll("\\D", "").trim();
        this.email = email;
        this.createdAt = new Date();
    }

    //    Esses getters e setters são necessários para o Hibernate, mas imagino ser possível talvez utilizar o Record do Java
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", email='" + email + '\'' +
                ']';
    }
}