package com.youtubeplaylistduration.controller;

import com.youtubeplaylistduration.model.PlaylistDuration;
import com.youtubeplaylistduration.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playlist")
@CrossOrigin(origins = "*")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    /**
     * Get playlist duration with optional range
     * Supports both full playlist and custom range in single endpoint
     */
    @GetMapping("/duration")
    public PlaylistDuration getPlaylistDuration(
        @RequestParam String playlistId,
        @RequestParam(required = false) Integer fromIndex,
        @RequestParam(required = false) Integer toIndex) {
    return playlistService.calculatePlaylistDuration(playlistId, fromIndex, toIndex);
    }

}
