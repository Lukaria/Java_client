package entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private int userId;
    @JsonManagedReference(value = "User-companies")
    private List<Company> companies;

    private String login;

    private String password;

    private String role;

    private String name;

    private String email;

    private int image;

    private transient int currenctCompany;

    public int getCurrentCompany() {
        return currenctCompany;
    }

    public void setCurrentCompany(int id) {
        this.currenctCompany = id;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, String role, String name, String email) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    public User(int id, List<Company> companies, String login, String password, String role, String name, String email) {
        this.userId = id;
        this.companies = companies;
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
    }

    public void addCompany(Company company) {
        if(companies == null) companies = new ArrayList<>();
        companies.add(company);
    }



}
