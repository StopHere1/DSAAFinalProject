//package edu.princeton.cs.algs4;

//import edu.princeton.cs.algs4.*;
//import edu.princeton.cs.algs4.Particle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Date;

import java.awt.*;
import java.util.ArrayList;

import static edu.princeton.cs.algs4.StdOut.*;


public class CollisionSystem {
        private static final double HZ = 10000;    // number of redraw events per clock tick
        private static final double dt = 1.0/HZ;
        private MinPQ<Event> pq;          // the priority queue
        private double t  = 0.0;          // simulation clock time
        private Particle[] particles;     // the array of particles
        private static double axisSize = 1 ;
        private static boolean terminal = true;
        private static double[] time =null;
        private static int[] index =null;
        private double last;// the array of particles



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
        private void predict(double limit) {

            Rectangle bound = new Rectangle(0, 0, 2 * axisSize, 2 * axisSize);
            Quadtree quadtree = new Quadtree(0, bound);
            int i = 0;
            for (Particle particle : particles) {
                quadtree.particleinsert(particle, dt, i, axisSize);
                i++;
            }

//            if (a == null) return;  //if particle is empty return
            Event minimumTimeEvent = new Event(t + limit, null, null);
            // particle-particle collisions
            for (Particle particle1 : particles) {
                ArrayList<Integer> range=quadtree.retrieve(particle1,dt,axisSize);
                for (int w:range) { //calculate the time to hit each particle
                    double T = particle1.timeToHit(particles[w]);
                    if (T <= limit && t + T < minimumTimeEvent.time) {
//                        println("update collision");
                        minimumTimeEvent = new Event(t + T, particle1, particles[w]);
                    }// if the collision happens in the time-limit
                }
                double dtX = particle1.timeToHitVerticalWall();
                double dtY = particle1.timeToHitHorizontalWall();
                if (dtX <= limit && dtX + t <= minimumTimeEvent.time) {
                    minimumTimeEvent = new Event(t + dtX, particle1, null);
                }

                if (dtY <= limit && dtY + t <= minimumTimeEvent.time) {
                    minimumTimeEvent = new Event(t + dtY, null, particle1);

                }
            }

            // particle-wall collisions
            if (minimumTimeEvent.a != null || minimumTimeEvent.b != null) {
                pq.insert(minimumTimeEvent);
            }
        }



        // redraw all particles
        private void redraw(double limit) {
            StdDraw.clear();      // clear the canvas
            for (int i = 0; i < particles.length; i++) {
                particles[i].draw();   //draw the particles

            }
            StdDraw.show();
            StdDraw.pause(1);
            if (t < limit) {
                pq.insert(new Event(t + dt, null, null));  //redraw event
            }
        }


        /**
         * Simulates the system of particles for the specified amount of time.
         *
         * @param  limit the amount of time
         */


        public void simulate(double limit) {
            int n = 0;
            // initialize PQ with collision events and redraw event
            pq = new MinPQ<Event>();

            predict( dt);

            pq.insert(new Event(0, null, null));        // redraw event

            int count= 0;
            int eventCount = 1;

            // the main event-driven simulation loop
            while (!pq.isEmpty()) {
//                println("current event = "+eventCount++);
//                println("pq size  = "+pq.size());
                predict( dt);
//                println("pq size after predict = "+pq.size());

                // get impending event, discard if invalidated
                Event e = pq.delMin();
                if(e.a==null&&e.b==null&&pq.size()!=0){pq.delMin();}
                if (!e.isValid()) continue;

                Particle a = e.a;
                Particle b = e.b;


                Quad quad = new Quad(0, 0, axisSize * 2);

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
                }



//                if(count<time.length && t-time[count] < dt  &&terminal){
//                    printf("%e %e %19f %19f\n",particles[index[count]].getRx(),particles[index[count]].getRy(),particles[index[count]].getVx(),particles[index[count]].getVy());
//                    count++;
//                }

                t = e.time;


                // process event
                if      (a != null && b != null) a.bounceOff(b);              // particle-particle collision
                else if (a != null && b == null) a.bounceOffVerticalWall();   // particle-wall collision
                else if (a == null && b != null) b.bounceOffHorizontalWall(); // particle-wall collision
                else if (a == null && b == null) {
                    redraw(limit);               // redraw event
                }


                if (n < time.length && time[n] < t) {
                    double x = particles[index[n]].getRx() + axisSize;
                    double y = particles[index[n]].getRy() + axisSize;
                    double vx = particles[index[n]].getVx();
                    double vy = particles[index[n]].getVy();
                    n++;
                    printf("%f %f %f %f\n", x, y, vx, vy);
                }

            }
        }

    public void simulate2(double limit) {
        int n = 0;
        // initialize PQ with collision events and redraw event
        pq = new MinPQ<Event>();

        predict(dt);

        pq.insert(new Event(0, null, null));        // redraw event

        int count = 0;
        int eventCount = 1;
        last = 0;


        // the main event-driven simulation loop
        while (!pq.isEmpty()) {


            predict(dt);


            // get impending event, discard if invalidated
            Event e = pq.delMin();
            if (e.a == null && e.b == null && pq.size() != 0) {
                pq.delMin();
            }
            if (!e.isValid()) continue;

            Particle a = e.a;
            Particle b = e.b;


            Quad quad = new Quad(0, 0, axisSize * 2);

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
            }

            t = e.time;
            // process event
            if (a != null && b != null) {
                a.bounceOff(b);
            }             // particle-particle collision
            else if (a != null && b == null) {
                a.bounceOffVerticalWall();
            }   // particle-wall collision
            else if (a == null && b != null) {
                b.bounceOffHorizontalWall();
            } // particle-wall collision
            else if (a == null && b == null) {
                if (t < limit) {
                    pq.insert(new Event(t + dt, null, null));  //redraw event
                }
            }
            redraw(limit);
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


            // enable double buffering


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
                if (output.equals("terminal")) {
                    terminal = true;
                    //set the axisSize
                    int n = StdIn.readInt();
                    axisSize = (double) n / 2;


                    //read in particles
                    n = StdIn.readInt();
                    particles = new Particle[n];
                    for (int i = 0; i < n; i++) {
                        double rx = StdIn.readDouble() - axisSize;
                        double ry = StdIn.readDouble() - axisSize;
                        double vx = StdIn.readDouble();
                        double vy = StdIn.readDouble();
                        double radius = StdIn.readDouble();
                        double mass = StdIn.readDouble();
                        int r = StdIn.readInt();
                        int g = StdIn.readInt();
                        int b = StdIn.readInt();
                        Color color = new Color(r, g, b);
                        particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
                    }

                    // read the time and particle needed to be print out
                    n = StdIn.readInt();
                    time = new double[n];
                    index = new int[n];
                    for (int i = 0; i < n; i++) {
                        double ti = StdIn.readDouble();
                        int in = StdIn.readInt();
                        time[i] = ti;
                        index[i] = in;
                    }


                    // create collision system and simulate
                    CollisionSystem system = new CollisionSystem(particles);
                    Date start = new Date();
                    system.simulate(5);
                    Date end = new Date();
                    StdOut.println("the spent time is : " + (end.getTime() - start.getTime()) );

                } else if (output.equals("gui")) {
                    StdDraw.enableDoubleBuffering();
                    StdDraw.setCanvasSize(600, 600);
                    terminal = false;
                    int n = StdIn.readInt();
                    axisSize = (double) n / 2;
                    StdDraw.setXscale(-axisSize, axisSize);
                    StdDraw.setYscale(-axisSize, axisSize);

                    //read in particles
                    n = StdIn.readInt();
                    particles = new Particle[n];
                    for (int i = 0; i < n; i++) {
                        double rx = StdIn.readDouble() - axisSize;
                        double ry = StdIn.readDouble() - axisSize;
                        double vx = StdIn.readDouble();
                        double vy = StdIn.readDouble();
                        double radius = StdIn.readDouble();
                        double mass = StdIn.readDouble();
                        int r = StdIn.readInt();
                        int g = StdIn.readInt();
                        int b = StdIn.readInt();
                        Color color = new Color(r, g, b);
                        particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
                    }

                    // read the time and particle needed to be print out
                    n = StdIn.readInt();
                    time = new double[n];
                    index = new int[n];
                    for (int i = 0; i < n; i++) {
                        double ti = StdIn.readDouble();
                        int in = StdIn.readInt();
                        time[i] = ti;
                        index[i] = in;
                    }

                    // create collision system and simulate
                    CollisionSystem system = new CollisionSystem(particles);
                    system.simulate2(10000);
                }
            }
        }

    public static double getAxisSize() {
        return axisSize;
    }
}
