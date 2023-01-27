package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class Liquidity {

    private int operationId;
    @JsonBackReference(value = "Company-liquidities")
    private Company company;

    private float currentRatio;

    private float expectedRatio;

    private float absoluteRatio;

    private float quickRatio;

    private int year;

    private float currentRatioCoeff;

    private boolean stateOfBalance;

    @JsonBackReference(value = "Currency-liquidities")
    private Currency currency;
}
