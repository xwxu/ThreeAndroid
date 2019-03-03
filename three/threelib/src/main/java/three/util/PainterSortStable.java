package three.util;

import java.util.Comparator;

public class PainterSortStable implements Comparator<RenderItem> {

    @Override
    public int compare(RenderItem a, RenderItem b) {
        if ( a.renderOrder != b.renderOrder ) {
            return a.renderOrder - b.renderOrder;
        } else if ( a.program != null && b.program != null && a.program != b.program ) {
            return a.program.id - b.program.id;
        } else if ( a.material.id != b.material.id ) {
            return a.material.id - b.material.id;
        } else if ( a.z != b.z ) {
            return a.z - b.z;
        } else {
            return a.id - b.id;
        }
    }
}
