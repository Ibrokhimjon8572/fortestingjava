package org.example.dto;

import java.util.List;

public class RuleDto {
    private String content;
    private List<Long> fees;
    private List<Long> categories;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getFees() {
        return fees;
    }

    public void setFees(List<Long> fees) {
        this.fees = fees;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }
}
