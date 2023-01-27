package entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Profit {

    private int operationId;
    @JsonBackReference(value = "Company-profits")
    private Company company;

    private float ROA;

    private float ROS;

    private float ROE;

    private float ROI;

    private int year;


}
