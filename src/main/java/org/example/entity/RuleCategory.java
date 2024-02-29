package org.example.entity;

import jakarta.persistence.*;
import org.example.utils.Constants;

import java.util.List;

@Entity
@Table(schema = Constants.SCHEMA)
public class RuleCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    @ManyToMany(mappedBy = "categories")
    private List<Rule> rules;

    public RuleCategory() {

    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RuleCategory(String title) {
        this.title = title;
    }
}
