package three.util;

import java.util.Comparator;

public class IntersectComparator implements Comparator<Intersect> {

    @Override
    public int compare(Intersect a, Intersect b){
        return a.distance < b.distance ? 1 : -1;
    }
}
