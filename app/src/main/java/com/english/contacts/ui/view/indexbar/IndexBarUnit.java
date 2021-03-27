package com.english.contacts.ui.view.indexbar;

import java.util.List;

class IndexBarUnit {

    static final String GROUP_LABEL = "•";

    private String singleLetter = null;
    private List<String> lettersGroup = null;

    IndexBarUnit(String singleLetter) {
        this.singleLetter = singleLetter;
    }

    IndexBarUnit(List<String> lettersGroup) {
        this.lettersGroup = lettersGroup;
    }

    String getDisplayValue() {
        if (singleLetter != null) {
            return singleLetter;
        } else {
            return GROUP_LABEL;
        }
    }

    String getPartialLetter(double part, int total) {
        if (singleLetter != null) {
            return singleLetter;
        } else {
            int letterIndex = (int) (part / (double) total * (double) lettersGroup.size());
            letterIndex = Math.max(0, letterIndex);
            letterIndex = Math.min(letterIndex, lettersGroup.size() - 1);
            return lettersGroup.get(letterIndex);
        }
    }
}

