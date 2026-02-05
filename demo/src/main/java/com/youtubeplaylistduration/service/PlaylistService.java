package com.youtubeplaylistduration.service;

import com.youtubeplaylistduration.model.PlaylistDuration;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlaylistService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /**
     * Extract playlist ID from URL or return ID as-is
     */
    public String extractPlaylistId(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Playlist URL or ID cannot be empty");
        }

        input = input.trim();

        // Pattern to match playlist ID in various YouTube URL formats
        // Matches: ?list=ID, &list=ID, /playlist?list=ID
        Pattern pattern = Pattern.compile("(?:list=)([a-zA-Z0-9_-]+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        // If no URL pattern found, assume it's already a playlist ID
        return input;
    }

    /**
     * Calculate playlist duration with optional range filtering
     * @param playlistInput YouTube playlist URL or ID
     * @param fromIndex Starting video index (1-based, inclusive), null = start from first video
     * @param toIndex Ending video index (1-based, inclusive), null = end at last video
     * @return PlaylistDuration object with calculated durations and metadata
     */
    public PlaylistDuration calculatePlaylistDuration(String playlistInput, Integer fromIndex, Integer toIndex) {
        try {
            // Extract playlist ID from URL or use directly
            String playlistId = extractPlaylistId(playlistInput);

            // Build YouTube API client
            YouTube youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                null
            )
            .setApplicationName("YouTube-Playlist-Duration-Calculator")
            .build();

            // Fetch all video IDs from playlist (skips private/deleted videos)
            List<String> allVideoIds = fetchVideoIdsFromPlaylist(youtube, playlistId);
            int totalVideos = allVideoIds.size();

            if (totalVideos == 0) {
                throw new IllegalArgumentException("Playlist is empty or not found. Please check the playlist ID and try again.");
            }

            // Validate and adjust indices (1-based indexing)
            int actualFromIndex = (fromIndex != null && fromIndex > 0) ? fromIndex : 1;
            int actualToIndex = (toIndex != null && toIndex > 0) ? Math.min(toIndex, totalVideos) : totalVideos;

            // Validation
            if (actualFromIndex > totalVideos) {
                throw new IllegalArgumentException(
                    String.format("From index %d exceeds total videos %d in playlist", actualFromIndex, totalVideos)
                );
            }

            if (actualFromIndex > actualToIndex) {
                throw new IllegalArgumentException(
                    String.format("From index %d cannot be greater than To index %d", actualFromIndex, actualToIndex)
                );
            }

            // Filter video IDs based on range (convert to 0-based indexing)
            List<String> selectedVideoIds = allVideoIds.subList(actualFromIndex - 1, actualToIndex);

            // Calculate total duration for selected videos
            DurationResult result = calculateTotalDuration(youtube, selectedVideoIds);

            // Create and populate PlaylistDuration object
            PlaylistDuration playlistDuration = new PlaylistDuration();
            playlistDuration.setTotalLength(formatDuration(result.duration));
            playlistDuration.setAt1_25x(formatDuration(scaleDuration(result.duration, 1.25)));
            playlistDuration.setAt1_50x(formatDuration(scaleDuration(result.duration, 1.50)));
            playlistDuration.setAt1_75x(formatDuration(scaleDuration(result.duration, 1.75)));
            playlistDuration.setAt2_00x(formatDuration(scaleDuration(result.duration, 2.00)));
            
            // Set metadata
            playlistDuration.setTotalVideos(totalVideos);
            playlistDuration.setVideosCalculated(result.videosProcessed); // Actual videos with duration data
            playlistDuration.setStartIndex(actualFromIndex);
            playlistDuration.setEndIndex(actualToIndex);

            return playlistDuration;

        } catch (GoogleJsonResponseException e) {
            // Handle YouTube API specific errors (403, 400, 404, etc.)
            String message = "YouTube API error";
            
            if (e.getDetails() != null) {
                if (e.getDetails().getMessage() != null) {
                    message = e.getDetails().getMessage();
                }
                
                // Provide user-friendly messages for common errors
                if (e.getStatusCode() == 403) {
                    message = "API access forbidden. Please check your API key and ensure YouTube Data API v3 is enabled.";
                } else if (e.getStatusCode() == 404) {
                    message = "Playlist not found. Please verify the playlist URL or ID.";
                } else if (e.getStatusCode() == 400) {
                    message = "Invalid request. Please check the playlist URL format.";
                }
            } else {
                message = e.getMessage();
            }
            
            throw new IllegalArgumentException(message);
            
        } catch (IllegalArgumentException e) {
            // Re-throw validation errors as-is
            throw e;
            
        } catch (Exception e) {
            // Handle unexpected errors
            String message = "Error calculating playlist duration";
            if (e.getMessage() != null) {
                message += ": " + e.getMessage();
            }
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Fetch video IDs from playlist with null-safety for private/deleted videos
     */
    private List<String> fetchVideoIdsFromPlaylist(YouTube youtube, String playlistId) throws Exception {
        List<String> videoIds = new ArrayList<>();
        
        YouTube.PlaylistItems.List request = youtube.playlistItems()
            .list(Arrays.asList("contentDetails"))
            .setPlaylistId(playlistId)
            .setKey(apiKey)
            .setMaxResults(50L);

        String nextPageToken = null;
        do {
            request.setPageToken(nextPageToken);
            PlaylistItemListResponse response = request.execute();
            
            if (response.getItems() != null) {
                for (PlaylistItem item : response.getItems()) {
                    // Null-safety: skip items without content details (private/deleted videos)
                    if (item.getContentDetails() == null) {
                        continue;
                    }
                    
                    String videoId = item.getContentDetails().getVideoId();
                    
                    // Only add valid video IDs
                    if (videoId != null && !videoId.trim().isEmpty()) {
                        videoIds.add(videoId);
                    }
                }
            }
            
            nextPageToken = response.getNextPageToken();
        } while (nextPageToken != null);

        return videoIds;
    }

    /**
     * Calculate total duration with null-safety for unavailable videos
     * Returns both duration and count of videos successfully processed
     */
    private DurationResult calculateTotalDuration(YouTube youtube, List<String> videoIds) throws Exception {
        Duration totalDuration = Duration.ZERO;
        int videosProcessed = 0;

        // Process videos in batches of 50 (YouTube API limit)
        for (int i = 0; i < videoIds.size(); i += 50) {
            List<String> batch = videoIds.subList(i, Math.min(i + 50, videoIds.size()));

            YouTube.Videos.List request = youtube.videos()
                .list(Arrays.asList("contentDetails"))
                .setId(batch)
                .setKey(apiKey);

            VideoListResponse response = request.execute();
            
            if (response.getItems() != null) {
                for (Video video : response.getItems()) {
                    // Null-safety: skip videos without content details
                    if (video.getContentDetails() == null) {
                        continue;
                    }
                    
                    String durationStr = video.getContentDetails().getDuration();
                    
                    // Only parse valid ISO 8601 duration strings
                    if (durationStr != null && !durationStr.trim().isEmpty()) {
                        try {
                            Duration videoDuration = Duration.parse(durationStr);
                            totalDuration = totalDuration.plus(videoDuration);
                            videosProcessed++;
                        } catch (Exception e) {
                            // Skip videos with invalid duration format
                            System.err.println("Invalid duration format for video: " + durationStr);
                        }
                    }
                }
            }
        }

        return new DurationResult(totalDuration, videosProcessed);
    }

    /**
     * Format duration to human-readable string
     */
    private String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        if (days > 0) {
            return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d minutes, %d seconds", minutes, seconds);
        } else {
            return String.format("%d seconds", seconds);
        }
    }

    /**
     * Scale duration by playback speed factor
     */
    private Duration scaleDuration(Duration duration, double speedFactor) {
        long scaledSeconds = (long) (duration.getSeconds() / speedFactor);
        return Duration.ofSeconds(scaledSeconds);
    }

    /**
     * Inner class to hold duration calculation result with metadata
     */
    private static class DurationResult {
        final Duration duration;
        final int videosProcessed;

        DurationResult(Duration duration, int videosProcessed) {
            this.duration = duration;
            this.videosProcessed = videosProcessed;
        }
    }
}
