package entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import  java.util.List;


@Data
@ToString
public class Currency {

    private int currencyId;

    private int currencyCode;

    private char symbol;

    private float coeff;

    private String abbreviation;
    @JsonManagedReference(value = "Currency-liquidities")
    private List<Liquidity> liquidities;
}
