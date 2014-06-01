![OhHeckYeah](https://avatars2.githubusercontent.com/u/6227089?s=140)
OhHeckYeah
==========

The [OhHeckYeah](http://ohheckyeah.com) video games, written with Java & [Procesing](http://processing.org), built on top of [Haxademic](https://github.com/cacheflowe/haxademic-2/).

===

## Installing / Compiling

* If you're on OS X, it's helpful to see hidden files. Run this command in Terminal:
	* `defaults write com.apple.finder AppleShowAllFiles YES`
* Download the standard Eclipse IDE for Java development, and the Java Development Kit itself:
	* [Eclipse (standard 32-bit)](http://www.eclipse.org/)
	* [Java for OS X](http://support.apple.com/kb/DL1572) or [JDK for Windows and other platforms](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Clone or [download](https://github.com/ohheckyeah/ohheckyeah-games-java/archive/master.zip) the `ohheckyeah-games-java` project
* Open Eclipse and: **File -> Import -> General / Existing Projects into Workspace**
	* Choose the `ohheckyeah-games-java` directory that you cloned/downloaded, press **Finish**, and the project should be ready to use, albeit with some errors.
* [Download](http://processing.org) and install the Processing 2.x core libraries (they're too big to include in this project). Add the jars to your build path, as well as the libraries that come with Processing (video, minim, etc.):
	* Download Processing and right-click the application. Select **Show Package Contents**
	* Within the package, navigate to `Contents/Java/core/library`
		* Copy the contents of this directory to `ohheckyeah-games-java/lib/processing-2/core` (create this directory if it doesn't exist)
	* Within the application package again, navigate to `Contents/Java/modes/java/libraries`, and again copy the contents. Paste them into `ohheckyeah-games-java/lib/processing-2/libraries`
* [Download](http://code.google.com/p/simple-openni/downloads/list) and install the latest SimpleOpenNI (Kinect) drivers with the [instructions](http://code.google.com/p/simple-openni/wiki/Installation) for your particular platform.
	* Unzip and copy the `SimpleOpenNI` directory into your project's `lib` folder
	* This is most likely to work with the 1st-gen Kinect model 1414
		* If you have a model 1473 Kinect camera, you might try [this build](http://intermedia.itu.dk/1473/) of the SimpleOpenNI library, but this isn't guaranteed to work
* [Download](https://github.com/voidplus/leap-motion-processing/archive/master.zip) and install the latest Leap Motion for Processing library.
	* Unzip and copy the `leap-motion-processing` directory into your project's `lib` folder
* In the **Package Explorer** in Eclipse, right-click the `lib` directory and select **Refresh**. This will let Eclipse know that you've added the appropriate libraries on your file system.
* Make sure you're compiling with Java 1.6:
	* Right-click the `ohheckyeah-games-java` project in the **Package Explorer** or **Navigator** window and click **Properties**
	* Click the **Java Compiler** section and check the **Enable project specific settings** box on the right
	* Select **1.6** as your **Compiler compliance level**, if possible
	* If "Configure the **Installed JREs**" is shown at the bottom of this window, click that, make sure the **1.6** item is checked, then click OK.
* Right-click on a any of the games within `src` and choose **Run As -> Java Applet** from the menu. Hopefully you're seeing something awesome at this point.

OhHeckYeah uses the following Java & Processing libraries, which I've included in this repository unless they're too big:

* [Processing](http://processing.org/) (view the [Processing for Eclipse instructions](http://processing.org/learning/eclipse/))
* [ESS](http://www.tree-axis.com/Ess/)
* [simple-openni](http://code.google.com/p/simple-openni/)
* [toxiclibs](http://toxiclibs.org/)
* [OBJLoader](http://code.google.com/p/saitoobjloader/)
* [oscP5](http://www.sojamo.de/libraries/oscP5/)
* [minim](http://code.compartmental.net/tools/minim/)
* [Geomerative](http://www.ricardmarxer.com/geomerative/)
* [UDP Processing Library](http://ubaa.net/shared/processing/udp/)
* [ControlP5](http://www.sojamo.de/libraries/controlP5/)
* [Leap Motion for Processing](https://github.com/voidplus/leap-motion-processing/)

Use the following VM Arguments when running the Java Application to increase memory allocated to your app

* `-Xms1024M`
* `-Xmx2048M`

General Use / Tips

* If you want to wipe your `bin/` directory, you'll have to do a **Project -> Clean…** in Eclipse afterwards.


