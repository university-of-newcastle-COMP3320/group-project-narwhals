--------------------------------Setup Instructions-------------------------------------------
This setup guide assumes that you have intellj IDEA installed (there is a free copy for
students available).

Pull current repository from Git Desktop (or chosen git platform).
Open "Project Narwhal" idea project in intellj IDEA.
Chosen java library should be java 11. If the java library does not correctly load, download
java 11 and try again.
The external libraries for LWJGL will not be loaded. The required libraries are from the LWJGL
found here: https://github.com/LWJGL/lwjgl3/releases
You will also require JOML for vectors and matrixs and the associated math functions for transforms.
JOML can be found here: https://github.com/JOML-CI/JOML/releases
PNGDecoder can be found here: https://drive.google.com/file/d/0B4_SgVGfVtFWMVlXSnkyeXIwdWs/view?resourcekey=0-m2YXuBFtDqiW9gI7NFhiQA
Download all required libraries and store in a safe place.

Libraries currently required are:
lwjgl
lwjgl-assimp
lwjgl-gflw
lwjgl-openal
lwjgl-opengl
joml 1.10.4
PNGDecoder

To load the required libraries, go to intellj IDEA File > Project Structure > Libraries and select the
+ sign and select java.
Select the file containing the required library and select apply. This should load the relevant library.
For JOML.1.10.4, while in the project structure menu, instead of clicking on the + sign above the library
names, click on lwjgl lib and click on the + above the class name. Then select JOML.1.10.4 and apply.

To run the project, it must be compiled, which can be done through the build menu or via
the hammer icon, or by right clicking the project and selecting the relevant compile option.
The project, once compiled, can be run using he green arrow, or via right clicking on project
narwhal and selecting run project narwhal.

-------------------------------Support---------------------------------------------------
The Project currently supports importing files in OBJ format and will parse
the information in an OBJ file into a 3D object that can be used in Project narwhal.
OBJ files should be stored in ProjectResources.


--------------------------------Recommendations-------------------------------------------
It is highly recommended that you play around with openGL as much as possible. It is recommended
that you follow through the tutorials found at:
ww.youtube.com/watch?v=VS8wlS9hF8E&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&ab_channel=ThinMatrix
This will provide you with a basic understanding of what the code is doing, and if you like you
can re-write it to ensure that you understand its features.



