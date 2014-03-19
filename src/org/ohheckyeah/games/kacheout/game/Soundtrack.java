package org.ohheckyeah.games.kacheout.game;

import java.util.ArrayList;

import org.ohheckyeah.games.kacheout.KacheOut;

import com.haxademic.core.app.P;
import com.haxademic.core.math.MathUtil;
import com.haxademic.core.system.FileUtil;

import ddf.minim.AudioPlayer;

public class Soundtrack {
	
	protected KacheOut p;
	protected AudioPlayer _backgroundAudio = null;
	protected ArrayList<String> _soundtrackFiles;
	protected int _index = 0;
	
	public Soundtrack() {
		p = (KacheOut) P.p;
		
		_soundtrackFiles = FileUtil.getFilesInDirOfType( FileUtil.getHaxademicDataPath() + "games/kacheout/audio/soundtrack", "mp3" );
		_index = MathUtil.randRange( 0, _soundtrackFiles.size() - 1 );
		
		playNext();
		stop();
	}
	
	public void playNext() {
		_index++;
		if( _index == _soundtrackFiles.size() ) _index = 0;
		if( _backgroundAudio != null ) _backgroundAudio.close();
		_backgroundAudio = p._minim.loadFile(FileUtil.getHaxademicDataPath() + "games/kacheout/audio/soundtrack/" + _soundtrackFiles.get( _index ), 512);
		_backgroundAudio.loop();
	}
	
	public void playIntro() {
		if( _backgroundAudio != null ) _backgroundAudio.close();
		_backgroundAudio = p._minim.loadFile(FileUtil.getHaxademicDataPath() + "games/kacheout/audio/screens/credits.mp3", 512);
		_backgroundAudio.loop( 0 );
	}
	
	public void playInstructions() {
		if( _backgroundAudio != null ) _backgroundAudio.close();
		_backgroundAudio = p._minim.loadFile(FileUtil.getHaxademicDataPath() + "games/kacheout/audio/screens/player-wait.mp3", 512);
		_backgroundAudio.loop();
	}
	
	public void mute( boolean mute ) {
		if( _backgroundAudio != null ) {
			if( mute == true ) 
				_backgroundAudio.mute();
			else
				_backgroundAudio.unmute();
		}
	}
	
	public void stop() {
		if( _backgroundAudio != null && _backgroundAudio.isPlaying() ) _backgroundAudio.pause();		
	}
}
