![OhHeckYeah](https://raw.githubusercontent.com/ohheckyeah/web-content/master/game-titles/big-blue's-hood-slam-title.png)


Big Blue's Hood Slam
==========

Big Blue's Hood Slam is a 2-player Kinect-based video game, and part of the inagural [OhHeckYeah](http://ohheckyeah.com) street arcade. Guide Denver's Blue Bear along the journey from the mountains back to Big Blue's home at the Colorado Convention Center. Players must avoid the obstacles placed in the way by the mischievous flying saucer. Big Blue's Hood Slam was designed by Legwork and developed by Mode Set.

Watch the trailer on [Vimeo](https://vimeo.com/95688462).

##### Running Catchy (from OS X)


* Make sure the machine is set to 1024 x 768
	* Open System Preferences
	* Click Displays
	* When plugged into a projector, make sure to "mirror" the displays at this resolution in the "Arrangement" tab
	* Set "Resolution" to "Scaled" and click 1024x768
* Make sure the Kinect is plugged into a USB port on the computer and plugged into a power outlet
* Start Eclipse from the Mac Dock or Applications directory
* If you've pulled updated code via GitHub, make sure to follow the update instructions below
* Find the larger green "play" button in the top bar in Eclipse, and click the tiny "down arrow" to the right
* Select "BlueBear" and the game will launch
* If any error windows pop up, make sure to press "Ignore", and (force) quit the game as described below
	* I've had to unplug and replug the Kinect USB once when I kept getting errors on launch, but this shouldn't happen


##### TODO

* Sidewalk people should look at bear
* Track game metrics
* Rebuild clouds layer
* Refine squirrel beam / obstacle launch animation sequence
* Build keystone handles into game PGraphics
* Enable reversed controls (multiply kinect region z value by -1)
* Sidewalk elements move at different speed than buildings when game is slowing down [bug]
* Update sky & sidewalk colors per neighborhood - need colors from Legwork
* Neighborhoods should be read in via directory structure for better reuse down the road


![OhHeckYeah](https://avatars2.githubusercontent.com/u/6227089?s=140)
