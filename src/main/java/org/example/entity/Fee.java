package org.example.entity;

import jakarta.persistence.*;

import java.util.List;

import static org.example.utils.Constants.*;


@Entity
@Table(schema = SCHEMA,name = TABLE_FEE)
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String feeSum;

    @ManyToMany(mappedBy = "fees")
    private List<Rule> rules;
    public Fee() {
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public Fee(String feeSum) {
        this.feeSum = feeSum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeeSum() {
        return feeSum;
    }

    public void setFeeSum(String feeSum) {
        this.feeSum = feeSum;
    }

}
