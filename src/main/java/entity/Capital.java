package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class Capital {

    private int operationId;
    @JsonBackReference(value = "Company-capitals")
    private Company company;

    private float netProfit;

    private float actives;

    private int year;

    private float RWVAT;

    private float netWorth;

    private float investedFunds;

}
