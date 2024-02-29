package org.example.entity;

import jakarta.persistence.*;


import java.util.List;

import static org.example.utils.Constants.*;

@Entity
@Table(schema = SCHEMA,name = TABLE_RULE)
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createdDate;
    private String content;

    @ManyToMany
    @JoinTable(
            name = "rule_fee_mapping",
            joinColumns = @JoinColumn(name = "rule_id"),
            inverseJoinColumns = @JoinColumn(name = "fee_id"))
    private List<Fee> fees;

    @ManyToMany(cascade = CascadeType.REMOVE) // Cascade delete to remove mapping records
    @JoinTable(

            name = "rule_category_mapping",
            joinColumns = @JoinColumn(name = "rule_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<RuleCategory> categories;

    public Rule() {
    }

    public Rule( String createdDate, String content, List<Fee> fees, List<RuleCategory> categories) {
        this.createdDate = createdDate;
        this.content = content;
        this.fees = fees;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Fee> getFees() {
        return fees;
    }

    public void setFees(List<Fee> fees) {
        this.fees = fees;
    }

    public List<RuleCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<RuleCategory> categories) {
        this.categories = categories;
    }
}
