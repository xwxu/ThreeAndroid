package three.materials.parameters;

import three.constants;

public class MaterialParameters {
        public boolean fog = true;
        public boolean lights = true;
        public int blending = constants.NormalBlending;
        public int side = constants.FrontSide;
        public boolean flatShading = false;
        public int vertexColors = constants.NoColors; // THREE.NoColors, THREE.VertexColors, THREE.FaceColors
        public float opacity = 1.0f;
        public boolean transparent = false;
        public int blendSrc = constants.SrcAlphaFactor;
        public int blendDst = constants.OneMinusSrcAlphaFactor;
        public int blendEquation = constants.AddEquation;
        public int blendSrcAlpha = 0;
        public int blendDstAlpha = 0;
        public int blendEquationAlpha = 0;
        public int depthFunc = constants.LessEqualDepth;
        public boolean depthTest = true;
        public boolean depthWrite = true;
        public boolean clipIntersection = false;
        public boolean clipShadows = false;
        public int shadowSide = 0;
        public boolean colorWrite = true;
        public String precision = null;
        public boolean polygonOffset = false;
        public float polygonOffsetFactor = 0.0f;
        public float polygonOffsetUnits = 0.0f;
        public boolean dithering = false;
        public int alphaTest = 0;
        public boolean premultipliedAlpha = false;
        public boolean visible = true;
        public boolean wireframe = false;
}
