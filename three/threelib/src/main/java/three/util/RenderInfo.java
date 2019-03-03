package three.util;

public class RenderInfo {
    public int frame;
    public int calls;
    public int triangles;
    public int points;
    public int lines;

    public RenderInfo(){
        this.frame = 0;
        this.calls = 0;
        this.triangles = 0;
        this.points = 0;
        this.lines = 0;
    }
}
