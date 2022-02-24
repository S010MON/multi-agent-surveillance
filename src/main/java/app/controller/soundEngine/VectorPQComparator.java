package app.controller.soundEngine;

import java.util.Comparator;

public class VectorPQComparator implements Comparator<VectorPQEntry> {
    @Override
    public int compare(VectorPQEntry o1, VectorPQEntry o2) {
        return Integer.compare(o1.diffractionCount, o2.diffractionCount);
    }
}
