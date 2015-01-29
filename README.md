![OhHeckYeah](https://avatars2.githubusercontent.com/u/6227089?s=140)
OhHeckYeah
==========

The [OhHeckYeah](http://ohheckyeah.com) video games, written with Java & [Procesing](http://processing.org), built on top of [Haxademic](https://github.com/cacheflowe/haxademic/).

===

## Installing / Compiling

* If you're on OS X, it's helpful to see hidden files. Run this command in Terminal:
	* `defaults write com.apple.finder AppleShowAllFiles YES`
* Download the standard Eclipse IDE for Java development, and the Java Development Kit itself:
	* [Eclipse (standard 32-bit)](http://www.eclipse.org/)
	* [Java for OS X](http://support.apple.com/kb/DL1572) and  or [JDK for Windows and other platforms](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
	* You'll also need [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) or [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) if you want to use the Leap Motion. After installing these later JDK libraries, click **Eclipse -> Preferences** then **Java -> Installed JREs**, and click **Search...** to have Eclipse find the newly-installed library.
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
	* This directory should be renamed to `LeapMotionForProcessing`
	* Right-click the project root folder and click **Properties**
		* Click the **Java Build Path** section, and click the **Libraries** tab
		* Click the accordion arrow next to `LeapJava.jar` and `LeapMotionForProcessing.jar`
			* For each, select `Native Library Location`, then the `Edit...` buttons to the right
			* Click `External Folder...` and find the directory with the c++ libraries for your platform. For OS X, it would look something like: `ohheckyeah-games-java/lib/LeapMotionForProcessing/library/macosx`
	* Make sure you've installed the latest Leap Motion software from the system control panel and turn on auto-updates
	* Also make sure to install the latest Leap Motion [SDK](https://developer.leapmotion.com/downloads)
	* Run the Leap Motion app, calibrate your Leap, and make sure that the red Infrared lights are turned on and that the unit is running
* In the **Package Explorer** in Eclipse, right-click the `lib` directory and select **Refresh**. This will let Eclipse know that you've added the appropriate libraries on your file system.
* Make sure you're compiling with at least Java SE 6 (1.6):
	* Right-click the `ohheckyeah-games-java` project in the **Package Explorer** or **Navigator** window and click **Properties**
	* Click the **Java Compiler** section and check the **Enable project specific settings** box on the right
	* Select **1.6** as your **Compiler compliance level**, if possible
	* If "Configure the **Installed JREs**" is shown at the bottom of this window, click that, make sure the **1.6** item is checked, then click OK.
* If you're using a Leap Motion controller, make sure you're compiling with Java SE 7 (1.7) or Java SE 8 (1.8):
	* In **Run Configurations** for your application, click the **JRE** tab and select **Alternate JRE**, then select **Java SE 7** or **Java SE 8** from the dropdown
* Right-click on any of the games' main .java files within `src/org/ohheckyeah/games` (**Catchy.java**, for example) and choose **Run As -> Java Application** from the menu. This will create a run configuration for the game, but you'll likely crash due to running out of memory.
* In **Run -> Run Configurations**, select your app and add the following **VM Arguments** when running the Java Application to increase memory allocated to your app. This is a minimum of 1gb and a maximum of 2gb of RAM:
	* `-Xms1G`
	* `-Xmx2G`
* Run the application again, and you should hopefully have a game running, with mock controls.
* Find the game's .properties file in `data/properties` to turn on a hardware controller by setting `kinect_active` or `leap_active` to true


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

General Use / Tips

* If you want to wipe your `bin/` directory, you'll have to do a **Project -> Clean…** in Eclipse afterwards.


