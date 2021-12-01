//package edu.princeton.cs.algs4;

//import edu.princeton.cs.algs4.*;
//import edu.princeton.cs.algs4.Particle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

import java.awt.Color;
import java.util.ArrayList;

import static edu.princeton.cs.algs4.StdOut.*;


public class CollisionSystem {
        private static final double HZ = 1;    // number of redraw events per clock tick
        private static final double dt = 1.0/HZ;
        private MinPQ<Event> pq;          // the priority queue
        private double t  = 0.0;          // simulation clock time
        private Particle[] particles;     // the array of particles
        private static double axisSize = 1 ;
        private static boolean terminal = true;
        private static int[] time =null;
        private static int[] index =null;



        /**
         * Initializes a system with the specified collection of particles.
         * The individual particles will be mutated during the simulation.
         *
         * @param  particles the array of particles
         */


        public CollisionSystem(Particle[] particles) {
            this.particles = particles.clone();   // defensive copy
        }

        // updates priority queue with all new events for particle a
        private void predict(Particle a, double limit) {
            if (a == null) return;  //if particle is empty return

            // particle-particle collisions
            for (int i = 0; i < particles.length; i++) { //calculate the time to hit each particle
                double deltat = a.timeToHit(particles[i]);
                if ( deltat <= limit) // if the collision happens in the time-limit
                    pq.insert(new Event(t + deltat, a, particles[i]));   // add the collision to the event priority queue
            }

            // particle-wall collisions
            double dtX = a.timeToHitVerticalWall();
            double dtY = a.timeToHitHorizontalWall();
            if (dtX <= limit) pq.insert(new Event(t + dtX, a, null));
            if (dtY <= limit) pq.insert(new Event(t + dtY, null, a));
        }

        // redraw all particles
        private void redraw(double limit) {
            StdDraw.clear();      // clear the canvas
            for (int i = 0; i < particles.length; i++) {
//                UniversalGravitation.getGravitation(particles[i],particles,1.0 / HZ);
//                predict(particles[i],limit);
//                particles[i].move(1.0 / HZ);
                particles[i].draw();   //draw the particles

            }
            StdDraw.show();
            StdDraw.pause(10);
            if (t < limit) {

//                pq.insert(new Event(t + dt, null, null));  //redraw event
            }
        }


        /**
         * Simulates the system of particles for the specified amount of time.
         *
         * @param  limit the amount of time
         */


        public void simulate(double limit) {

            // initialize PQ with collision events and redraw event
            pq = new MinPQ<Event>();
            for (int i = 0; i < particles.length; i++) {
                predict(particles[i], dt);
            }
            pq.insert(new Event(0, null, null));        // redraw event

            int count= 0;

//            Quad quad = new Quad(0, 0, axisSize * 2);
//            BHTree tree = new BHTree(quad);

//            for (int i = 0; i < particles.length; i++)
//                if (particles[i].in(quad))
//                    tree.insert(particles[i]);

            // the main event-driven simulation loop
            while (!pq.isEmpty()) {

                // get impending event, discard if invalidated
                Event e = pq.delMin();
                if (!e.isValid()) continue;
                Particle a = e.a;
                Particle b = e.b;

//                for(Particle current :particles){
//                    UniversalGravitation.getGravitation(current,particles,limit);
//                }



                // physical collision, so update positions, and then simulation clock
//                for (int i = 0; i < particles.length; i++) {
//                    double individualTime = t;
//                    particles[i].resetForce();
//                    tree.updateForce(particles[i]);
////                  UniversalGravitation.getGravitation(particles[i], particles, e.time);
//                    while(individualTime+dt<=e.time) {
////                        UniversalGravitation.getGravitation(particles[i],particles,dt);
////                        predict(particles[i],limit);
//
//                        particles[i].update(dt);
//                        if(count<=time.length-1 && individualTime==time[count]&&terminal){
//                            printf("%e %e %e %e\n",particles[index[count]].getRx(),particles[index[count]].getRy(),particles[index[count]].getVx(),particles[index[count]].getVy());
//                            count++;
//                        }
////                        particles[i].move(dt);
//                        individualTime = individualTime + dt;
//
//                    }
////                    particles[i].resetForce();
////                    tree.updateForce(particles[i]);
//                    particles[i].update(e.time - individualTime);
////                    UniversalGravitation.getGravitation(particles[i],particles,e.time - individualTime);
////                    particles[i].move(e.time - individualTime);
//                }    //enable particles to move to the next time when collision take place.
//                t = e.time; // set t as the time when the last event take place

                
//                println(t);
                Quad quad = new Quad(0, 0, axisSize * 2);
                //if e.time bigger than n*dt
                while(t + dt<e.time){

                    BHTree tree = new BHTree(quad);
                    println(e.a);
                    println(dt);
                    // build the Barnes-Hut tree
                    for (Particle particle1 : particles)
                        if (particle1.in(quad))
                            tree.insert(particle1);

                    // update the forces, positions, velocities, and accelerations
                    for (Particle element : particles) {
                        element.resetForce();
                        tree.updateForce(element);
                        element.update(dt);
//                        predict(particles[i],dt);
                    }
                    if(count<=time.length-1 && t==time[count]&&terminal){
                            printf("%e %e %e %e\n",particles[index[count]].getRx(),particles[index[count]].getRy(),particles[index[count]].getVx(),particles[index[count]].getVy());
                            count++;
                        }

                    StdDraw.clear();
                    for (Particle item : particles) item.draw();
//                    StdDraw.show(10);

                    t = t + dt;

//                    Quad quad = new Quad(0, 0, axisSize * 2);
                    tree = new BHTree(quad);

//                    println("123456");
                    // build the Barnes-Hut tree
                    for (Particle value : particles)
                        if (value.in(quad))
                            tree.insert(value);


                    for (Particle value : particles) {
                        value.resetForce();
                        tree.updateForce(value);
                        value.updateVelocity(dt);
                    }

                    for (Particle particle : particles) {
                        predict(particle, dt);
                    }

                    for (Particle particle : particles) {
                        particle.resetvelocity();

                    }


                }



                //for the remain time e.time-t (when smaller than dt)

                BHTree tree = new BHTree(quad);

                // build the Barnes-Hut tree
                for (Particle particle : particles)
                    if (particle.in(quad))
                        tree.insert(particle);

                // update the forces, positions, velocities, and accelerations
                for (Particle particle : particles) {
                    particle.resetForce();
                    tree.updateForce(particle);
                    particle.update(e.time - t);
//                    predict(particles[i],dt);
                }

                tree = new BHTree(quad);

//                    println("123456");
                // build the Barnes-Hut tree
                for (Particle particle : particles)
                    if (particle.in(quad))
                        tree.insert(particle);

                // update the forces, positions, velocities, and accelerations
                for (Particle particle : particles) {
                    particle.resetForce();
                    tree.updateForce(particle);
                    particle.updateVelocity(dt);
//                        predict(particles[i],dt);
                }

                for (Particle particle : particles) {
                    predict(particle, dt);

                }

                for (Particle particle : particles) {
                    particle.resetvelocity();

                }

//                for (int i = 0; i < particles.length; i++) {
////                    particles[i].resetForce();
////                    tree.updateForce(particles[i]);
//                    particles[i].update(e.time - t);
//                    predict(particles[i],dt);
//                }
                if(count<time.length && t==time[count]&&terminal){
                    printf("%e %e %e %e\n",particles[index[count]].getRx(),particles[index[count]].getRy(),particles[index[count]].getVx(),particles[index[count]].getVy());
                    count++;
                }
                StdDraw.clear();

                for (Particle particle : particles) particle.draw();
//                StdDraw.show(10);

                t = e.time;

                // process event
                if      (a != null && b != null) a.bounceOff(b);              // particle-particle collision
                else if (a != null && b == null) a.bounceOffVerticalWall();   // particle-wall collision
                else if (a == null && b != null) b.bounceOffHorizontalWall(); // particle-wall collision
                else if (a == null && b == null) {
                    redraw(limit);               // redraw event
                }

                pq.insert(new Event(t+dt,null,null));
//                double totalKineticEnergy =0;
//                for(Particle value:particles){
//                    totalKineticEnergy+=value.kineticEnergy();
//                }
//                println(totalKineticEnergy);
                // update the priority queue with new collisions involving a or b
                tree = new BHTree(quad);

//                    println("123456");
                // build the Barnes-Hut tree
                for (Particle particle : particles)
                    if (particle.in(quad))
                        tree.insert(particle);

                // update the forces, positions, velocities, and accelerations
                for (Particle particle : particles) {
                    particle.resetForce();
                    tree.updateForce(particle);
                    particle.updateVelocity(dt);
//                        predict(particles[i],dt);
                }

                predict(a,dt);
                predict(b,dt);

                for (Particle particle : particles) {
                    particle.resetvelocity();

                }
            }
        }


        /***************************************************************************
         *  An event during a particle collision simulation. Each event contains
         *  the time at which it will occur (assuming no supervening actions)
         *  and the particles a and b involved.
         *
         *    -  a and b both null:      redraw event
         *    -  a null, b not null:     collision with vertical wall
         *    -  a not null, b null:     collision with horizontal wall
         *    -  a and b both not null:  binary collision between a and b
         *
         ***************************************************************************/


        private static class Event implements Comparable<Event> {
            private final double time;         // time that event is scheduled to occur
            private final Particle a, b;       // particles involved in event, possibly null
            private final int countA, countB;  // collision counts at event creation


            // create a new event to occur at time t involving a and b
            public Event(double t, Particle a, Particle b) {
                this.time = t;
                this.a    = a;
                this.b    = b;
                if (a != null) countA = a.count();
                else           countA = -1;
                if (b != null) countB = b.count();
                else           countB = -1;
            }

            // compare times when two events will occur
            public int compareTo(Event that) {
                return Double.compare(this.time, that.time);
            }

            // has any collision occurred between when event was created and now?
            public boolean isValid() {
                if (a != null && a.count() != countA) return false;
                if (b != null && b.count() != countB) return false;
                return true;
            }

        }


        /**
         * Unit tests the {@code CollisionSystem} data type.
         * Reads in the particle collision system from a standard input
         * (or generates {@code N} random particles if a command-line integer
         * is specified); simulates the system.
         *
         * @param args the command-line arguments
         */


        public static void main(String[] args) {

            StdDraw.setCanvasSize(600, 600);

            //test axis
//            StdDraw.setPenColor(StdDraw.RED);
//            StdDraw.setPenRadius(0.1);
//            StdDraw.point(0.5,0.5);



            // enable double buffering
            StdDraw.enableDoubleBuffering();

            // the array of particles
            Particle[] particles;

            // create n random particles
            if (args.length == 1) {
                int n = Integer.parseInt(args[0]);
                particles = new Particle[n];
                for (int i = 0; i < n; i++)
                    particles[i] = new Particle();
            }

            // or read from standard input
            else {
                // read if needed to print the data
                String output = StdIn.readString();
                if(output.equals("terminal")){
                    terminal=true;
                }else{
                    terminal=false;
                }
                //set the axisSize
                int n = StdIn.readInt();
                axisSize = (double) n / 2;
                StdDraw.setXscale(-axisSize,axisSize);
                StdDraw.setYscale(-axisSize,axisSize);

                //read in particles
                n = StdIn.readInt();
                particles = new Particle[n];
                for (int i = 0; i < n; i++) {
                    double rx     = StdIn.readDouble() - axisSize;
                    double ry     = StdIn.readDouble() - axisSize;
                    double vx     = StdIn.readDouble() ;
                    double vy     = StdIn.readDouble() ;
                    double radius = StdIn.readDouble();
                    double mass   = StdIn.readDouble();
                    int r         = StdIn.readInt();
                    int g         = StdIn.readInt();
                    int b         = StdIn.readInt();
                    Color color   = new Color(r, g, b);
                    particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
                }
                // read the time and particle needed to be print out
                n = StdIn.readInt();
                time = new int[n];
                index= new int[n];
                for(int i = 0; i < n ;i++){
                    int ti = StdIn.readInt();
                    int in= StdIn.readInt();
                    time[i]= ti;
                    index[i]=in;
                }

            }

            // create collision system and simulate
            CollisionSystem system = new CollisionSystem(particles);
            system.simulate(10000);
        }

    public static double getAxisSize() {
        return axisSize;
    }
}
