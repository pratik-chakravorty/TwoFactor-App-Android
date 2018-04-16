package com.pratik.twofactorauth;

/**
 * Created by Pratik on 4/9/2018.
 */
public class Artist {
    String artistId;
    String artistName;

    public Artist() {

    }

    public Artist(String artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;

    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

}
