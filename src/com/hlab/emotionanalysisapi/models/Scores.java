package com.hlab.emotionanalysisapi.models;


public class Scores {

    private double anger;
    private double contempt;
    private double disgust;
    private double fear;
    private double happiness;
    private double neutral;
    private double sadness;
    private double surprise;

    /**
     * No args constructor for use in serialization
     *
     */
    public Scores() {
    }

    /**
     *
     * @param disgust
     * @param sadness
     * @param contempt
     * @param anger
     * @param happiness
     * @param neutral
     * @param surprise
     * @param fear
     */
    public Scores(double anger, double contempt, double disgust, double fear, double happiness, double neutral, double sadness, double surprise) {
        this.anger = anger;
        this.contempt = contempt;
        this.disgust = disgust;
        this.fear = fear;
        this.happiness = happiness;
        this.neutral = neutral;
        this.sadness = sadness;
        this.surprise = surprise;
    }

    /**
     *
     * @return
     *     The anger
     */
    public double getAnger() {
        return anger;
    }

    /**
     *
     * @param anger
     *     The anger
     */
    public void setAnger(double anger) {
        this.anger = anger;
    }

    /**
     *
     * @return
     *     The contempt
     */
    public double getContempt() {
        return contempt;
    }

    /**
     *
     * @param contempt
     *     The contempt
     */
    public void setContempt(double contempt) {
        this.contempt = contempt;
    }

    /**
     *
     * @return
     *     The disgust
     */
    public double getDisgust() {
        return disgust;
    }

    /**
     *
     * @param disgust
     *     The disgust
     */
    public void setDisgust(double disgust) {
        this.disgust = disgust;
    }

    /**
     *
     * @return
     *     The fear
     */
    public double getFear() {
        return fear;
    }

    /**
     *
     * @param fear
     *     The fear
     */
    public void setFear(double fear) {
        this.fear = fear;
    }

    /**
     *
     * @return
     *     The happiness
     */
    public double getHappiness() {
        return happiness;
    }

    /**
     *
     * @param happiness
     *     The happiness
     */
    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    /**
     *
     * @return
     *     The neutral
     */
    public double getNeutral() {
        return neutral;
    }

    /**
     *
     * @param neutral
     *     The neutral
     */
    public void setNeutral(double neutral) {
        this.neutral = neutral;
    }

    /**
     *
     * @return
     *     The sadness
     */
    public double getSadness() {
        return sadness;
    }

    /**
     *
     * @param sadness
     *     The sadness
     */
    public void setSadness(double sadness) {
        this.sadness = sadness;
    }

    /**
     *
     * @return
     *     The surprise
     */
    public double getSurprise() {
        return surprise;
    }

    /**
     *
     * @param surprise
     *     The surprise
     */
    public void setSurprise(double surprise) {
        this.surprise = surprise;
    }
}
