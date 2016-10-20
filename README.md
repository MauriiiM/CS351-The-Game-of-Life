# The-Game-of-Life_Lab3
# Mauricio Monsivais

>>Description
  a 3D implementation of the Game of Life in which r = (r1, r2, r3, r4)
  
>>Sources
  Used to accept only number inputs in text field
  - http://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx

>>Classes
  TheGameOfLife <<contains main
  Cell
  InputHandler
  
>>Inputs
  Given the input, r=(r1,r2,r3,r4) 
   1) A new cube will appear if the number of neighbors is equal or more than r1 and equal or less than r2. 
   2) A cube will die if the number of neighbors is more than r3 or less than r4.
   
>>Controls
  Mouse: scroll up/down to zoom in/out
  Mouse: grab cell and move sideways to rotate Life structure
  
>>Presets
  n Random cells (type in a number and press enter)
  Random (will create a completely random 3D structure)
  1) a stairway
  2) 3D structure outline
  3) a small box without corner (to observe patterns)
  
>>Known Bugs
  - game can't be paused (camera rotation can be)