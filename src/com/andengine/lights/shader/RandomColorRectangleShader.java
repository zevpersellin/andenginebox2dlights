package com.andengine.lights.shader;

import java.util.Random;
import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.shader.source.IShaderSource;
import org.andengine.opengl.shader.source.StringShaderSource;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

public class RandomColorRectangleShader  extends ShaderProgram 
{
	static IShaderSource pVertexShaderSource = new StringShaderSource(
		    "uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
		    "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
		    "void main()        \n" +
		    "{  \n" +
		    "   gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
		    "}"
		);
	static IShaderSource pFragmentShaderSource = new StringShaderSource(
		    "precision mediump float;           \n" +
		    "uniform vec4 theColor;             \n" +
		    "void main()                                        \n" +
		    "{                                                  \n" +
		    "   gl_FragColor = theColor;        \n" +
		    "}"
		);
	private RandomColorRectangleShader() {
		super(pVertexShaderSource, pFragmentShaderSource);
	}
	
	private static RandomColorRectangleShader INSTANCE;
	public static RandomColorRectangleShader getInstance()
	{
		if (RandomColorRectangleShader.INSTANCE == null) {
			RandomColorRectangleShader.INSTANCE = new RandomColorRectangleShader();
		}
		return RandomColorRectangleShader.INSTANCE;
	}
	
	public int theColorLocation = ShaderProgramConstants.LOCATION_INVALID;
    private Random r = new Random();
                   
    @Override
    protected void link(final GLState pGLState) throws ShaderProgramLinkException {
   
         GLES20.glBindAttribLocation(this.mProgramID, ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION);

         super.link(pGLState);

         PositionTextureCoordinatesShaderProgram.sUniformModelViewPositionMatrixLocation = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);
         theColorLocation = this.getUniformLocation("theColor");
    }  

   @Override
   public void bind(final GLState pGLState, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
       super.bind(pGLState, pVertexBufferObjectAttributes);
       GLES20.glUniformMatrix4fv(PositionTextureCoordinatesShaderProgram.sUniformModelViewPositionMatrixLocation, 
    		   1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
       GLES20.glUniform4f(theColorLocation, r.nextFloat(), r.nextFloat(), r.nextFloat(), 0.3f);
   }

}
