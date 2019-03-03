package three.renderers.shaders.ShaderChunks;

public class Bsdfs {
    public final static String code =
    "float punctualLightIntensityToIrradianceFactor( const in float lightDistance, const in float cutoffDistance, const in float decayExponent ) {\n" +

    "#if defined ( PHYSICALLY_CORRECT_LIGHTS )\n" +
    "\tfloat distanceFalloff = 1.0 / max( pow( lightDistance, decayExponent ), 0.01 );\n" +
    "\tif( cutoffDistance > 0.0 ) {\n" +
    "\t\tdistanceFalloff *= pow2( saturate( 1.0 - pow4( lightDistance / cutoffDistance ) ) );\n" +
    "\t}\n" +
    "\treturn distanceFalloff;\n" +
    "#else\n" +
    "\tif( cutoffDistance > 0.0 && decayExponent > 0.0 ) {\n" +
    "\t\treturn pow( saturate( -lightDistance / cutoffDistance + 1.0 ), decayExponent );\n" +
    "\t}\n" +
    "\treturn 1.0;\n" +
    "#endif\n" +
    "}\n" +
    "vec3 BRDF_Diffuse_Lambert( const in vec3 diffuseColor ) {\n" +
    "\treturn RECIPROCAL_PI * diffuseColor;\n" +
    "} // validated\n" +
    "\n" +
    "vec3 F_Schlick( const in vec3 specularColor, const in float dotLH ) {\n" +

    "\tfloat fresnel = exp2( ( -5.55473 * dotLH - 6.98316 ) * dotLH );\n" +
    "\treturn ( 1.0 - specularColor ) * fresnel + specularColor;\n" +
    "} // validated\n" +

    "float G_GGX_Smith( const in float alpha, const in float dotNL, const in float dotNV ) {\n" +
    "\tfloat a2 = pow2( alpha );\n" +
    "\tfloat gl = dotNL + sqrt( a2 + ( 1.0 - a2 ) * pow2( dotNL ) );\n" +
    "\tfloat gv = dotNV + sqrt( a2 + ( 1.0 - a2 ) * pow2( dotNV ) );\n" +
    "\treturn 1.0 / ( gl * gv );\n" +
    "} // validated\n" +

    "float G_GGX_SmithCorrelated( const in float alpha, const in float dotNL, const in float dotNV ) {\n" +
    "\tfloat a2 = pow2( alpha );\n" +
    "\tfloat gv = dotNL * sqrt( a2 + ( 1.0 - a2 ) * pow2( dotNV ) );\n" +
    "\tfloat gl = dotNV * sqrt( a2 + ( 1.0 - a2 ) * pow2( dotNL ) );\n" +
    "\treturn 0.5 / max( gv + gl, EPSILON );\n" +
    "}\n" +

    "float D_GGX( const in float alpha, const in float dotNH ) {\n" +
    "\tfloat a2 = pow2( alpha );\n" +
    "\tfloat denom = pow2( dotNH ) * ( a2 - 1.0 ) + 1.0; // avoid alpha = 0 with dotNH = 1\n" +
    "\treturn RECIPROCAL_PI * a2 / pow2( denom );\n" +
    "}\n" +

    "vec3 BRDF_Specular_GGX( const in IncidentLight incidentLight, const in GeometricContext geometry, const in vec3 specularColor, const in float roughness ) {\n" +
    "\tfloat alpha = pow2( roughness ); // UE4's roughness\n" +
    "\tvec3 halfDir = normalize( incidentLight.direction + geometry.viewDir );\n" +
    "\tfloat dotNL = saturate( dot( geometry.normal, incidentLight.direction ) );\n" +
    "\tfloat dotNV = saturate( dot( geometry.normal, geometry.viewDir ) );\n" +
    "\tfloat dotNH = saturate( dot( geometry.normal, halfDir ) );\n" +
    "\tfloat dotLH = saturate( dot( incidentLight.direction, halfDir ) );\n" +
    "\tvec3 F = F_Schlick( specularColor, dotLH );\n" +
    "\tfloat G = G_GGX_SmithCorrelated( alpha, dotNL, dotNV );\n" +
    "\tfloat D = D_GGX( alpha, dotNH );\n" +
    "\treturn F * ( G * D );\n" +
    "} // validated\n" +

    "vec2 LTC_Uv( const in vec3 N, const in vec3 V, const in float roughness ) {\n" +
    "\tconst float LUT_SIZE  = 64.0;\n" +
    "\tconst float LUT_SCALE = ( LUT_SIZE - 1.0 ) / LUT_SIZE;\n" +
    "\tconst float LUT_BIAS  = 0.5 / LUT_SIZE;\n" +
    "\tfloat dotNV = saturate( dot( N, V ) );\n" +
    "\tvec2 uv = vec2( roughness, sqrt( 1.0 - dotNV ) );\n" +
    "\tuv = uv * LUT_SCALE + LUT_BIAS;\n" +
    "\treturn uv;\n" +
    "}\n" +

    "float LTC_ClippedSphereFormFactor( const in vec3 f ) {\n" +
    "\tfloat l = length( f );\n" +
    "\treturn max( ( l * l + f.z ) / ( l + 1.0 ), 0.0 );\n" +
    "}\n" +

    "vec3 LTC_EdgeVectorFormFactor( const in vec3 v1, const in vec3 v2 ) {\n" +
    "\tfloat x = dot( v1, v2 );\n" +
    "\tfloat y = abs( x );\n" +
    "\tfloat a = 0.8543985 + ( 0.4965155 + 0.0145206 * y ) * y;\n" +
    "\tfloat b = 3.4175940 + ( 4.1616724 + y ) * y;\n" +
    "\tfloat v = a / b;\n" +
    "\tfloat theta_sintheta = ( x > 0.0 ) ? v : 0.5 * inversesqrt( max( 1.0 - x * x, 1e-7 ) ) - v;\n" +
    "\treturn cross( v1, v2 ) * theta_sintheta;\n" +
    "}\n" +

    "vec3 LTC_Evaluate( const in vec3 N, const in vec3 V, const in vec3 P, const in mat3 mInv, const in vec3 rectCoords[ 4 ] ) {\n" +
    "\tvec3 v1 = rectCoords[ 1 ] - rectCoords[ 0 ];\n" +
    "\tvec3 v2 = rectCoords[ 3 ] - rectCoords[ 0 ];\n" +
    "\tvec3 lightNormal = cross( v1, v2 );\n" +
    "\tif( dot( lightNormal, P - rectCoords[ 0 ] ) < 0.0 ) return vec3( 0.0 );\n" +
    "\tvec3 T1, T2;\n" +
    "\tT1 = normalize( V - N * dot( V, N ) );\n" +
    "\tT2 = - cross( N, T1 ); // negated from paper; possibly due to a different handedness of world coordinate system\n" +
    "\tmat3 mat = mInv * transposeMat3( mat3( T1, T2, N ) );\n" +
    "\t// transform rect\n" +
    "\tvec3 coords[ 4 ];\n" +
    "\tcoords[ 0 ] = mat * ( rectCoords[ 0 ] - P );\n" +
    "\tcoords[ 1 ] = mat * ( rectCoords[ 1 ] - P );\n" +
    "\tcoords[ 2 ] = mat * ( rectCoords[ 2 ] - P );\n" +
    "\tcoords[ 3 ] = mat * ( rectCoords[ 3 ] - P );\n" +
    "\t// project rect onto sphere\n" +
    "\tcoords[ 0 ] = normalize( coords[ 0 ] );\n" +
    "\tcoords[ 1 ] = normalize( coords[ 1 ] );\n" +
    "\tcoords[ 2 ] = normalize( coords[ 2 ] );\n" +
    "\tcoords[ 3 ] = normalize( coords[ 3 ] );\n" +
    "\t// calculate vector form factor\n" +
    "\tvec3 vectorFormFactor = vec3( 0.0 );\n" +
    "\tvectorFormFactor += LTC_EdgeVectorFormFactor( coords[ 0 ], coords[ 1 ] );\n" +
    "\tvectorFormFactor += LTC_EdgeVectorFormFactor( coords[ 1 ], coords[ 2 ] );\n" +
    "\tvectorFormFactor += LTC_EdgeVectorFormFactor( coords[ 2 ], coords[ 3 ] );\n" +
    "\tvectorFormFactor += LTC_EdgeVectorFormFactor( coords[ 3 ], coords[ 0 ] );\n" +
    "\tfloat result = LTC_ClippedSphereFormFactor( vectorFormFactor );\n" +

    "/*\n" +
    "\tfloat len = length( vectorFormFactor );\n" +
    "\tfloat z = vectorFormFactor.z / len;\n" +
    "\tconst float LUT_SIZE  = 64.0;\n" +
    "\tconst float LUT_SCALE = ( LUT_SIZE - 1.0 ) / LUT_SIZE;\n" +
    "\tconst float LUT_BIAS  = 0.5 / LUT_SIZE;\n" +
    "\t// tabulated horizon-clipped sphere, apparently...\n" +
    "\tvec2 uv = vec2( z * 0.5 + 0.5, len );\n" +
    "\tuv = uv * LUT_SCALE + LUT_BIAS;\n" +
    "\tfloat scale = texture2D( ltc_2, uv ).w;\n" +
    "\tfloat result = len * scale;\n" +
    "*/\n" +

    "\treturn vec3( result );\n" +
    "}\n" +

    "vec3 BRDF_Specular_GGX_Environment( const in GeometricContext geometry, const in vec3 specularColor, const in float roughness ) {\n" +
    "\tfloat dotNV = saturate( dot( geometry.normal, geometry.viewDir ) );\n" +
    "\tconst vec4 c0 = vec4( - 1, - 0.0275, - 0.572, 0.022 );\n" +
    "\tconst vec4 c1 = vec4( 1, 0.0425, 1.04, - 0.04 );\n" +
    "\tvec4 r = roughness * c0 + c1;\n" +
    "\tfloat a004 = min( r.x * r.x, exp2( - 9.28 * dotNV ) ) * r.x + r.y;\n" +
    "\tvec2 AB = vec2( -1.04, 1.04 ) * a004 + r.zw;\n" +
    "\treturn specularColor * AB.x + AB.y;\n" +
    "} // validated\n" +

    "float G_BlinnPhong_Implicit( /* const in float dotNL, const in float dotNV */ ) {\n" +
    "\t// geometry term is (n dot l)(n dot v) / 4(n dot l)(n dot v)\n" +
    "\treturn 0.25;\n" +
    "}\n" +

    "float D_BlinnPhong( const in float shininess, const in float dotNH ) {\n" +
    "\treturn RECIPROCAL_PI * ( shininess * 0.5 + 1.0 ) * pow( dotNH, shininess );\n" +
    "}\n" +

    "vec3 BRDF_Specular_BlinnPhong( const in IncidentLight incidentLight, const in GeometricContext geometry, const in vec3 specularColor, const in float shininess ) {\n" +
    "\tvec3 halfDir = normalize( incidentLight.direction + geometry.viewDir );\n" +
    "\tfloat dotNH = saturate( dot( geometry.normal, halfDir ) );\n" +
    "\tfloat dotLH = saturate( dot( incidentLight.direction, halfDir ) );\n" +
    "\tvec3 F = F_Schlick( specularColor, dotLH );\n" +
    "\tfloat G = G_BlinnPhong_Implicit( /* dotNL, dotNV */ );\n" +
    "\tfloat D = D_BlinnPhong( shininess, dotNH );\n" +
    "\treturn F * ( G * D );\n" +
    "} // validated\n" +

    "float GGXRoughnessToBlinnExponent( const in float ggxRoughness ) {\n" +
    "\treturn ( 2.0 / pow2( ggxRoughness + 0.0001 ) - 2.0 );\n" +
    "}\n" +

    "float BlinnExponentToGGXRoughness( const in float blinnExponent ) {\n" +
    "\treturn sqrt( 2.0 / ( blinnExponent + 2.0 ) );\n" +
    "}\n";
}
