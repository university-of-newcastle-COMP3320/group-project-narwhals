--------------------------------Setup Instructions-------------------------------------------
This setup guide assumes that you have intellj IDEA installed (there is a free copy for students available).

Pull current repository from Git Desktop (or chosen git platform).
Open "Project Narwhal" idea project in intellj IDEA.
Chosen java library should be java 11. If the java library does not correctly load, download java 11 and try again.
The external libraries for LWJGL will not be loaded. The required libraries are from the LWJGL found here: https://github.com/LWJGL/lwjgl3/releases
Download lwjgl.zip and store libraries in a safe place.

Libraries currently required are:
lwjgl
lwjgl-assimp
lwjgl-gflw
lwjgl-openal
lwjgl-opengl

To load the required libraries, go to intellj IDEA File > Project Structure > Libraries and select the + sign and select java.
Select the file containing the required library and select okay. This should load the relevant library.

To run the project, it must be compiled, which can be done through the build menu or via the hammer icon, or by right clicking the project
and selecting the relevant compile option.
The project, once compiled, can be run using he green arrow, or via right clicking on project narwhal and selecting run project narwhal.

-----------------------------------------------------------------------------------------------

If you have any problems, let me know.
-Luke
