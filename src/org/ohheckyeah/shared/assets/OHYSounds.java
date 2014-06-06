package org.ohheckyeah.shared.assets;

import org.ohheckyeah.shared.app.OHYBaseGame;

import com.haxademic.core.app.P;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.system.FileUtil;

import ddf.minim.AudioPlayer;

public class OHYSounds {
	
	protected OHYBaseGame p;
	
	protected AudioPlayer _ohySound;
	protected AudioPlayer _introSound;
	protected AudioPlayer _curSoundtrack;
	protected AudioPool _soundEffects;

	public OHYSounds() {
		p = (OHYBaseGame)P.p;
	}
	
	protected void loadSoundtracks() {
		_ohySound = p.minim.loadFile( FileUtil.getHaxademicDataPath() + "audio/ohy-clip-2.wav", 512 );
	}
	
	public void playOHY() { playSoundtrack( _ohySound, false ); }
	public void playIntro() {} // override this please
	public void fadeOutIntro() {} // override this please
	
	protected void playSoundtrack( AudioPlayer newSoundTrack, boolean loops ) {
		if( _curSoundtrack != null ) _curSoundtrack.pause();
		_curSoundtrack = newSoundTrack;
		_curSoundtrack.setGain(0);
		_curSoundtrack.play(0);
		if( loops ) _curSoundtrack.loop(-1);
	}

	public void stopSoundtrack() {
		if( _curSoundtrack != null ) _curSoundtrack.pause();
		_curSoundtrack = null;
	}

	public void playSound( String id ) {
		_soundEffects.playSound( id );
	}

}
