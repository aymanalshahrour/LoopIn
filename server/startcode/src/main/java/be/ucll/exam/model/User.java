package be.ucll.exam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required.")
    private String username;

    @Email(message = "E-mail must be a valid email format.")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    protected User() {
    }

    public User(String name, String email, String password) {
        setEmail(email);
        setUsername(name);
        setPassword(password);
    }

    public boolean isDateOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start2.isBefore(end1) && end2.isAfter(start1);
    }

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String name) {
        this.username = name;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
