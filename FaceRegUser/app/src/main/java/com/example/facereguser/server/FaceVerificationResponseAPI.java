package com.example.facereguser.server;

import com.google.gson.annotations.SerializedName;

public class FaceVerificationResponseAPI {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    // Default constructor
    public FaceVerificationResponseAPI() {
    }

    // Parameterized constructor
    public FaceVerificationResponseAPI(String message, Data data) {
        this.message = message;
        this.data = data;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("verified")
        private boolean verified;

        @SerializedName("distance")
        private double distance;

        @SerializedName("threshold")
        private double threshold;

        @SerializedName("model")
        private String model;

        @SerializedName("detector_backend")
        private String detectorBackend;

        @SerializedName("similarity_metric")
        private String similarityMetric;

        @SerializedName("facial_areas")
        private FacialAreas facialAreas;

        @SerializedName("time")
        private double time;

        // Default constructor
        public Data() {
        }

        // Parameterized constructor
        public Data(boolean verified, double distance, double threshold, String model, String detectorBackend, String similarityMetric, FacialAreas facialAreas, double time) {
            this.verified = verified;
            this.distance = distance;
            this.threshold = threshold;
            this.model = model;
            this.detectorBackend = detectorBackend;
            this.similarityMetric = similarityMetric;
            this.facialAreas = facialAreas;
            this.time = time;
        }

        // Getters and setters
        public boolean isVerified() {
            return verified;
        }

        public void setVerified(boolean verified) {
            this.verified = verified;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getThreshold() {
            return threshold;
        }

        public void setThreshold(double threshold) {
            this.threshold = threshold;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getDetectorBackend() {
            return detectorBackend;
        }

        public void setDetectorBackend(String detectorBackend) {
            this.detectorBackend = detectorBackend;
        }

        public String getSimilarityMetric() {
            return similarityMetric;
        }

        public void setSimilarityMetric(String similarityMetric) {
            this.similarityMetric = similarityMetric;
        }

        public FacialAreas getFacialAreas() {
            return facialAreas;
        }

        public void setFacialAreas(FacialAreas facialAreas) {
            this.facialAreas = facialAreas;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public static class FacialAreas {
            @SerializedName("img1")
            private FacialArea img1;

            @SerializedName("img2")
            private FacialArea img2;

            // Default constructor
            public FacialAreas() {
            }

            // Parameterized constructor
            public FacialAreas(FacialArea img1, FacialArea img2) {
                this.img1 = img1;
                this.img2 = img2;
            }

            // Getters and setters
            public FacialArea getImg1() {
                return img1;
            }

            public void setImg1(FacialArea img1) {
                this.img1 = img1;
            }

            public FacialArea getImg2() {
                return img2;
            }

            public void setImg2(FacialArea img2) {
                this.img2 = img2;
            }

            public static class FacialArea {
                @SerializedName("x")
                private int x;

                @SerializedName("y")
                private int y;

                @SerializedName("w")
                private int w;

                @SerializedName("h")
                private int h;

                @SerializedName("left_eye")
                private int[] leftEye;

                @SerializedName("right_eye")
                private int[] rightEye;

                // Default constructor
                public FacialArea() {
                }

                // Parameterized constructor
                public FacialArea(int x, int y, int w, int h, int[] leftEye, int[] rightEye) {
                    this.x = x;
                    this.y = y;
                    this.w = w;
                    this.h = h;
                    this.leftEye = leftEye;
                    this.rightEye = rightEye;
                }

                // Getters and setters
                public int getX() {
                    return x;
                }

                public void setX(int x) {
                    this.x = x;
                }

                public int getY() {
                    return y;
                }

                public void setY(int y) {
                    this.y = y;
                }

                public int getW() {
                    return w;
                }

                public void setW(int w) {
                    this.w = w;
                }

                public int getH() {
                    return h;
                }

                public void setH(int h) {
                    this.h = h;
                }

                public int[] getLeftEye() {
                    return leftEye;
                }

                public void setLeftEye(int[] leftEye) {
                    this.leftEye = leftEye;
                }

                public int[] getRightEye() {
                    return rightEye;
                }

                public void setRightEye(int[] rightEye) {
                    this.rightEye = rightEye;
                }
            }
        }
    }
}