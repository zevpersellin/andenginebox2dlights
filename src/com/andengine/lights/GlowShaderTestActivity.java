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
import org.andengine.util.adt.color.Color;

import com.andengine.lights.shader.GlowShader;
import com.andengine.lights.shader.LightShader;
import com.andengine.lights.shader.RandomColorRectangleShader;

public class GlowShaderTestActivity extends BaseGameActivity implements IOnSceneTouchListener{
	
	public static final int WIDTH = 1080;
	public static final int HEIGHT = 720;
	
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
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException
	{			
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{		
		GlowShader mGlowShader = new GlowShader().getInstance(); 
		float ratioX = mCamera.getSurfaceWidth()/mCamera.getWidth();
        float ratioY = mCamera.getSurfaceHeight()/mCamera.getHeight();
        mGlowShader.setCentre(100.0f * ratioX, 100.0f * ratioY);
        mGlowShader.setRadius(30.0f);
        
        Rectangle rec = new Rectangle(WIDTH / 2, HEIGHT / 2, 400, 400, getVertexBufferObjectManager());
        rec.setColor(Color.TRANSPARENT);
        rec.setShaderProgram(mGlowShader);
        rec.setZIndex(11);
        rec.setBlendFunction(1,1);
        scene.attachChild(rec);

		return false;
	}
	
}
