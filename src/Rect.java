public class Rect {
    private double x;
    private double y;
    private double width;
    private double height;

    public Rect(Particle particle,double dt){

        double absDeltaX= dt*Math.abs(particle.getVx());
        double absDeltaY= dt*Math.abs(particle.getVy());
        this.width=absDeltaX+2*particle.getRadius();
        this.height=absDeltaY+2*particle.getRadius();

        if(particle.getVx()>0&&particle.getVy()>0){
            this.x=particle.getRx()-particle.getRadius();
            this.y=particle.getRy()+absDeltaY+particle.getRadius();
        }
        else if(particle.getVx()>0&&particle.getVy()<0){
            this.x=particle.getRx()-particle.getRadius();
            this.y=particle.getRy()+particle.getRadius();
        }

        else if(particle.getVx()<0&&particle.getVy()>0){
            this.x=particle.getRx()-absDeltaX-particle.getRadius();
            this.y=particle.getRy()+absDeltaY+particle.getRadius();
        }
        else if(particle.getVx()<0&&particle.getVy()<0){
            this.x=particle.getRx()-absDeltaX-particle.getRadius();
            this.y=particle.getRy()+particle.getRadius();
        }
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
