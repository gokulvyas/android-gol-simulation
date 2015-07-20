# android-gol-simulation

This is an android app for simulating Conway's Game of Life. (http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) .

I have used android.widget.TableLayout which is a child of ViewGroup for creating 2-D Grid.
For simplicity there are fixed 20 rows and 20 columns in this TableLayout. Each Row contains 20 TextViews.

-Used "*" to represent a Live cell.
-Used " " for a dead cell.
-Cell can be made alive/dead by user. No initial pattern added.
-1 Toggle button for Play/Pause
-1 Clear button to reset the view
-Added support for Portrait and Landscape mode
-Calculation of live cells is determined in another thread. I have used AsyncTask for this purpose.
-Added one simple Instrumentation test and 1 unit test. Followed Android convention of placing Instrumentation tests in 'androidTest' folder and unit tests in "test" folder
-Used Gradle

I have developed this app using Android Studio.
 

 