public class Square {
    //center of the Square
    private final double xmid;
    private final double ymid;

    //length of the Square
    private final double length;

    public Square(double xmid, double ymid, double length) { //initialize
        this.xmid = xmid;
        this.ymid = ymid;
        this.length = length;
    }
    public Square NW() {
        double x = this.xmid - this.length / 4.0;
        double y = this.ymid + this.length / 4.0;
        double len = this.length / 2.0;
        Square NW = new Square(x, y, len);
        return NW;
    }
    public Square NE() {
        double x = this.xmid + this.length / 4.0;
        double y = this.ymid + this.length / 4.0;
        double len = this.length / 2.0;
        Square NE = new Square(x, y, len);
        return NE;
    }
    public Square SW() {
        double x = this.xmid - this.length / 4.0;
        double y = this.ymid - this.length / 4.0;
        double len = this.length / 2.0;
        Square SW = new Square(x, y, len);
        return SW;
    }
    public Square SE() {
        double x = this.xmid + this.length / 4.0;
        double y = this.ymid - this.length / 4.0;
        double len = this.length / 2.0;
        Square SE = new Square(x, y, len);
        return SE;
    }

    public boolean contains(Rect rect) {  //check if the rect of the particle is in the square totally
        double halfLen = this.length / 2.0;
        return (rect.getX() + rect.getWidth() <= this.xmid + halfLen &&
                rect.getX() >= this.xmid - halfLen &&
                rect.getY() <= this.ymid + halfLen &&
                rect.getY() - rect.getHeight() >= this.ymid - halfLen);
    }

    public double getLength() {
        return length;
    }

    public double getXmid() {
        return xmid;
    }

    public double getYmid() {
        return ymid;
    }

}
