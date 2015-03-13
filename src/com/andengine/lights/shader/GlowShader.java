GlowShaderTestActivity.javaGlowShaderTestActivity.javapackage com.andengine.lights.shader;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.shader.source.IShaderSource;
import org.andengine.opengl.shader.source.StringShaderSource;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

/*
 * Author: "F.G.N. M." 
 */
public class GlowShader extends ShaderProgram {
    
    private static GlowShader INSTANCE;
    
    private float mCentreX, mCentreY, mRadius;
    
    private static final String UNIFORM_DATA = "u_data";
    public static int sUniformData = ShaderProgramConstants.LOCATION_INVALID;
    
    
    static IShaderSource mVertexShaderSource = new StringShaderSource(
            "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION
                    + ";\n" + "attribute vec2 "
                    + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES
                    + ";\n"
                    + "uniform mat4 "
                    + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX
                    + ";\n" + "varying vec2 vTexCoord1;\n"
                    + "varying vec4 vPosition;\n"
                    + "void main(void) {\n" + "vPosition = "
                    + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n"
                    + "vTexCoord1 = "
                    + ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES
                    + ";\n"
                    + "gl_Position = "
                    + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX
                    + " * vPosition;\n" + "}");
    
    static IShaderSource mFragmentShaderSource = new StringShaderSource("precision highp float;" +
    		"\n uniform vec3 u_data;" +
    		"\n void main()\n " +
    		"{ \n vec2 lightpos = vec2(u_data.x, -(u_data.y-720.0));" +
    		"\n vec4 lightColor = vec4(1.0,1.0,1.0,1.0);\n " +
    		"float screenHeight = 720.0;\n " +
    		"vec3 lightAttenuation = vec3(1.0,1.0,1.0);\n " +
    		"vec2 pixel=gl_FragCoord.xy;\n " +
    		"pixel.y=screenHeight-pixel.y;\n " +
    		"vec2 aux=lightpos-pixel;\n " +
    		"float distance=length(aux)/u_data.z;\n " +
    		"float attenuation=1.0/(lightAttenuation.x+lightAttenuation.y*distance+lightAttenuation.z*distance*distance);\n " +
    		"vec4 color=vec4(attenuation,attenuation,attenuation,1.0)*lightColor;\n gl_FragColor = color;\n }");
    
    
    public GlowShader() {
        super(mVertexShaderSource, mFragmentShaderSource);
    }

    @Override
    protected void link(final GLState pGLState)
            throws ShaderProgramLinkException {

        GLES20.glBindAttribLocation(this.mProgramID,
                ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
                ShaderProgramConstants.ATTRIBUTE_POSITION);

        GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES);
        
        super.link(pGLState);

        PositionTextureCoordinatesShaderProgram.sUniformModelViewPositionMatrixLocation = this
                .getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
        
        GlowShader.sUniformData = this.getUniformLocation(GlowShader.UNIFORM_DATA);
    }

    @Override
    public void bind(final GLState pGLState,
            final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        super.bind(pGLState, pVertexBufferObjectAttributes);

        GLES20.glUniformMatrix4fv(
                PositionTextureCoordinatesShaderProgram.sUniformModelViewPositionMatrixLocation,
                1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
        
        GLES20.glUniform3f(GlowShader.sUniformData, mCentreX, mCentreY, mRadius);
    }

    public static GlowShader getInstance() {
        if(GlowShader.INSTANCE == null) {
        	GlowShader.INSTANCE = new GlowShader();
        }
        return GlowShader.INSTANCE;
    }
    
    public void setCentre(float pX, float pY){
        mCentreX = pX;
        mCentreY = pY;
    }
    
    public void setRadius(float pRadius){
        mRadius = pRadius;
    }

}

