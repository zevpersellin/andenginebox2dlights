package com.andengine.lights.shader;

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
    
    private float mCentreX, mCentreY, mRadius, mScreenHeight, mIntensity;
    
    private static final String UNIFORM_DATA = "u_data";
    private static final String UNIFORM_INTENSITY = "intensity";
    public static int sUniformData = ShaderProgramConstants.LOCATION_INVALID;
    public static int iUniformData = ShaderProgramConstants.LOCATION_INVALID;
    
    
    static IShaderSource mVertexShaderSource = new StringShaderSource(
            "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION
                    + ";\n"
                    + "uniform mat4 "
                    + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n"
                    + "varying vec4 vPosition;\n"
                    + "void main(void) {\n" + "vPosition = "
                    + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n"
                    + "gl_Position = "
                    + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX
                    + " * vPosition;\n" + "}");
    
    static IShaderSource mFragmentShaderSource = new StringShaderSource("precision highp float;" +
    		"\n uniform vec4 u_data;" +
    		"\n uniform float intensity;" +
    		"\n void main()\n " +
    		"{ \n vec2 lightpos = vec2(u_data.x, -(u_data.y-u_data.w));" +
    		"\n vec4 lightColor = vec4(1.0,1.0,1.0,1.0);\n " +
    		"float screenHeight = u_data.w;\n " +
    		"vec3 lightAttenuation = vec3(1.0,1.0,1.0) * intensity;\n " +
    		"vec2 pixel=gl_FragCoord.xy;\n " +
    		"pixel.y=screenHeight-pixel.y;\n " +
    		"vec2 aux=lightpos-pixel;\n " +
    		"float distance=length(aux)/u_data.z;\n " +
    		"float attenuation=1.0/(lightAttenuation.x+lightAttenuation.y*distance+lightAttenuation.z*distance*distance);\n " +
    		"vec4 color=vec4(attenuation,attenuation,attenuation,1.0)*lightColor;\n gl_FragColor = color;\n }");
    
    
    public GlowShader(float screenHeight) {
        super(mVertexShaderSource, mFragmentShaderSource);
        setScreenHeight(screenHeight);
    }

    @Override
    protected void link(final GLState pGLState)
            throws ShaderProgramLinkException {

        GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION);

        super.link(pGLState);

        PositionTextureCoordinatesShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
        
        GlowShader.sUniformData = this.getUniformLocation(GlowShader.UNIFORM_DATA);
        GlowShader.iUniformData = this.getUniformLocation(GlowShader.UNIFORM_INTENSITY);
    }

    @Override
    public void bind(final GLState pGLState,
            final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
        super.bind(pGLState, pVertexBufferObjectAttributes);

        GLES20.glUniformMatrix4fv(PositionTextureCoordinatesShaderProgram.sUniformModelViewPositionMatrixLocation, 1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
        
        GLES20.glUniform4f(GlowShader.sUniformData, mCentreX, mCentreY, mRadius, mScreenHeight);
        GLES20.glUniform1f(GlowShader.iUniformData, mIntensity);
    }
    
    public void setCentre(float pX, float pY){
        mCentreX = pX;
        mCentreY = pY;
    }
    
    public void setRadius(float pRadius){
        mRadius = pRadius;
    }
    
    public void setIntensity(float intensity){
    	mIntensity = intensity;
    }
    
    protected void setScreenHeight(float height){
    	mScreenHeight = height;
    }

}

