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
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import com.andengine.lights.shader.GlowShader;
public class GlowShaderTestActivity extends BaseGameActivity implements IOnSceneTouchListener{
	
	public static final int CAMERA_WIDTH = 1080;
	public static final int CAMERA_HEIGHT = 720;
	
	private Camera mCamera;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);

		return engineOptions;
	}
	
	@Override
    public Engine onCreateEngine(final EngineOptions pEngineOptions) 
	{
        return new LimitedFPSEngine(pEngineOptions, 60) ;
        	
			
	}
	GlowShader mGlowShader;
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)	throws IOException {
	
		mGlowShader = new GlowShader().getInstance();
		this.getShaderProgramManager().loadShaderProgram(mGlowShader);
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	 Scene scene;
	@Override
	public void  onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException
	{       
			this.mEngine.registerUpdateHandler(new FPSLogger());
	        scene = new Scene();	        	
	        scene.setOnSceneTouchListener(this);
	        createGlowRectangle();
	        pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			IGameInterface.OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException
	{			
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	public void createGlowRectangle()
	{	
        
        Rectangle rec = new Rectangle(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, CAMERA_WIDTH, CAMERA_HEIGHT, getVertexBufferObjectManager());
        rec.setColor(Color.BLUE);
        rec.setShaderProgram(mGlowShader);
        rec.setZIndex(11);
        rec.setBlendFunction(1,1);
        scene.attachChild(rec);
       
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{		
		float ratioX = mCamera.getSurfaceWidth()/mCamera.getWidth();
        float ratioY = mCamera.getSurfaceHeight()/mCamera.getHeight();
        float x = pSceneTouchEvent.getX();
        float y = pSceneTouchEvent.getY();
		mGlowShader.setCentre(x * ratioX, y * ratioY);
	    mGlowShader.setRadius(40.0f);
		return false;
	}
	
}
