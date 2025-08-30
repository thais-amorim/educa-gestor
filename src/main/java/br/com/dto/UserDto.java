package br.com.dto;

import java.util.Date;

public class UserDto {
    public String name;
    public String documentNumber;
    public String email;
    Boolean isActive = true;
    Date createdAt = new Date();
    Date updatedAt = null;
}
