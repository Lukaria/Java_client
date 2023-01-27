package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class Apives {

    private int operationId;
    @JsonBackReference(value = "Company-apives")
    private Company company;

    private boolean isActive;

    private int year;

    private float AP1;

    private float AP2;

    private float AP3;

    private float AP4;
}
