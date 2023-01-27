package entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Company {

    private int companyId;
    @JsonBackReference(value = "User-companies")
    private User user;

    private String name;
    @JsonManagedReference(value = "Company-capitals")
    private List<Capital> capitals;
    @JsonManagedReference(value = "Company-liquidities")
    private List<Liquidity> liquidities;
    @JsonManagedReference(value = "Company-apives")
    private List<Apives> apives;
    @JsonManagedReference(value = "Company-profits")
    private List<Profit> profits;

    public Company(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public Company(int companyId, String name, List<Apives> apives, List<Capital> capitals, List<Liquidity> liquidities, List<Profit> profits) {
        this.capitals = capitals;
        this.companyId = companyId;
        this.apives = apives;
        this.liquidities = liquidities;
        this.profits = profits;
        this.name = name;
    }

    public void nullAll(){
        this.user = null;
        this.capitals = null;
        this.apives = null;
        this.liquidities = null;
        this.profits = null;
    }

}
