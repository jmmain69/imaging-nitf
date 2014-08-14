/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
package org.codice.imaging.nitf.core;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractNitfSegment {

    private TreCollection treCollection = new TreCollection();

    public final TreCollection getTREsRawStructure() {
        return treCollection;
    }

    public final Map<String, String> getTREsFlat() {
        Map<String, String> tresFlat = new TreeMap<String, String>();
        for (String treName : treCollection.getUniqueNamesOfTRE()) {
            List<Tre> tresWithName = treCollection.getTREsWithName(treName);
            if (tresWithName.size() == 1) {
                Tre onlyTre = tresWithName.get(0);
                List<TreEntry> treEntries = onlyTre.getEntries();
                for (TreEntry treEntry : treEntries) {
                    flattenOneTreEntry(tresFlat, treEntry, onlyTre.getName());
                }
            } else {
                for (int i = 0; i < tresWithName.size(); ++i) {
                    Tre thisTre = tresWithName.get(i);
                    List<TreEntry> treEntries = thisTre.getEntries();
                    for (TreEntry treEntry : treEntries) {
                        tresFlat.put(String.format("%s_%d_%s", thisTre.getName(), i, treEntry.getName()), treEntry.getFieldValue().trim());
                    }
                }
            }
        }
        return tresFlat;
    }

    public final void flattenOneTreEntry(final Map<String, String> tresFlat, final TreEntry treEntry, final String parentName) {
        if ((treEntry.getName() != null) && (treEntry.getFieldValue() != null)) {
            String key = String.format("%s_%s", parentName, treEntry.getName());
            String value = treEntry.getFieldValue().trim();
            tresFlat.put(key, value);
        } else if (treEntry.getGroups() != null) {
            int groupCounter = 0;
            for (TreGroup group : treEntry.getGroups()) {
                groupCounter++;
                for (TreEntry entryInGroup : group.getEntries()) {
                    String key = String.format("%s_%s_%d", parentName, entryInGroup.getName(), groupCounter);
                    String value = entryInGroup.getFieldValue().trim();
                    tresFlat.put(key, value);
                }
            }
        }
    }

    protected final void mergeTREs(final TreCollection tresToAdd) {
        treCollection.add(tresToAdd);
    }
}