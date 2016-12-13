package com.kuwapp.twitcastingviewersample;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchLiveMovieResponse {

    @SerializedName("movies")
    private List<MovieInfo> movieInfoList;

    public List<MovieInfo> getMovieInfoList() {
        return movieInfoList;
    }

    public static class MovieInfo {

        @SerializedName("movie")
        private Movie movie;
        @SerializedName("broadcaster")
        private Broadcaster broadcaster;

        public Movie getMovie() {
            return movie;
        }

        public Broadcaster getBroadcaster() {
            return broadcaster;
        }

    }

    public static class Movie {

        @SerializedName("title")
        private String title;
        @SerializedName("small_thumbnail")
        private String smallThumbnail;
        @SerializedName("hls_url")
        private String hlsUrl;

        public String getTitle() {
            return title;
        }

        public String getSmallThumbnail() {
            return smallThumbnail;
        }

        public String getHlsUrl() {
            return hlsUrl;
        }
    }

    public static class Broadcaster {

        @SerializedName("screen_id")
        private String screenId;
        @SerializedName("name")
        private String name;

        public String getScreenId() {
            return screenId;
        }

        public String getName() {
            return name;
        }
    }

}
