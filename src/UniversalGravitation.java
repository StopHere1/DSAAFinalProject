import edu.princeton.cs.algs4.*;
//import edu.princeton.cs.algs4.Particle;

import java.math.*;
public class UniversalGravitation {
    private static final double G = 6.67 * Math.pow(10,-11);

//   public double getG() {
//        return G;
//    }

    public static void getGravitation(Particle a, Particle[] allParticles,double dt){
        double xAcceleration = 0;
        double yAcceleration = 0;

        for(Particle current:allParticles) {

            if (a.equals(current)) {
                continue;                 //if the particles are the same skip
            }

            double dx = current.getRx()-a.getRx() ;
            double dy = current.getRy()-a.getRy();

            double squareDistance = Math.pow(dx, 2) + Math.pow(dy, 2);  //calculate the distance
            double force = G * a.getMass() * current.getMass() / squareDistance;  //force calculation
            double atan = Math.atan(Math.abs(dy / dx));
            double cosFactor = Math.cos(atan);
            double sinFactor = Math.sin(atan);
            double fx;
            double fy;


            if (dx >= 0) {                      //adjusting sign for forces
                fx =  force * cosFactor;
            }
            else{
                fx = -(force * cosFactor);
            }

            if (dy >= 0) {
                fy = force * sinFactor;
            }
            else{
                fy = -(force * sinFactor);
            }


            xAcceleration = xAcceleration + fx / a.getMass(); // add up acceleration caused by different particles
            yAcceleration = yAcceleration + fy / a.getMass();
        }

        a.setVx(a.getVx()+xAcceleration*dt);  //save change to the particle velocity
        a.setVy(a.getVy()+yAcceleration*dt);
        a.setRx(a.getRx()+a.getVx()*dt);
        a.setRy(a.getRy()+a.getVy()*dt);
        a.draw();
    }
    public void gravitationMove(Particle a,Particle current){


    }
}
