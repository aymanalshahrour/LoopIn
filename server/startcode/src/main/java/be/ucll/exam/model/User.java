package be.ucll.exam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required.")
    private String name;

    @Range(min = 0, max = 100, message = "Age must be a positive integer between 1 and 100.")
    private int age;

    @Email(message = "E-mail must be a valid email format.")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    protected User() {
    }

    public User(String name, int age, String email, String password) {
        setAge(age);
        setEmail(email);
        setName(name);
        setPassword(password);
    }

    public boolean isDateOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start2.isBefore(end1) && end2.isAfter(start1);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return name.equals(other.name) && age == other.age && email.equals(other.email) && password.equals(other.password);
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", age=" + age + ", email=" + email + ", password=" + password + "]";
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
