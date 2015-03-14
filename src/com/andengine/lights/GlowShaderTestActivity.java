package com.andengine.lights;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.shader.source.IShaderSource;
import org.andengine.opengl.shader.source.StringShaderSource;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;

import com.andengine.lights.shader.LightShader;

public class LightsTestActivity extends BaseGameActivity implements IOnSceneTouchListener{
	
	public static final int WIDTH = 720;
	public static final int HEIGHT = 480;
	
	private Camera mCamera;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, WIDTH, HEIGHT);
		
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(WIDTH, HEIGHT), this.mCamera);

		return engineOptions;
	}
	
	@Override
    public Engine onCreateEngine(final EngineOptions pEngineOptions) 
	{
        return new LimitedFPSEngine(pEngineOptions, 60) ;
        	
			
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)	throws IOException {
	

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	 Scene scene;
	@Override
	public void  onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException
	{       
			this.mEngine.registerUpdateHandler(new FPSLogger());
	        scene = new Scene();	        
	        scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));	        
	        getEngine().registerUpdateHandler(new FPSLogger());
	        scene.setOnSceneTouchListener(this);
	        pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			IGameInterface.OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException
	{			
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{		
		Rectangle r = new Rectangle(100f, 100f, 300f, 100f, getVertexBufferObjectManager());
		LightShader lightShader = LightShader.getInstance();
		r.setShaderProgram(lightShader);
		getShaderProgramManager().loadShaderProgram(lightShader);
		
		scene.attachChild(r);
		// TODO Auto-generated method stub
		return false;
	}
	
//	This is just a basic vector shader that andengine does... Its a vector that 
	/*
	 * AndEngine sends to OpenGL a list of points in space that represent what you are drawing to screen. 
	 * These are the vertices of your scene -- for example the 4 points on each corner of a Sprite.
	 *  The vertex shader is executed early in the pipeline and is called once for each vertex.

		The final goal of the vertex shader is to return a position where that vertex should be rendered on the screen.
		 It can do other things, like calculate normals, but that's not all that useful in a 2d world.
	 */
	static IShaderSource vShader = new StringShaderSource(
		    "uniform mat4 " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + ";\n" +
		    "attribute vec4 " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
		    "void main()        \n" +
		    "{  \n" +
		    "   gl_Position = " + ShaderProgramConstants.UNIFORM_MODELVIEWPROJECTIONMATRIX + " * " + ShaderProgramConstants.ATTRIBUTE_POSITION + ";\n" +
		    "}"
		);
	static IShaderSource fShader = new StringShaderSource(
		    "precision mediump float;           \n" +
		    "uniform vec4 theColor;             \n" +
		    "void main()                                        \n" +
		    "{                                                  \n" +
		    "   gl_FragColor = theColor;        \n" +
		    "}"
		);
	
}
