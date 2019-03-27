package three.core;

import java.util.ArrayList;

import three.math.Color;
import three.math.Vector3;

public class Face3 {
    public int a;
    public int b;
    public int c;
    public Vector3 normal;
    public ArrayList<Vector3> vertexNormals;
    public Color color;
    public ArrayList<Color> vertexColors;
    public int materialIndex;

    public Face3(){

    }

    public Face3(int a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Face3(int a, int b, int c, Vector3 normal, Color color, int materialIndex){
        this.a = a;
        this.b = b;
        this.c = c;
        this.normal = normal;
        this.color = color;
        this.materialIndex = materialIndex;
    }

    public Face3(int a, int b, int c, ArrayList<Vector3> vertexNormals, ArrayList<Color> vertexColors, int materialIndex){
        this.a = a;
        this.b = b;
        this.c = c;
        this.vertexNormals = vertexNormals;
        this.vertexColors = vertexColors;
        this.materialIndex = materialIndex;
    }

    public Face3 clone(){
        return new Face3().copy(this);
    }

    public Face3 copy(Face3 source){
        this.a = source.a;
        this.b = source.b;
        this.c = source.c;

        this.normal.copy( source.normal );
        this.color.copy( source.color );

        this.materialIndex = source.materialIndex;

        for ( int i = 0, il = source.vertexNormals.size(); i < il; i ++ ) {
            this.vertexNormals.set(i, source.vertexNormals.get(i).Clone());
        }

        for ( int i = 0, il = source.vertexColors.size(); i < il; i ++ ) {
            this.vertexColors.set(i, source.vertexColors.get(i).clone());
        }

        return this;
    }
}
