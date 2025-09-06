package assignment3;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth;
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random();

 /*
  * These two constructors are here for testing purposes.
  */
 public Block() {
  this.children = new Block[0];
 }

 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }

 /*
  * Creates a random block given its level and a max depth.
  *
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {
  if (lvl > maxDepth){
   throw new IllegalArgumentException("The current level cannot be greater than the max depth");
  }

  this.maxDepth = maxDepth;
  this.children = new Block[0];
  this.level = lvl;
  if (lvl < maxDepth) {
   double randomNum = this.gen.nextDouble();
   if (randomNum < Math.exp((-0.25) * lvl)) {
    Block UR = new Block(lvl + 1, maxDepth);
    Block UL = new Block(lvl + 1, maxDepth);
    Block LL = new Block(lvl + 1, maxDepth);
    Block LR = new Block(lvl + 1, maxDepth);
    Block[] kids = {UR, UL, LL, LR};
    this.children = kids;
    return;
   }
  }
  int randomInt = this.gen.nextInt(GameColors.BLOCK_COLORS.length);
  this.color = GameColors.BLOCK_COLORS[randomInt];
 }


 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the
  * blocks.
  *
  *  The size is the height and width of the block. (xCoord, yCoord) are the
  *  coordinates of the top left corner of the block.
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
  //Input validation for size
  if (size <= 0){
   throw new IllegalArgumentException("The size must be greater than 0");
  }
  if (this.level == 0 && (size < 0 || (size / Math.pow(2,this.maxDepth)) % 1 != 0)) {
   throw new IllegalArgumentException("The size is not a valid input, it must be divisible by 2 for all levels up to the maximimum depth.");
  }
  else if (this.children.length == 4) {
   this.children[0].updateSizeAndPosition(size / 2, xCoord + size / 2, yCoord);
   this.children[1].updateSizeAndPosition(size / 2, xCoord, yCoord);
   this.children[2].updateSizeAndPosition(size / 2, xCoord, yCoord + size / 2);
   this.children[3].updateSizeAndPosition(size / 2, xCoord + size / 2, yCoord + size / 2);
  } else if (this.children.length != 0) {
   throw new IllegalArgumentException("This block is not valid");
  }
  this.size = size;
  this.xCoord = xCoord;
  this.yCoord = yCoord;
 }


 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  *
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  *
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *
  * The order in which the blocks to draw appear in the list does NOT matter.
  */

 public ArrayList<BlockToDraw> getBlocksToDraw() {
  ArrayList<BlockToDraw> toDraw = new ArrayList<BlockToDraw>();
  if (this.children.length == 0) {
   toDraw.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0));
   toDraw.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3));
   return toDraw;
  } else {
   for (Block b : this.children){
    toDraw.addAll(b.getBlocksToDraw());
   }
  }
  return toDraw;
 }

 /*
  * This method is provided and you should NOT modify it.
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }

 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than
  * the lowest block at the specified location, then return the block
  * at the location with the closest level value.
  *
  * The location is specified by its (x, y) coordinates. The lvl indicates
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will
  * contain the location (x, y) too. This is why we need lvl to identify
  * which Block should be returned.
  *
  * Input validation:
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
  // input validation
  if (lvl>this.maxDepth || lvl < this.level) {
   throw new IllegalArgumentException("That level input is invalid");
  }
  // base case
  if (lvl == this.level && (this.xCoord <= x && x < (this.xCoord + size)) && (this.yCoord <= y && y < (this.yCoord + size))){
   return this;
  }

  else if (this.xCoord <= x && x < (this.xCoord + size) && (this.yCoord <= y && y < (this.yCoord + size))){
   if (this.children.length == 0){
    return this;
   } else {
    if (x > this.xCoord + size/2 && y < this.yCoord + size/2){
     return this.children[0].getSelectedBlock(x,y,lvl);
    } else if (x < this.xCoord + size/2 && y < this.yCoord + size/2){
     return this.children[1].getSelectedBlock(x,y,lvl);
    } else if (x < this.xCoord + size/2 && y > this.yCoord + size/2){
     return this.children[2].getSelectedBlock(x,y,lvl);
    } else if (x > this.xCoord + size/2 && y > this.yCoord + size/2){
     return this.children[3].getSelectedBlock(x,y,lvl);
    }
   }
  }
  return null;
 }

 /*
  * Swaps the child Blocks of this Block.
  * If input is 1, swap vertically. If 0, swap horizontally.
  * If this Block has no children, do nothing. The swap
  * should be propagated, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  *
  */
 public void reflect(int direction) {
  // input validation
  if (direction != 0 && direction != 1) {
   throw new IllegalArgumentException("The direction can either be 0 or 1");
  }
  // base case
  if (this.children.length == 0){
   return;
  }
  if (direction == 0){
   Block [] flip = {this.children[3],this.children[2],this.children[1],this.children[0]};
   this.children = flip;
  } else if (direction == 1) {
   Block[] flip = {this.children[1], this.children[0], this.children[3], this.children[2]};
   this.children = flip;
  }
  this.updateSizeAndPosition(this.size,this.xCoord,this.yCoord);
  // recursive step
  for (Block b : this.children){
   b.reflect(direction);
  }
 }

 /*
  * Rotate this Block and all its descendants.
  * If the input is 1, rotate clockwise. If 0, rotate
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  // input validation
  if (direction != 0 && direction != 1){
   throw new IllegalArgumentException("That is not a valid direction");
  }
  if (this.children.length == 0){
   return;
  }

  if (direction == 0){
   Block [] rotate = {this.children[3],this.children[0],this.children[1],this.children[2]};
   this.children = rotate;
  } else if (direction == 1){
   Block[] rotate = {this.children[1],this.children[2],this.children[3],this.children[0]};
   this.children = rotate;
  }
  this.updateSizeAndPosition(this.size,this.xCoord,this.yCoord);
  // recursive step
  for (Block b : this.children){
   b.rotate(direction);
  }
 }

 /*
  * Smash this Block.
  *
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  *
  * A Block can be smashed iff it is not the top-level Block
  * and it is not already at the level of the maximum depth.
  *
  * Return True if this Block was smashed and False otherwise.
  *
  */
 public boolean smash() {
  if (this.level == 0){
   return false;
  } else if (this.level >= maxDepth){
   return false;
  } else {
   Block[] newChildren = {new Block(this.level-1,this.level-1), new Block(this.level-1,this.level-1), new Block(this.level-1,this.level-1), new Block(this.level-1,this.level-1)};
   this.children = newChildren;
   this.updateSizeAndPosition(this.size,this.xCoord,this.yCoord);
   return true;
  }
 }

 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  *
  * Return and array arr where, arr[i] represents the unit cells in row i,
  * arr[i][j] is the color of unit cell in row i and column j.
  *
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */

 public Color[][] flatten() {
  int numUnits = (int) Math.pow(2, this.maxDepth - this.level);
  Color[][] flat = new Color[numUnits][numUnits];

  if (this.children.length == 0) {
   for (int i = 0; i < numUnits; i++) {
    for (int j = 0; j < numUnits; j++) {
     flat[i][j] = this.color;
    }
   }
  } else {
   for (int i = 0; i<numUnits/2; i++){
    for (int j = 0; j<numUnits/2; j++){
     flat[i][j] = this.children[1].flatten()[i][j];
    }
    for (int j = 0; j<numUnits/2; j++){
     flat[i][j+numUnits/2] = this.children[0].flatten()[i][j];
    }
   }

   for (int i = 0; i<numUnits/2; i++){
    for (int j = 0; j<numUnits/2; j++){
     flat[i+numUnits/2][j] = this.children[2].flatten()[i][j];
    }
    for (int j = 0; j<numUnits/2; j++){
     flat[i+numUnits/2][j+numUnits/2] = this.children[3].flatten()[i][j];    }
   }
  }
  return flat;
 }

 // These two get methods have been provided. Do NOT modify them.
 public int getMaxDepth() {
  return this.maxDepth;
 }

 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block.
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }

 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
}
