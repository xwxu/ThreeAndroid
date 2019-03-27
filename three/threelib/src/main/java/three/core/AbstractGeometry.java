package three.core;

import io.reactivex.subjects.PublishSubject;
import three.math.Math_;

public abstract class AbstractGeometry {

    public int id;
    public String type;
    public String uuid;
    public String name;

    public PublishSubject subject;

    public AbstractGeometry(){
        type = "AbstractGeometry";
        //id = geometryId ++;
        uuid = Math_.generateUUID();
        name = "";
        this.subject = PublishSubject.create();
    }
}
