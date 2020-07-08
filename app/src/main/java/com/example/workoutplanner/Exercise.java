package com.example.workoutplanner;

import java.io.Serializable;

/**
 * Class representing an Exercise belonging to a Workout
 */
public class Exercise implements Serializable {
    private String name;
    private int noSets;

    //@TODO make private
    public Exercise(String name, int noSets) {
        this.name = name;
        this.noSets = noSets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoSets() {
        return noSets;
    }

    public void setNoSets(int noSets) {
        this.noSets = noSets;
    }

    /**
     * Tempo, e.g. 4020 where 4 is 4s of eccentric phase and so on
     */
    public static class TempoExercise extends Exercise{
        private int noReps;
        private int eccentric;
        private int eccentricPause;
        private int concentric;
        private int concentricPause;

        public TempoExercise(String name, int noSets, int noReps, int eccentric, int eccentricPause, int concentric, int concentricPause) {
            super(name, noSets);
            this.noReps = noReps;
            this.eccentric = eccentric;
            this.eccentricPause = eccentricPause;
            this.concentric = concentric;
            this.concentricPause = concentricPause;
        }

        public int getNoReps() {
            return noReps;
        }

        public void setNoReps(int noReps) {
            this.noReps = noReps;
        }

        public int getEccentric() {
            return eccentric;
        }

        public void setEccentric(int eccentric) {
            this.eccentric = eccentric;
        }

        public int getEccentricPause() {
            return eccentricPause;
        }

        public void setEccentricPause(int eccentricPause) {
            this.eccentricPause = eccentricPause;
        }

        public int getConcentric() {
            return concentric;
        }

        public void setConcentric(int concentric) {
            this.concentric = concentric;
        }

        public int getConcentricPause() {
            return concentricPause;
        }

        public void setConcentricPause(int concentricPause) {
            this.concentricPause = concentricPause;
        }
    }

    public static class IsometricExercise extends Exercise{
        private int time;

        public IsometricExercise(String name, int noSets, int time) {
            super(name, noSets);
            this.time = time;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}