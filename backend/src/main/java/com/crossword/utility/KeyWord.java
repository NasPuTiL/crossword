package com.crossword.utility;

import java.util.List;
import java.util.Map;

public class KeyWord {
    private Long id;
    private String value;
    private Map<KeyWord, List<ValueWord>> keyWordListMap;

    public KeyWord(Long id, String value, Map<KeyWord, List<ValueWord>> keyWordListMap) {
        this.id = id;
        this.value = value;
        this.keyWordListMap = keyWordListMap;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Map<KeyWord, List<ValueWord>> getKeyWordListMap() {
        return keyWordListMap;
    }

}
