package com.hlab.emotionanalysisapi.models;


public class FaceAnalysis {

    private FaceRectangle faceRectangle;
    private Scores scores;

    /**
     * No args constructor for use in serialization
     *
     */
    public FaceAnalysis() {
    }

    /**
     *
     * @param scores
     * @param faceRectangle
     */
    public FaceAnalysis(FaceRectangle faceRectangle, Scores scores) {
        this.faceRectangle = faceRectangle;
        this.scores = scores;
    }

    /**
     *
     * @return
     *     The faceRectangle
     */
    public FaceRectangle getFaceRectangle() {
        return faceRectangle;
    }

    /**
     *
     * @param faceRectangle
     *     The faceRectangle
     */
    public void setFaceRectangle(FaceRectangle faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    /**
     *
     * @return
     *     The scores
     */
    public Scores getScores() {
        return scores;
    }

    /**
     *
     * @param scores
     *     The scores
     */
    public void setScores(Scores scores) {
        this.scores = scores;
    }
}