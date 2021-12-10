import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Quadtree {

    private LinkedList<Particle> particle;     // body or aggregate body stored in this node
    private Rect rect;
    private Square square;     // square region that the tree represents
    private Quadtree father;
    private Quadtree NW;     // tree representing northwest quadrant
    private Quadtree NE;     // tree representing northeast quadrant
    private Quadtree SW;     // tree representing southwest quadrant
    private Quadtree SE;



    public Quadtree(Square square,Quadtree fatherNode) {
         this.square=square;
         this.particle=null;
         this.father=fatherNode;
         this.rect=null;
         this.NE=null;
         this.NW=null;
         this.SE=null;
         this.SW=null;

    }
    public void insert(Particle b,double dt) {

        // if this node does not contain a body, put the new body b here
        while(square.contains(b.getRect())) {
            // internal node
            if (!isExternal()) {

                // recursively insert Body b into the appropriate quadrant
                putBody(b, dt);
            }

            // external node
            else {
                // subdivide the region further by creating four children
                NW = new Quadtree(square.NW(), this);
                NE = new Quadtree(square.NE(), this);
                SE = new Quadtree(square.SE(), this);
                SW = new Quadtree(square.SW(), this);

                // recursively insert both this body and Body b into the appropriate quadrant
                for(Particle i:particle) {
                    putBody(i, dt);
                }
                putBody(b, dt);


            }
        }
    }
//@Override
//private Rectangle rectangle(double x,double y,double deltax,double deltay){
//        return
//}
    private void putBody(Particle b,double dt) {
        if (b.inSquare(square.NW()))
            NW.insert(b,dt);
        else if (b.inSquare(square.NE()))
            NE.insert(b,dt);
        else if (b.inSquare(square.SE()))
            SE.insert(b,dt);
        else if (b.inSquare(square.SW()))
            SW.insert(b,dt);
    }

    //method clear is wrong
    public void clear(){
        Quadtree root = null;
        while(this.father!=null){

            root =this.father;
        }
        root.NW=null;
        root.NE=null;
        root.SE=null;
        root.SW=null;
    }

    private boolean isExternal() {
        // a node is external if all four children are null
        return (NW == null && NE == null && SW == null && SE == null);
    }

//    public Particle[] retrieve(Quadtree){
//        Particles[] allParticles = new Particle[];
//        return
//    }


//    public String toString() {
//        if (isExternal())
//            return " " + particle + "\n";
//        else
//            return "*" + particle + "\n" + NW + NE + SW + SE;
//    }
}