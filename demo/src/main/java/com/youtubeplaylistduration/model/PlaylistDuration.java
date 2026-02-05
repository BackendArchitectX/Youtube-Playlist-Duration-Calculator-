package com.youtubeplaylistduration.model;

public class PlaylistDuration {

    private String totalLength;
    private String at1_25x;
    private String at1_50x;
    private String at1_75x;
    private String at2_00x;

    // ✅ Metadata (needed for setTotalVideos, setVideosCalculated, setStartIndex, setEndIndex)
    private Integer totalVideos;
    private Integer videosCalculated;
    private Integer startIndex;
    private Integer endIndex;

    public String getTotalLength() { return totalLength; }
    public void setTotalLength(String totalLength) { this.totalLength = totalLength; }

    public String getAt1_25x() { return at1_25x; }
    public void setAt1_25x(String at1_25x) { this.at1_25x = at1_25x; }

    public String getAt1_50x() { return at1_50x; }
    public void setAt1_50x(String at1_50x) { this.at1_50x = at1_50x; }

    public String getAt1_75x() { return at1_75x; }
    public void setAt1_75x(String at1_75x) { this.at1_75x = at1_75x; }

    public String getAt2_00x() { return at2_00x; }
    public void setAt2_00x(String at2_00x) { this.at2_00x = at2_00x; }

    public Integer getTotalVideos() { return totalVideos; }
    public void setTotalVideos(Integer totalVideos) { this.totalVideos = totalVideos; }

    public Integer getVideosCalculated() { return videosCalculated; }
    public void setVideosCalculated(Integer videosCalculated) { this.videosCalculated = videosCalculated; }

    public Integer getStartIndex() { return startIndex; }
    public void setStartIndex(Integer startIndex) { this.startIndex = startIndex; }

    public Integer getEndIndex() { return endIndex; }
    public void setEndIndex(Integer endIndex) { this.endIndex = endIndex; }
}
