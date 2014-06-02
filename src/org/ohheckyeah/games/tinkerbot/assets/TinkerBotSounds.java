package org.ohheckyeah.games.tinkerbot.assets;

import org.ohheckyeah.shared.app.OHYBaseGame;

import com.haxademic.core.app.P;
import com.haxademic.core.audio.AudioPool;
import com.haxademic.core.system.FileUtil;

import ddf.minim.AudioPlayer;

public class TinkerBotSounds {
	
	protected OHYBaseGame p;
	
	protected AudioPlayer _introSound;
	protected AudioPlayer _waitingSound;
	protected AudioPlayer _gameplaySound;
	protected AudioPlayer _winSound;
	protected AudioPlayer _loseSound;
	protected AudioPlayer _ohySound;
	
	protected AudioPlayer _curSoundtrack;
	
	protected AudioPool _soundEffects;

	public static final String STEP_IN = "step-in";
	public static final String STEP_OUT = "step-out";
	public static final String PLAYER_LOCKED = "player-locked";
	public static final String PLAYERS_DETECTED = "players-detected";
	public static final String COUNTDOWN = "countdown";
	public static final String GAMEPLAY_START = "gameplay-start";
	public static final String LASER = "laser";
	public static final String ERROR = "error";
	
	public TinkerBotSounds() {
		p = (OHYBaseGame)P.p;
		
		loadSoundtracks();
		loadSoundEffects();
	}
	
	protected void loadSoundtracks() {
		_introSound = 		p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/soundtrack/title-screen.wav", 512 );
		_waitingSound = 	p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/soundtrack/waiting-loop.wav", 512 );
		_gameplaySound = 	p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/soundtrack/tinkerbot-soundtrack.wav", 512 );
		_winSound = 		p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/bluebear/audio/soundtrack/win.wav", 512 );
		_loseSound = 		p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/bluebear/audio/soundtrack/lose.wav", 512 );
		_ohySound = 		p.minim.loadFile( FileUtil.getHaxademicDataPath() + "games/bluebear/audio/soundtrack/hug-it-out.wav", 512 );
	}
	
	public void playIntro() { playSoundtrack( _introSound ); }
	public void fadeOutIntro() { _introSound.shiftGain(0, -17, 2000); }
	public void playWaiting() { playSoundtrack( _waitingSound ); }
	public void playGameplay() { playSoundtrack( _gameplaySound ); }
	public void playWin() { playSoundtrack( _winSound ); }
	public void playLose() { playSoundtrack( _loseSound ); }
	public void playOHY() { playSoundtrack( _ohySound ); }
	
	protected void playSoundtrack( AudioPlayer newSoundTrack ) {
		if( _curSoundtrack != null ) _curSoundtrack.pause();
		_curSoundtrack = newSoundTrack;
		_curSoundtrack.setGain(0);
		_curSoundtrack.play(0);
		if( _curSoundtrack != _winSound && _curSoundtrack != _loseSound && _curSoundtrack != _ohySound ) _curSoundtrack.loop(-1);
	}
	
	public void stopSoundtrack() {
		if( _curSoundtrack != null ) _curSoundtrack.pause();
		_curSoundtrack = null;
	}
		
	public void loadSoundEffects() {
		_soundEffects = new AudioPool( p, p.minim );

		_soundEffects.loadAudioFile( TinkerBotSounds.PLAYER_LOCKED, 1, 		FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/trinkle.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.PLAYERS_DETECTED, 1, 	FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/player-detected.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.STEP_IN, 1, 			FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/player-enter.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.STEP_OUT, 1, 			FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/player-leave-2.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.COUNTDOWN, 1f,			FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/countdown.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.GAMEPLAY_START, 1f,	FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/crash.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.LASER, 1f,				FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/laser.wav" );
		_soundEffects.loadAudioFile( TinkerBotSounds.ERROR, 1f,				FileUtil.getHaxademicDataPath() + "games/tinkerbot/audio/sfx/error-big-deep-note.wav" );
	}

	public void playSound( String id ) {
		_soundEffects.playSound( id );
	}
}
