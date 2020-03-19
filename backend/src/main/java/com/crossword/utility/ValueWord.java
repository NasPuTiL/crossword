package com.crossword.utility;

public class ValueWord {
    private Long id;
    private Long foreign_id;
    private String value;

    public ValueWord(Long id, Long foreign_id, String value) {
        this.id = id;
        this.foreign_id = foreign_id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Long getForeign_id() {
        return foreign_id;
    }

    public void setForeign_id(Long foreign_id) {
        this.foreign_id = foreign_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
