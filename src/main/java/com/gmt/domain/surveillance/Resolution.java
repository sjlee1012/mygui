package com.gmt.domain.surveillance;

public enum Resolution {
    HD("1280x720"),
    FULL_HD("1920x1080"),
    UHD_4K("3840x2160"),
    UHD_8K("7680x4320");

    private final String resolution;

    Resolution(String resolution) {
        this.resolution = resolution;
    }

    public String getResolution() {
        return resolution;
    }

    @Override
    public String toString() {
        return resolution;
    }
}

