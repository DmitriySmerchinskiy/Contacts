package com.english.contacts.ui.view.indexbar;

import java.util.HashSet;
import java.util.Set;

class GroupsResolver {

    private Set<Integer> indexesForGroups;

    GroupsResolver(int groupsCount, int availablePlaces) {
        indexesForGroups = findIndexesForGroups(groupsCount, availablePlaces);
    }

    private Set<Integer> findIndexesForGroups(int groupsCount, int availablePlaces) {
        if (groupsCount == 0) {
            return new HashSet<>();
        }

        int[] ranges = new int[groupsCount];
        for (int i = 0; i < groupsCount; ++i) {
            ranges[i] = availablePlaces / groupsCount;
        }
        int rest = availablePlaces % groupsCount;

        if (rest != 0) {
            if (rest % 2 == 0 && ranges.length % 2 == 0) {
                putEvenToEvenRangeCount(ranges, rest);
            } else if (rest % 2 != 0 && ranges.length % 2 != 0) {
                putOddRestToOddRangeCount(ranges, rest);
            } else if (rest % 2 == 0 && ranges.length % 2 != 0) {
                ranges[ranges.length / 2]++;
                rest--;
                putOddRestToOddRangeCount(ranges, rest);
            } else { // rest - odd ranges - even
                ranges[ranges.length / 2 - 1]++;
                rest--;
                putEvenToEvenRangeCount(ranges, rest);
            }
        }

        Set<Integer> indexesForGroups = new HashSet<>();
        if (groupsCount > 1) {
            int start = 0;
            indexesForGroups.add(start);
            for (int i = 0; i < groupsCount / 2 - 1; ++i) {
                start += ranges[i];
                indexesForGroups.add(start);
            }
            if (groupsCount % 2 != 0) {
                start += ranges[groupsCount / 2 - 1] + ranges[groupsCount / 2] / 2;
                indexesForGroups.add(start);
            }
            int end = availablePlaces - 1;
            indexesForGroups.add(end);
            for (int i = groupsCount - 1; i > groupsCount - groupsCount / 2; --i) {
                end -= ranges[i];
                indexesForGroups.add(end);
            }
        } else {
            indexesForGroups.add(ranges[0] / 2);
        }

        return indexesForGroups;
    }

    private void putEvenToEvenRangeCount(int[] ranges, int rest) {
        for (int i = 0; i < ranges.length / 2 && rest > 0; i++, rest -= 2) {
            ranges[ranges.length / 2 - 1 - i]++;
            ranges[ranges.length / 2 + i]++;
        }
    }

    private void putOddRestToOddRangeCount(int[] ranges, int rest) {
        ranges[ranges.length / 2]++;
        rest--;
        for (int i = 1; i <= ranges.length / 2 && rest > 0; i++, rest -= 2) {
            ranges[ranges.length / 2 - i]++;
            ranges[ranges.length / 2 + i]++;
        }
    }

    boolean isGroupPlace(int index) {
        return indexesForGroups.contains(index);
    }
}
