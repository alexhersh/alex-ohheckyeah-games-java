package org.ohheckyeah.games.kacheout.media;

import org.ohheckyeah.games.kacheout.KacheOut;

import geomerative.RFont;
import geomerative.RG;

import com.haxademic.core.app.P;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.draw.mesh.MeshUtil;
import com.haxademic.core.system.FileUtil;

public class AssetLoader {
	
	protected KacheOut p;
	
	public AssetLoader() {
		p = (KacheOut) P.p;
	}
	
	public void createMeshPool() {
		// make sure geomerative is ready
		if( RG.initialized() == false ) RG.init( p );
		
		// load fonts
		RFont _fontHelloDenver = new RFont( FileUtil.getHaxademicDataPath() + "fonts/HelloDenverDisplay-Regular.ttf", 200, RFont.CENTER);
		RFont _fontBitLow = new RFont( FileUtil.getHaxademicDataPath() + "fonts/bitlow.ttf", 200, RFont.CENTER);
		
		// create denver
		p.meshPool.addMesh( KacheOut.CREATE_DENVER_TEXT, MeshUtil.mesh2dFromTextFont( p, _fontHelloDenver, null, 200, "ANOTHER GOOD IDEA FROM", -1, 2, 0.6f ), 1 );
//		p.meshPool.addMesh( KacheOut.CREATE_DENVER_LOGO, MeshUtil.meshFromSVG( p, FileUtil.getHaxademicDataPath() + "svg/create-denver-logo.svg", -1, 6, 0.6f ), 1 );
		
		// Kacheout logo
		p.meshPool.addMesh( KacheOut.KACHEOUT_LOGO, MeshUtil.meshFromImg( p, FileUtil.getHaxademicDataPath() + "games/kacheout/images/kacheout.gif", 1f ), 22f );
		p.meshPool.addMesh( KacheOut.KACHEOUT_LOGO_ALT, MeshUtil.meshFromImg( p, FileUtil.getHaxademicDataPath() + "games/kacheout/images/kacheout_alt.gif", 1f ), 22f );

		// Built by text & ufo
		p.meshPool.addMesh( KacheOut.BUILT_BY_TEXT, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "BUILT BY:", -1, 2, 0.5f ), 1 );
		p.meshPool.addMesh( KacheOut.UFO_1, MeshUtil.meshFromImg( p, FileUtil.getHaxademicDataPath() + "games/kacheout/images/ufo_1.gif", 1.4f ), 15f );
		p.meshPool.addMesh( KacheOut.UFO_2, MeshUtil.meshFromImg( p, FileUtil.getHaxademicDataPath() + "games/kacheout/images/ufo_2.gif", 1.4f ), 15f );
		p.meshPool.addMesh( KacheOut.UFO_3, MeshUtil.meshFromImg( p, FileUtil.getHaxademicDataPath() + "games/kacheout/images/ufo_3.gif", 1.4f ), 15f );
		
		// cacheflowe / mode set
		p.meshPool.addMesh( KacheOut.MODE_SET_LOGO, MeshUtil.meshFromOBJ( p, FileUtil.getHaxademicDataPath() + "models/mode-set.obj", 1f ), 150 );
		p.meshPool.addMesh( KacheOut.MODE_SET_LOGOTYPE, MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, FileUtil.getHaxademicDataPath() + "svg/modeset-logotype.svg", -1, 6, 0.35f ), 4 ), 1 );
		p.meshPool.addMesh( KacheOut.CACHEFLOWE_LOGO, MeshUtil.meshFromOBJ( p, FileUtil.getHaxademicDataPath() + "models/cacheflowe-3d.obj", 80f ), 1f );
		p.meshPool.addMesh( KacheOut.CACHEFLOWE_LOGOTYPE, MeshUtil.getExtrudedMesh( MeshUtil.meshFromSVG( p, FileUtil.getHaxademicDataPath() + "svg/cacheflowe-logotype.svg", -1, 6, 0.6f ), 4 ), 1 );

		// design credits
		p.meshPool.addMesh( KacheOut.DESIGN_BY, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "DESIGN BY:", -1, 2, 0.3f ), 1 );
		p.meshPool.addMesh( KacheOut.JON_DESIGN, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "JON TRAISTER", -1, 2, 0.4f ), 1 );
		p.meshPool.addMesh( KacheOut.RYAN_DESIGN, MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "LATENIGHT WEEKNIGHT", -1, 2, 0.4f ), 1 );
		
		// instructions screen
		p.meshPool.addMesh( KacheOut.STEP_UP_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "STEP UP", -1, 2, 0.4f ), 10 ), 1 );
		p.meshPool.addMesh( KacheOut.READY_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "READY!", -1, 2, 0.4f ), 10 ), 1 );
		
		// countdown
		p.meshPool.addMesh( KacheOut.COUNTDOWN_TEXT_1, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "1", -1, 2, 2f ), 40 ), 1 );
		p.meshPool.addMesh( KacheOut.COUNTDOWN_TEXT_2, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "2", -1, 2, 2f ), 40 ), 1 );
		p.meshPool.addMesh( KacheOut.COUNTDOWN_TEXT_3, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "3", -1, 2, 2f ), 40 ), 1 );
		
		// win/lose
		p.meshPool.addMesh( KacheOut.WIN_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "WIN", -1, 2, 0.7f ), 10 ), 1 );
		p.meshPool.addMesh( KacheOut.LOSE_TEXT, MeshUtil.getExtrudedMesh( MeshUtil.mesh2dFromTextFont( p, _fontBitLow, null, 200, "LOSE", -1, 2, 0.45f ), 10 ), 1 );
	}
	
	public void loadAudio( AudioPool sounds ) {
		sounds.loadAudioFile( KacheOut.PADDLE_BOUNCE, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/ball_hit_wall_v03.mp3" );
		sounds.loadAudioFile( KacheOut.WALL_BOUNCE, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/ball_hit_wall_v02.mp3" );
		sounds.loadAudioFile( KacheOut.INSERT_COIN, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/insert-coin.mp3" );
		sounds.loadAudioFile( KacheOut.COUNTDOWN_1, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/countdown-01.mp3" );
		sounds.loadAudioFile( KacheOut.COUNTDOWN_2, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/countdown-02.mp3" );
		sounds.loadAudioFile( KacheOut.COUNTDOWN_3, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/countdown-03.mp3" );
		sounds.loadAudioFile( KacheOut.COUNTDOWN_1_VOX, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/robot-1.mp3" );
		sounds.loadAudioFile( KacheOut.COUNTDOWN_2_VOX, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/robot-2.mp3" );
		sounds.loadAudioFile( KacheOut.COUNTDOWN_3_VOX, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/robot-3.mp3" );
		sounds.loadAudioFile( KacheOut.WIN_SOUND, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/win-notes.mp3" );
		sounds.loadAudioFile( KacheOut.READY_SOUND, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/ready.mp3" );
		sounds.loadAudioFile( KacheOut.LAUNCH_SOUND, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/crunch-kick-verb.mp3" );
		sounds.loadAudioFile( KacheOut.STEP_UP_SOUND, 1, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/step-up.mp3" );
		sounds.loadAudioFile( KacheOut.LOSE_BALL_SOUND, 0.85f, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/bad-saw.mp3" );
		sounds.loadAudioFile( KacheOut.SFX_DOWN, 0.85f, FileUtil.getHaxademicDataPath() + "games/kacheout/audio/sfx/efxdown2-faded.mp3" );
	}

}
