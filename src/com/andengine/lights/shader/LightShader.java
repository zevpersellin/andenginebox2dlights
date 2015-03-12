package com.andengine.lights.shader;

import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.exception.ShaderProgramLinkException;
import org.andengine.opengl.shader.source.IShaderSource;
import org.andengine.opengl.shader.source.StringShaderSource;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;


public final class LightShader extends ShaderProgram 
{
	
		static String gamma = ""; 
//		if (RayHandler.getGammaCorrection())
//			gamma = "sqrt";
		static IShaderSource mVertexShaderSource = new StringShaderSource(
				"attribute vec4 "+ ShaderProgramConstants.ATTRIBUTE_POSITION +";\n"
				+ "attribute vec4 "  + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" 
				+ "attribute float s;\n"
				+ "uniform mat4 "  + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" 
				+ "varying vec4 "  + ShaderProgramConstants.VARYING_COLOR + ";\n" 			
				+ "void main()\n" 
				+ "{\n" 
				+ "   "  + ShaderProgramConstants.VARYING_COLOR + " = s * "  + ShaderProgramConstants.ATTRIBUTE_COLOR + ";\n" 			
				+ "   gl_Position =   "  + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * "+ ShaderProgramConstants.ATTRIBUTE_POSITION +";\n" 
				+ "}\n");

		
		 static final IShaderSource fragmentShader = new StringShaderSource(
				"precision lowp float;\n" +
				"varying vec4 " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
				"void main() {\n" +
				"	gl_FragColor = " + ShaderProgramConstants.VARYING_COLOR + ";\n" +
				"}");
		
		
		private LightShader()
		{
			super(mVertexShaderSource, fragmentShader);
		}
		private static LightShader INSTANCE;
		public static LightShader getInstance()
		{
			if (LightShader.INSTANCE == null) {
				LightShader.INSTANCE = new LightShader();
			}
			return LightShader.INSTANCE;
		}
	
		 @Override
		   protected void link(final GLState pGLState)
		         throws ShaderProgramLinkException {

		      GLES20.glBindAttribLocation(this.mProgramID,
		            ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
		            ShaderProgramConstants.ATTRIBUTE_POSITION);
		      GLES20.glBindAttribLocation(this.mProgramID, 
		    		  ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, 
		    		  ShaderProgramConstants.ATTRIBUTE_COLOR);		      
		      
		      super.link(pGLState);
		      sUniformData = this.getUniformLocation(ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX);

		   }
		   static int sUniformData = 0;

		   @Override
		   public void bind(final GLState pGLState,
		         final VertexBufferObjectAttributes pVertexBufferObjectAttributes) 
		   {
			   GLES20.glDisableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);
		      super.bind(pGLState, pVertexBufferObjectAttributes);

		      GLES20.glUniformMatrix4fv(
		    		  sUniformData,
		            1, false, pGLState.getModelViewProjectionGLMatrix(), 0);
		      
//		      GLES20.glUniform3f(sUniformData, mCentreX, mCentreY, mRadius);
		   }
		   
		   @Override
			public void unbind(final GLState pGLState) {
				GLES20.glEnableVertexAttribArray(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION);

				super.unbind(pGLState);
			}
}
