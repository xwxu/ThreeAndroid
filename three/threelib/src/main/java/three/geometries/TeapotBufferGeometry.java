package three.geometries;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import three.bufferAttribute.Float32BufferAttribute;
import three.bufferAttribute.Uint32BufferAttribute;
import three.core.BufferGeometry;
import three.math.Matrix4;
import three.math.Vector3;
import three.math.Vector4;

public class TeapotBufferGeometry extends BufferGeometry {

    int[] teapotPatches = new int[]{/*rim*/
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            3, 16, 17, 18, 7, 19, 20, 21, 11, 22, 23, 24, 15, 25, 26, 27,
            18, 28, 29, 30, 21, 31, 32, 33, 24, 34, 35, 36, 27, 37, 38, 39,
            30, 40, 41, 0, 33, 42, 43, 4, 36, 44, 45, 8, 39, 46, 47, 12,
            /*body*/
            12, 13, 14, 15, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
            15, 25, 26, 27, 51, 60, 61, 62, 55, 63, 64, 65, 59, 66, 67, 68,
            27, 37, 38, 39, 62, 69, 70, 71, 65, 72, 73, 74, 68, 75, 76, 77,
            39, 46, 47, 12, 71, 78, 79, 48, 74, 80, 81, 52, 77, 82, 83, 56,
            56, 57, 58, 59, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95,
            59, 66, 67, 68, 87, 96, 97, 98, 91, 99, 100, 101, 95, 102, 103, 104,
            68, 75, 76, 77, 98, 105, 106, 107, 101, 108, 109, 110, 104, 111, 112, 113,
            77, 82, 83, 56, 107, 114, 115, 84, 110, 116, 117, 88, 113, 118, 119, 92,
            /*handle*/
            120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135,
            123, 136, 137, 120, 127, 138, 139, 124, 131, 140, 141, 128, 135, 142, 143, 132,
            132, 133, 134, 135, 144, 145, 146, 147, 148, 149, 150, 151, 68, 152, 153, 154,
            135, 142, 143, 132, 147, 155, 156, 144, 151, 157, 158, 148, 154, 159, 160, 68,
            /*spout*/
            161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176,
            164, 177, 178, 161, 168, 179, 180, 165, 172, 181, 182, 169, 176, 183, 184, 173,
            173, 174, 175, 176, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196,
            176, 183, 184, 173, 188, 197, 198, 185, 192, 199, 200, 189, 196, 201, 202, 193,
            /*lid*/
            203, 203, 203, 203, 204, 205, 206, 207, 208, 208, 208, 208, 209, 210, 211, 212,
            203, 203, 203, 203, 207, 213, 214, 215, 208, 208, 208, 208, 212, 216, 217, 218,
            203, 203, 203, 203, 215, 219, 220, 221, 208, 208, 208, 208, 218, 222, 223, 224,
            203, 203, 203, 203, 221, 225, 226, 204, 208, 208, 208, 208, 224, 227, 228, 209,
            209, 210, 211, 212, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240,
            212, 216, 217, 218, 232, 241, 242, 243, 236, 244, 245, 246, 240, 247, 248, 249,
            218, 222, 223, 224, 243, 250, 251, 252, 246, 253, 254, 255, 249, 256, 257, 258,
            224, 227, 228, 209, 252, 259, 260, 229, 255, 261, 262, 233, 258, 263, 264, 237,
            /*bottom*/
            265, 265, 265, 265, 266, 267, 268, 269, 270, 271, 272, 273, 92, 119, 118, 113,
            265, 265, 265, 265, 269, 274, 275, 276, 273, 277, 278, 279, 113, 112, 111, 104,
            265, 265, 265, 265, 276, 280, 281, 282, 279, 283, 284, 285, 104, 103, 102, 95,
            265, 265, 265, 265, 282, 286, 287, 266, 285, 288, 289, 270, 95, 94, 93, 92};

    float[] teapotVertices = new float[]{1.4f, 0, 2.4f,
            1.4f, - 0.784f, 2.4f,
            0.784f, - 1.4f, 2.4f,
            0, - 1.4f, 2.4f,
            1.3375f, 0, 2.53125f,
            1.3375f, - 0.749f, 2.53125f,
            0.749f, - 1.3375f, 2.53125f,
            0, - 1.3375f, 2.53125f,
            1.4375f, 0, 2.53125f,
            1.4375f, - 0.805f, 2.53125f,
            0.805f, - 1.4375f, 2.53125f,
            0f, - 1.4375f, 2.53125f,
            1.5f, 0f, 2.4f,
            1.5f, - 0.84f, 2.4f,
            0.84f, - 1.5f, 2.4f,
            0f, - 1.5f, 2.4f,
            - 0.784f, - 1.4f, 2.4f,
            - 1.4f, - 0.784f, 2.4f,
            - 1.4f, 0f, 2.4f,
            - 0.749f, - 1.3375f, 2.53125f,
            - 1.3375f, - 0.749f, 2.53125f,
            - 1.3375f, 0f, 2.53125f,
            - 0.805f, - 1.4375f, 2.53125f,
            - 1.4375f, - 0.805f, 2.53125f,
            - 1.4375f, 0f, 2.53125f,
            - 0.84f, - 1.5f, 2.4f,
            - 1.5f, - 0.84f, 2.4f,
            - 1.5f, 0f, 2.4f,
            - 1.4f, 0.784f, 2.4f,
            - 0.784f, 1.4f, 2.4f,
            0f, 1.4f, 2.4f,
            - 1.3375f, 0.749f, 2.53125f,
            - 0.749f, 1.3375f, 2.53125f,
            0f, 1.3375f, 2.53125f,
            - 1.4375f, 0.805f, 2.53125f,
            - 0.805f, 1.4375f, 2.53125f,
            0f, 1.4375f, 2.53125f,
            - 1.5f, 0.84f, 2.4f,
            - 0.84f, 1.5f, 2.4f,
            0f, 1.5f, 2.4f,
            0.784f, 1.4f, 2.4f,
            1.4f, 0.784f, 2.4f,
            0.749f, 1.3375f, 2.53125f,
            1.3375f, 0.749f, 2.53125f,
            0.805f, 1.4375f, 2.53125f,
            1.4375f, 0.805f, 2.53125f,
            0.84f, 1.5f, 2.4f,
            1.5f, 0.84f, 2.4f,
            1.75f, 0f, 1.875f,
            1.75f, - 0.98f, 1.875f,
            0.98f, - 1.75f, 1.875f,
            0f, - 1.75f, 1.875f,
            2f, 0f, 1.35f,
            2f, - 1.12f, 1.35f,
            1.12f, - 2f, 1.35f,
            0f, - 2f, 1.35f,
            2f, 0f, 0.9f,
            2f, - 1.12f, 0.9f,
            1.12f, - 2f, 0.9f,
            0f, - 2f, 0.9f,
            - 0.98f, - 1.75f, 1.875f,
            - 1.75f, - 0.98f, 1.875f,
            - 1.75f, 0f, 1.875f,
            - 1.12f, - 2f, 1.35f,
            - 2f, - 1.12f, 1.35f,
            - 2f, 0f, 1.35f,
            - 1.12f, - 2f, 0.9f,
            - 2f, - 1.12f, 0.9f,
            - 2f, 0f, 0.9f,
            - 1.75f, 0.98f, 1.875f,
            - 0.98f, 1.75f, 1.875f,
            0f, 1.75f, 1.875f,
            - 2f, 1.12f, 1.35f,
            - 1.12f, 2f, 1.35f,
            0f, 2f, 1.35f,
            - 2f, 1.12f, 0.9f,
            - 1.12f, 2f, 0.9f,
            0f, 2f, 0.9f,
            0.98f, 1.75f, 1.875f,
            1.75f, 0.98f, 1.875f,
            1.12f, 2f, 1.35f,
            2f, 1.12f, 1.35f,
            1.12f, 2f, 0.9f,
            2f, 1.12f, 0.9f,
            2f, 0f, 0.45f,
            2f, - 1.12f, 0.45f,
            1.12f, - 2f, 0.45f,
            0f, - 2f, 0.45f,
            1.5f, 0f, 0.225f,
            1.5f, - 0.84f, 0.225f,
            0.84f, - 1.5f, 0.225f,
            0f, - 1.5f, 0.225f,
            1.5f, 0f, 0.15f,
            1.5f, - 0.84f, 0.15f,
            0.84f, - 1.5f, 0.15f,
            0f, - 1.5f, 0.15f,
            - 1.12f, - 2f, 0.45f,
            - 2f, - 1.12f, 0.45f,
            - 2f, 0f, 0.45f,
            - 0.84f, - 1.5f, 0.225f,
            - 1.5f, - 0.84f, 0.225f,
            - 1.5f, 0f, 0.225f,
            - 0.84f, - 1.5f, 0.15f,
            - 1.5f, - 0.84f, 0.15f,
            - 1.5f, 0f, 0.15f,
            - 2f, 1.12f, 0.45f,
            - 1.12f, 2f, 0.45f,
            0f, 2f, 0.45f,
            - 1.5f, 0.84f, 0.225f,
            - 0.84f, 1.5f, 0.225f,
            0f, 1.5f, 0.225f,
            - 1.5f, 0.84f, 0.15f,
            - 0.84f, 1.5f, 0.15f,
            0f, 1.5f, 0.15f,
            1.12f, 2f, 0.45f,
            2f, 1.12f, 0.45f,
            0.84f, 1.5f, 0.225f,
            1.5f, 0.84f, 0.225f,
            0.84f, 1.5f, 0.15f,
            1.5f, 0.84f, 0.15f,
            - 1.6f, 0f, 2.025f,
            - 1.6f, - 0.3f, 2.025f,
            - 1.5f, - 0.3f, 2.25f,
            - 1.5f, 0f, 2.25f,
            - 2.3f, 0f, 2.025f,
            - 2.3f, - 0.3f, 2.025f,
            - 2.5f, - 0.3f, 2.25f,
            - 2.5f, 0f, 2.25f,
            - 2.7f, 0f, 2.025f,
            - 2.7f, - 0.3f, 2.025f,
            - 3f, - 0.3f, 2.25f,
            - 3f, 0f, 2.25f,
            - 2.7f, 0f, 1.8f,
            - 2.7f, - 0.3f, 1.8f,
            - 3f, - 0.3f, 1.8f,
            - 3f, 0f, 1.8f,
            - 1.5f, 0.3f, 2.25f,
            - 1.6f, 0.3f, 2.025f,
            - 2.5f, 0.3f, 2.25f,
            - 2.3f, 0.3f, 2.025f,
            - 3f, 0.3f, 2.25f,
            - 2.7f, 0.3f, 2.025f,
            - 3f, 0.3f, 1.8f,
            - 2.7f, 0.3f, 1.8f,
            - 2.7f, 0f, 1.575f,
            - 2.7f, - 0.3f, 1.575f,
            - 3f, - 0.3f, 1.35f,
            - 3f, 0f, 1.35f,
            - 2.5f, 0f, 1.125f,
            - 2.5f, - 0.3f, 1.125f,
            - 2.65f, - 0.3f, 0.9375f,
            - 2.65f, 0f, 0.9375f,
            - 2f, - 0.3f, 0.9f,
            - 1.9f, - 0.3f, 0.6f,
            - 1.9f, 0f, 0.6f,
            - 3f, 0.3f, 1.35f,
            - 2.7f, 0.3f, 1.575f,
            - 2.65f, 0.3f, 0.9375f,
            - 2.5f, 0.3f, 1.125f,
            - 1.9f, 0.3f, 0.6f,
            - 2f, 0.3f, 0.9f,
            1.7f, 0f, 1.425f,
            1.7f, - 0.66f, 1.425f,
            1.7f, - 0.66f, 0.6f,
            1.7f, 0f, 0.6f,
            2.6f, 0f, 1.425f,
            2.6f, - 0.66f, 1.425f,
            3.1f, - 0.66f, 0.825f,
            3.1f, 0f, 0.825f,
            2.3f, 0f, 2.1f,
            2.3f, - 0.25f, 2.1f,
            2.4f, - 0.25f, 2.025f,
            2.4f, 0f, 2.025f,
            2.7f, 0f, 2.4f,
            2.7f, - 0.25f, 2.4f,
            3.3f, - 0.25f, 2.4f,
            3.3f, 0f, 2.4f,
            1.7f, 0.66f, 0.6f,
            1.7f, 0.66f, 1.425f,
            3.1f, 0.66f, 0.825f,
            2.6f, 0.66f, 1.425f,
            2.4f, 0.25f, 2.025f,
            2.3f, 0.25f, 2.1f,
            3.3f, 0.25f, 2.4f,
            2.7f, 0.25f, 2.4f,
            2.8f, 0f, 2.475f,
            2.8f, - 0.25f, 2.475f,
            3.525f, - 0.25f, 2.49375f,
            3.525f, 0f, 2.49375f,
            2.9f, 0f, 2.475f,
            2.9f, - 0.15f, 2.475f,
            3.45f, - 0.15f, 2.5125f,
            3.45f, 0f, 2.5125f,
            2.8f, 0f, 2.4f,
            2.8f, - 0.15f, 2.4f,
            3.2f, - 0.15f, 2.4f,
            3.2f, 0f, 2.4f,
            3.525f, 0.25f, 2.49375f,
            2.8f, 0.25f, 2.475f,
            3.45f, 0.15f, 2.5125f,
            2.9f, 0.15f, 2.475f,
            3.2f, 0.15f, 2.4f,
            2.8f, 0.15f, 2.4f,
            0f, 0f, 3.15f,
            0.8f, 0f, 3.15f,
            0.8f, - 0.45f, 3.15f,
            0.45f, - 0.8f, 3.15f,
            0f, - 0.8f, 3.15f,
            0f, 0f, 2.85f,
            0.2f, 0f, 2.7f,
            0.2f, - 0.112f, 2.7f,
            0.112f, - 0.2f, 2.7f,
            0f, - 0.2f, 2.7f,
            - 0.45f, - 0.8f, 3.15f,
            - 0.8f, - 0.45f, 3.15f,
            - 0.8f, 0f, 3.15f,
            - 0.112f, - 0.2f, 2.7f,
            - 0.2f, - 0.112f, 2.7f,
            - 0.2f, 0f, 2.7f,
            - 0.8f, 0.45f, 3.15f,
            - 0.45f, 0.8f, 3.15f,
            0f, 0.8f, 3.15f,
            - 0.2f, 0.112f, 2.7f,
            - 0.112f, 0.2f, 2.7f,
            0f, 0.2f, 2.7f,
            0.45f, 0.8f, 3.15f,
            0.8f, 0.45f, 3.15f,
            0.112f, 0.2f, 2.7f,
            0.2f, 0.112f, 2.7f,
            0.4f, 0f, 2.55f,
            0.4f, - 0.224f, 2.55f,
            0.224f, - 0.4f, 2.55f,
            0f, - 0.4f, 2.55f,
            1.3f, 0f, 2.55f,
            1.3f, - 0.728f, 2.55f,
            0.728f, - 1.3f, 2.55f,
            0f, - 1.3f, 2.55f,
            1.3f, 0f, 2.4f,
            1.3f, - 0.728f, 2.4f,
            0.728f, - 1.3f, 2.4f,
            0f, - 1.3f, 2.4f,
            - 0.224f, - 0.4f, 2.55f,
            - 0.4f, - 0.224f, 2.55f,
            - 0.4f, 0f, 2.55f,
            - 0.728f, - 1.3f, 2.55f,
            - 1.3f, - 0.728f, 2.55f,
            - 1.3f, 0f, 2.55f,
            - 0.728f, - 1.3f, 2.4f,
            - 1.3f, - 0.728f, 2.4f,
            - 1.3f, 0f, 2.4f,
            - 0.4f, 0.224f, 2.55f,
            - 0.224f, 0.4f, 2.55f,
            0f, 0.4f, 2.55f,
            - 1.3f, 0.728f, 2.55f,
            - 0.728f, 1.3f, 2.55f,
            0f, 1.3f, 2.55f,
            - 1.3f, 0.728f, 2.4f,
            - 0.728f, 1.3f, 2.4f,
            0f, 1.3f, 2.4f,
            0.224f, 0.4f, 2.55f,
            0.4f, 0.224f, 2.55f,
            0.728f, 1.3f, 2.55f,
            1.3f, 0.728f, 2.55f,
            0.728f, 1.3f, 2.4f,
            1.3f, 0.728f, 2.4f,
            0f, 0f, 0f,
            1.425f, 0f, 0f,
            1.425f, 0.798f, 0f,
            0.798f, 1.425f, 0f,
            0f, 1.425f, 0f,
            1.5f, 0f, 0.075f,
            1.5f, 0.84f, 0.075f,
            0.84f, 1.5f, 0.075f,
            0f, 1.5f, 0.075f,
            - 0.798f, 1.425f, 0f,
            - 1.425f, 0.798f, 0f,
            - 1.425f, 0f, 0f,
            - 0.84f, 1.5f, 0.075f,
            - 1.5f, 0.84f, 0.075f,
            - 1.5f, 0f, 0.075f,
            - 1.425f, - 0.798f, 0f,
            - 0.798f, - 1.425f, 0f,
            0f, - 1.425f, 0f,
            - 1.5f, - 0.84f, 0.075f,
            - 0.84f, - 1.5f, 0.075f,
            0f, - 1.5f, 0.075f,
            0.798f, - 1.425f, 0f,
            1.425f, - 0.798f, 0f,
            0.84f, - 1.5f, 0.075f,
            1.5f, - 0.84f, 0.075f};

    public TeapotBufferGeometry(int size, int segments, boolean bottom,
                                boolean lid, boolean body, boolean fitLid, boolean blinn){
        super();
        float blinnScale = 1.3f;
        // scale the size to be the real scaling factor
        float maxHeight = 3.15f * ( blinn ? 1 : blinnScale );

        float maxHeight2 = maxHeight / 2;
        float trueSize = size / maxHeight2;

        // Number of elements depends on what is needed. Subtract degenerate
        // triangles at tip of bottom and lid out in advance.
        int numTriangles = bottom ? ( 8 * segments - 4 ) * segments : 0;
        numTriangles += lid ? ( 16 * segments - 4 ) * segments : 0;
        numTriangles += body ? 40 * segments * segments : 0;

        int[] indices = new int[numTriangles * 3];

        int numVertices = bottom ? 4 : 0;
        numVertices += lid ? 8 : 0;
        numVertices += body ? 20 : 0;
        numVertices *= ( segments + 1 ) * ( segments + 1 );

        float[] vertices = new float[numVertices * 3];
        float[] normals = new float[numVertices * 3];
        float[] uvs = new float[numVertices * 2];

        // Bezier form
        Matrix4 ms = new Matrix4();
        ms.Set(
                - 1.0f, 3.0f, - 3.0f, 1.0f,
                3.0f, - 6.0f, 3.0f, 0.0f,
                - 3.0f, 3.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 0.0f );

        float[] g = new float[16];

        float[] sp = new float[4];
        float[] tp = new float[4];
        float[] dsp = new float[4];
        float[] dtp = new float[4];

        // M * G * M matrix, sort of see
        // http://www.cs.helsinki.fi/group/goa/mallinnus/curves/surfaces.html
        Matrix4[] mgm = new Matrix4[3];

        float[] vert = new float[3];
        float[] sdir = new float[3];
        float[] tdir = new float[3];

        Vector3 norm = new Vector3();

        Vector4 tcoord;

        int vertPerRow;

        float dsval = 0;
        float dtval = 0;

        Vector3 normOut = new Vector3();

        Matrix4 gmx = new Matrix4();
        Matrix4 tmtx = new Matrix4();

        Vector4 vsp = new Vector4();
        Vector4 vtp = new Vector4();
        Vector4 vdsp = new Vector4();
        Vector4 vdtp = new Vector4();

        Vector3 vsdir = new Vector3();
        Vector3 vtdir = new Vector3();

        Matrix4 mst = ms.Clone();
        mst.Transpose();

        for ( int i = 0; i < 3; i ++ ) {
            mgm[i] = new Matrix4();
        }

        int minPatches = body ? 0 : 20;
        int maxPatches = bottom ? 32 : 28;

        vertPerRow = segments + 1;

        int surfCount = 0;

        int vertCount = 0;
        int normCount = 0;
        int uvCount = 0;
        int indexCount = 0;

        for ( int surf = minPatches; surf < maxPatches; surf ++ ) {

            // lid is in the middle of the data, patches 20-27,
            // so ignore it for this part of the loop if the lid is not desired
            if ( lid || ( surf < 20 || surf >= 28 ) ) {

                // get M * G * M matrix for x,y,z
                for ( int i = 0; i < 3; i ++ ) {
                    // get control patches
                    for ( int r = 0; r < 4; r ++ ) {
                        for ( int c = 0; c < 4; c ++ ) {
                            // transposed
                            g[ c * 4 + r ] = teapotVertices[ teapotPatches[ surf * 16 + r * 4 + c ] * 3 + i ];
                            // is the lid to be made larger, and is this a point on the lid
                            // that is X or Y?
                            if ( fitLid && ( surf >= 20 && surf < 28 ) && ( i != 2 ) ) {
                                // increase XY size by 7.7%, found empirically. I don't
                                // increase Z so that the teapot will continue to fit in the
                                // space -1 to 1 for Y (Y is up for the final model).
                                g[ c * 4 + r ] *= 1.077f;
                            }
                            // Blinn "fixed" the teapot by dividing Z by blinnScale, and that's the
                            // data we now use. The original teapot is taller. Fix it:
                            if ( ! blinn && ( i == 2 ) ) {
                                g[ c * 4 + r ] *= blinnScale;
                            }
                        }
                    }

                    gmx.Set( g[ 0 ], g[ 1 ], g[ 2 ], g[ 3 ], g[ 4 ], g[ 5 ], g[ 6 ], g[ 7 ], g[ 8 ], g[ 9 ], g[ 10 ], g[ 11 ], g[ 12 ], g[ 13 ], g[ 14 ], g[ 15 ] );

                    tmtx.MultiplyMatrices( gmx, ms );
                    mgm[i].MultiplyMatrices( mst, tmtx );
                }

                // step along, get points, and output
                for ( int sstep = 0; sstep <= segments; sstep ++ ) {
                    float s = (float) sstep / segments;
                    for ( int tstep = 0; tstep <= segments; tstep ++ ) {
                        float t = (float) tstep / segments;
                        // point from basis
                        // get power vectors and their derivatives
                        float sval = 1.0f, tval = 1.0f;
                        for ( int p = 3; p >= 0; p-- ) {
                            sp[ p ] = sval;
                            tp[ p ] = tval;
                            sval *= s;
                            tval *= t;

                            if ( p == 3 ) {
                                dsp[ p ] = dtp[ p ] = 0.0f;
                                dsval = dtval = 1.0f;
                            } else {
                                dsp[ p ] = dsval * ( 3 - p );
                                dtp[ p ] = dtval * ( 3 - p );
                                dsval *= s;
                                dtval *= t;
                            }
                        }

                        vsp.FromArray( sp, 0 );
                        vtp.FromArray( tp, 0 );
                        vdsp.FromArray( dsp, 0 );
                        vdtp.FromArray( dtp, 0 );

                        // do for x,y,z
                        for ( int i = 0; i < 3; i ++ ) {
                            // multiply power vectors times matrix to get value
                            tcoord = vsp.Clone();
                            tcoord.ApplyMatrix4( mgm[i] );
                            vert[ i ] = tcoord.Dot( vtp );

                            // get s and t tangent vectors
                            tcoord = vdsp.Clone();
                            tcoord.ApplyMatrix4( mgm[i] );
                            sdir[ i ] = tcoord.Dot( vtp );

                            tcoord = vsp.Clone();
                            tcoord.ApplyMatrix4( mgm[i] );
                            tdir[ i ] = tcoord.Dot( vdtp );
                        }

                        // find normal
                        vsdir.FromArray( sdir, 0 );
                        vtdir.FromArray( tdir, 0 );
                        norm.CrossVectors( vtdir, vsdir );
                        norm.Normalize();

                        // if X and Z length is 0, at the cusp, so point the normal up or down, depending on patch number
                        if ( vert[ 0 ] == 0 && vert[ 1 ] == 0 ) {
                            // if above the middle of the teapot, normal points up, else down
                            normOut.Set( 0, vert[ 2 ] > maxHeight2 ? 1 : - 1, 0 );
                        } else {
                            // standard output: rotate on X axis
                            normOut.Set( norm.x, norm.z, - norm.y );
                        }

                        // store it all
                        vertices[ vertCount ++ ] = trueSize * vert[ 0 ];
                        vertices[ vertCount ++ ] = trueSize * ( vert[ 2 ] - maxHeight2 );
                        vertices[ vertCount ++ ] = - trueSize * vert[ 1 ];

                        normals[ normCount ++ ] = normOut.x;
                        normals[ normCount ++ ] = normOut.y;
                        normals[ normCount ++ ] = normOut.z;

                        uvs[ uvCount ++ ] = 1 - t;
                        uvs[ uvCount ++ ] = 1 - s;
                    }
                }

                // save the faces
                for ( int sstep = 0; sstep < segments; sstep ++ ) {
                    for ( int tstep = 0; tstep < segments; tstep ++ ) {
                        int v1 = surfCount * vertPerRow * vertPerRow + sstep * vertPerRow + tstep;
                        int v2 = v1 + 1;
                        int v3 = v2 + vertPerRow;
                        int v4 = v1 + vertPerRow;

                        // Normals and UVs cannot be shared. Without clone(), you can see the consequences
                        // of sharing if you call geometry.applyMatrix( matrix ).
                        if ( NotDegenerate( vertices, v1, v2, v3 ) && indexCount < indices.length) {
                            indices[ indexCount ++ ] = v1;
                            indices[ indexCount ++ ] = v2;
                            indices[ indexCount ++ ] = v3;
                        }
                        if ( NotDegenerate( vertices, v1, v3, v4 ) && indexCount < indices.length ) {
                            indices[ indexCount ++ ] = v1;
                            indices[ indexCount ++ ] = v3;
                            indices[ indexCount ++ ] = v4;
                        }
                    }
                }

                // increment only if a surface was used
                surfCount ++;
            }
        }

        this.SetIndex( new Uint32BufferAttribute( indices, 1 ) );
        this.AddAttribute( "position", new Float32BufferAttribute( vertices, 3 ) );
        this.AddAttribute( "normal", new Float32BufferAttribute( normals, 3 ) );
        this.AddAttribute( "uv", new Float32BufferAttribute( uvs, 2 ) );

        this.ComputeBoundingSphere();
    }

    private boolean NotDegenerate(float[] vertices, int vtx1, int vtx2, int vtx3){
        // if any vertex matches, return false
        return ! ( ( ( vertices[vtx1 * 3] == vertices[ vtx2 * 3 ] ) &&
                ( vertices[ vtx1 * 3 + 1 ] == vertices[ vtx2 * 3 + 1 ] ) &&
                ( vertices[ vtx1 * 3 + 2 ] == vertices[ vtx2 * 3 + 2 ] ) ) ||
                ( ( vertices[ vtx1 * 3 ] == vertices[ vtx3 * 3 ] ) &&
                        ( vertices[ vtx1 * 3 + 1 ] == vertices[ vtx3 * 3 + 1 ] ) &&
                        ( vertices[ vtx1 * 3 + 2 ] == vertices[ vtx3 * 3 + 2 ] ) ) ||
                ( ( vertices[ vtx2 * 3 ] == vertices[ vtx3 * 3 ] ) &&
                        ( vertices[ vtx2 * 3 + 1 ] == vertices[ vtx3 * 3 + 1 ] ) &&
                        ( vertices[ vtx2 * 3 + 2 ] == vertices[ vtx3 * 3 + 2 ] ) ) );
    }

}
