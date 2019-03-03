package three.util;

import java.util.Comparator;

public class ReversePainterSortStable implements Comparator<RenderItem> {
    @Override
    public int compare(RenderItem a, RenderItem b) {
        if ( a.renderOrder != b.renderOrder ) {
            return a.renderOrder - b.renderOrder;
        } if ( a.z != b.z ) {
            return b.z - a.z;
        } else {
            return a.id - b.id;
        }
    }
}
