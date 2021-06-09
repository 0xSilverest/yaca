package com.ensate.chatapp.client.view;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AudioPlayer extends Group {
    private String playButtonG = "";
    private String pauseButtonG = "";
    private Duration duration;

    private Media sound;
    private MediaPlayer mediaPlayer;
    private Label time;
    private Slider slider;
    
    AudioPlayer (String filePath) {
        super();
        this.sound = new Media(filePath);
        this.mediaPlayer = new MediaPlayer(sound);
        init();
    }

    public void play() {
        mediaPlayer.play();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    private void init() {
        Button playBtn = new Button();
        playBtn.setText(playButtonG);
        playBtn.setOnAction(e -> {
            updateValues();
            Status status = mediaPlayer.getStatus();
            
            if (status == Status.PAUSED
            || status == Status.READY
            || status == Status.STOPPED) {
                mediaPlayer.play();
                playBtn.setText(playButtonG); 
            } else {
                mediaPlayer.pause();
                playBtn.setText(pauseButtonG);
            }
        });

        time = new Label();
        time.setTextFill(Color.YELLOW);
        time.setPrefWidth(80);

        slider = new Slider();

        HBox.setHgrow(slider, Priority.ALWAYS);
        slider.setMinSize(250, 40);
        slider.valueProperty()
            .addListener(o -> {
                if (slider.isValueChanging()) {
                    if (duration != null) {
                        mediaPlayer.seek(duration);
                    }
                }
            });

        mediaPlayer.currentTimeProperty()
            .addListener(ov -> updateValues());

        mediaPlayer.setOnReady(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateValues();
        });
    }

    private void updateValues() {
        if (time != null && slider != null && duration != null) {
            Platform.runLater(() -> {
                Duration curr = mediaPlayer.getCurrentTime();
                time.setText(formatTime(curr, duration));
                slider.setDisable(duration.isUnknown());
                if (!slider.isDisabled() 
                        && duration.greaterThan(Duration.ZERO) 
                        && !slider.isValueChanging()) {

                        }
            });
        }
    }

    private String formatTime (Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;
        
        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
            intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                    elapsedHours, elapsedMinutes, elapsedSeconds,
                    durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                    elapsedMinutes, elapsedSeconds, durationMinutes,
                    durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                    elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                    elapsedSeconds);
            }
        }
    }
}
