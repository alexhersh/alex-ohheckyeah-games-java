package org.ohheckyeah.shared.app;

import java.io.File;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

import com.haxademic.core.app.P;
import com.haxademic.core.app.PAppletHax;
import com.haxademic.core.draw.util.DrawUtil;
import com.haxademic.core.system.FileUtil;

@SuppressWarnings("serial")
public class OHYPechaKucha
extends PAppletHax {
	
	protected ArrayList<ISlideShow> _slideshows;
	protected ISlideShow _curSlideshow;
	protected int _slideshowIndex;
	
	public static void main(String args[]) {
		_isFullScreen = true;
		_hasChrome = false;
		boolean isSecondScreen = false;
		if( isSecondScreen ) {
			PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", "--location=-1280,0", "--display=1", OHYPechaKucha.class.getName() });		// requires hiding os x 2nd screen menu bar: http://www.cultofmac.com/255910/hide-the-menu-bar-on-your-secondary-monitor-with-mavericks-os-x-tips/
		} else {
			PApplet.main(new String[] { "--hide-stop", "--bgcolor=000000", OHYPechaKucha.class.getName() });
		}
	}

	protected void overridePropsFile() {
		_appConfig.setProperty( "fullscreen", "true" );
		_appConfig.setProperty( "fills_screen", "false" );
		_appConfig.setProperty( "width", "1100" );
		_appConfig.setProperty( "height", "462" );
		
		// imagemagick helper: 
		// $ mogrify -thumbnail 1100x462 -background black -gravity center -extent 1100x462 *.jpg
		// strip colorspace that jacks up images
		// $ mogrify -format jpg -colorspace RGB -strip -quality 99% *.jpg
	}
	
	public void setup() {
		super.setup();

		String pkDir = FileUtil.getHaxademicDataPath() + "images/pechakucha_ohy_2014/";
		
		_slideshows = new ArrayList<ISlideShow>();
		_slideshows.add(new ManualSlideShow(pkDir + "00 Opening slides/"));
		_slideshows.add(new AutoSlideShow(pkDir + "01 ERIC FARONE/"));
		_slideshows.add(new AutoSlideShow(pkDir + "02 KATIE WATSON & TARA PATTIE/"));
		_slideshows.add(new AutoSlideShow(pkDir + "03 Justin Gitlin/"));
		_slideshows.add(new AutoSlideShow(pkDir + "04 TALYA DORNBUSH/"));
		_slideshows.add(new AutoSlideShow(pkDir + "05 AARON RAY/"));
		_slideshows.add(new AutoSlideShow(pkDir + "06 LAURA BOND/"));
		_slideshows.add(new AutoSlideShow(pkDir + "07 DAVID EHRLICH/"));
		_slideshows.add(new ManualSlideShow(pkDir + "100 Closing slides/"));
		
		_slideshowIndex = 0;
		_curSlideshow = _slideshows.get(_slideshowIndex);
	}
	
	public void drawApp() {
		p.background( 0 );
		
		DrawUtil.setDrawCorner(p);
		
		_curSlideshow.update();
	}
	
	public void nextSlideshow() {
		_slideshowIndex++;
		if(_slideshowIndex > _slideshows.size()-1) _slideshowIndex = _slideshows.size()-1;
		_curSlideshow = _slideshows.get(_slideshowIndex);
	}
	
	public void prevSlideshow() {
		_slideshowIndex--;
		if(_slideshowIndex < 0) _slideshowIndex = 0;
		_curSlideshow = _slideshows.get(_slideshowIndex);
		if(_curSlideshow instanceof AutoSlideShow) {
			_curSlideshow.reset();
		}
	}
	
	
	// key commands
	public void keyPressed() {
		// P.println(p.keyCode);
		if(p.keyCode == 39) {
			_curSlideshow.next();
		} else if(p.keyCode == 37) {
			_curSlideshow.prev();
		}
	}

	
	public interface ISlideShow {
		public void prev();
		public void next();
		public void update();
		public void reset();
	}

	// opening/closing slide object ----------------------------------------------------------------------
	public class ManualSlideShow implements ISlideShow {
		
		protected ArrayList<PImage> _images;
		protected int _index;
		
		public ManualSlideShow( String path ) {
			_images = getFilesInDirOfType(path, "png");
			_index = 0;
		}

		public void reset() {
			_index = 0;
		}
		
		public void prev() {
			_index--;
			if(_index < 0) {
				_index = 0;
				prevSlideshow();	// call to main app
			}
		}
		
		public void next() {
			_index++;
			if(_index > _images.size()-1) {
				_index = _images.size()-1;
				nextSlideshow();	// call to main app
			}
		}
		
		public void update() {
			p.image(_images.get(_index), 0, 0, p.width, p.height); // this is the stupid part of dealing with 14th st screen, since it's stretched
		}
	}
	
	// auto-advance slide object ----------------------------------------------------------------------
	public class AutoSlideShow implements ISlideShow {
		
		protected ArrayList<PImage> _images;
		protected int _index;
		protected int _startMillis = -1;
		protected int _slideTime = 20*1000;
		
		public AutoSlideShow( String path ) {
			_images = getFilesInDirOfType(path, "png");
			_index = 0;
		}
		
		public void reset() {
			_index = 0;
			resetTimer();
		}
		
		public void prev() {
			if(_index > 0) {
				reset();
			} else {
				reset();
				prevSlideshow();	// call to main app
			}
		}
		
		public void next() {
			if(_index == 0) {
				_index++;
				startTimer();
			} else { // if(_index == _images.size()-1) {
				nextSlideshow();	// call to main app
				reset();
			}
		}
		
		protected void startTimer() {
			_startMillis = p.millis();
		}
		
		protected void resetTimer() {
			_startMillis = -1;
		}
		
		public void update() {
			p.image(_images.get(_index), 0, 0, p.width, p.height); // this is the stupid part of dealing with 14th st screen, since it's stretched
			
			// auto-advance
			if(_startMillis != -1 && p.millis() > _startMillis + _slideTime) {
				_index++;
				if( _index < _images.size()-1 ) {
					startTimer();
				} else {
					resetTimer();
				}
			}
			
			// draw progress
			if(_startMillis != -1) {
				float progress = (p.millis() - _startMillis) / (float) _slideTime;
				p.noStroke();
				p.fill(255, 127);
				p.rect(0, p.height - 4, progress * p.width, 4);
				p.fill(0, 127);
				p.rect(progress * p.width, p.height - 4, 4, 4);
			}
		}
	}
	
	
	// image directory reader helper ----------------------------------------------------------------------
	public ArrayList<PImage> getFilesInDirOfType( String directory, String type ) {
		type = "."+type;
		File dir = new File( directory );
		String[] children = dir.list();
		ArrayList<PImage> filesOfType = new ArrayList<PImage>();
		if (children == null) {
			P.println("Error: couldn't find file or directory");
		} else {
		    for (int i=0; i < children.length; i++) {
		        String filename = children[i];
		        if( filename.toLowerCase().indexOf( ".png" ) != -1 || filename.toLowerCase().indexOf( ".jpg" ) != -1 ) {	
		        	P.println(filename);
		        	filesOfType.add( p.loadImage(directory + filename) );
		        }
		    }
		}
		return filesOfType;
	}


}
