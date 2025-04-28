public class Episode {
    private String episodeName;
    private int episodeLength;

    public Episode(String episodeName, int episodeLength) {
        this.episodeName = episodeName;
        this.episodeLength = episodeLength;
    }

    public String getEpisodeName() {
        return this.episodeName;
    }

    public int getEpisodeLength() {
        return this.episodeLength;
    }
}