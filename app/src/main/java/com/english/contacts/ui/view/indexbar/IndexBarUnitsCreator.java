package com.english.contacts.ui.view.indexbar;

import com.english.contacts.data.IndexHeader;

import java.util.ArrayList;
import java.util.List;

class IndexBarUnitsCreator {

    static List<IndexBarUnit> create(int availableSpace, int unitSpace, List<IndexHeader> indexHeaders) {
        List<IndexBarUnit> indexBarUnits = new ArrayList<>();

        int availablePlacesForLetters = availableSpace / unitSpace;
        int lettersCount = indexHeaders.size();
        if (availablePlacesForLetters >= lettersCount) {
            for (int i = 0; i < indexHeaders.size(); ++i) {
                indexBarUnits.add(new IndexBarUnit(indexHeaders.get(i).getDisplayHeader()));
            }
        } else {
            indexBarUnits.add(new IndexBarUnit(indexHeaders.get(0).getDisplayHeader()));

            availablePlacesForLetters -= 2;
            if (availablePlacesForLetters % 2 == 0) {
                availablePlacesForLetters++;
            }
            lettersCount -= 2;

            int excessLettersCount = lettersCount - availablePlacesForLetters;
            int maxDotsCount = (availablePlacesForLetters + 1) / 2;
            int dotsCount;
            if (excessLettersCount >= maxDotsCount) {
                dotsCount = maxDotsCount;
            } else {
                dotsCount = excessLettersCount;
            }

            GroupsResolver groupsResolver = new GroupsResolver(dotsCount, availablePlacesForLetters);
            int dotsRemain = dotsCount;
            int headersIterator = 1;
            for (int placeForLetter = 0; placeForLetter < availablePlacesForLetters; ++placeForLetter) {
                if (groupsResolver.isGroupPlace(placeForLetter)) {
                    List<String> group = new ArrayList<>();
                    for (int i = 0; i < excessLettersCount / dotsRemain + 1; ++i) {
                        group.add(indexHeaders.get(headersIterator++).getDisplayHeader());
                    }
                    excessLettersCount -= excessLettersCount / dotsRemain;
                    dotsRemain--;
                    indexBarUnits.add(new IndexBarUnit(group));
                } else {
                    indexBarUnits.add(new IndexBarUnit(indexHeaders.get(headersIterator++).getDisplayHeader()));
                }
            }

            indexBarUnits.add(new IndexBarUnit(indexHeaders.get(indexHeaders.size() - 1).getDisplayHeader()));
        }

        return indexBarUnits;
    }
}