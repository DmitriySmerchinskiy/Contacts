package com.english.contacts.data;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

public class IndexHeader implements StickyHeader {

    private String displayHeader;
    private String indexConditionData;

    public IndexHeader(String indexConditionData, String displayHeader) {
        this.indexConditionData = indexConditionData;
        this.displayHeader = displayHeader;
    }

    public String getDisplayHeader() {
        return displayHeader;
    }

    public String getConditionData() {
        return indexConditionData;
    }
}
