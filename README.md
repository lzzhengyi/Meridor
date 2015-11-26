# Meridor
A Java application recreating an online strategy web browser game.

To play: compile the src files into a .jar file.
Make sure the image icons are in the same folder as the compiled, runnable .jar.

Run the .jar, then start a new campaign!
You can also save and load a campaign if you wish to put the game down and play it later.

11/26 Update:
The campaign mode has been fully implemented for a while, but the campaign victory screen is a work in progress
Items are fully implemented
Enemy AI is fully implemented
Terrain features are fully implemented
The UI is currently considered complete, aside from item information screens

11/18 Update:
I didn't log most of the updates I made to this project over the last two weeks, which I belatedly realize I should have. I am documenting all currently implemented features for posterity.

Currently:
The intended game mode is the campaign mode. This does not work yet, so the game instantiates a random battle against ten opponents to demo implemented features.

The map places terrain features (fully implemented), enemy monsters (fully implemented), heroes (fully implemented), items (half-implemented), treasure (half-implemented), and potions (fully implemented). It displays the attributes of the units in the UI on the right (75% complete). You click to select a unit, then click another tile to resolve its turn; the turn will resolve depending on what is on the tile. Enemies will be attacked, potions will be consumed, and items will be collected.

The menu toggles to show the incomplete team selector UI. This will be worked on more when I move to the Campaign Arc.
