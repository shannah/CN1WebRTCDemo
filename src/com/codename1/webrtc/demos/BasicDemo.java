/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.webrtc.demos;

import com.codename1.io.Log;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.webrtc.AudioTrackConstraints;
import com.codename1.webrtc.ConstrainBoolean;
import com.codename1.webrtc.MediaStreamConstraints;
import com.codename1.webrtc.RTC;
import com.codename1.webrtc.RTCVideoElement;

/**
 *
 * @author shannah
 */
public class BasicDemo extends Form implements AutoCloseable {
    private RTC rtc;
    public BasicDemo() {
        super("Basic WebRTC Demo", new BorderLayout());
        Form hi = this;
        RTC.createRTC().ready(rtc->{
            this.rtc = rtc;
            MediaStreamConstraints constraints = new MediaStreamConstraints()
                    .audio()
                    .echoCancellation(true)
                    .noiseSuppression(true)
                    .video(true)
                    .stream();

           
            rtc.getUserMedia(constraints).then(stream->{
                System.out.println("Acquired stream from user media");
                RTCVideoElement video = rtc.createVideo();
                video.setAutoplay(true);
                System.out.println("Setting srcObject to stream");
                video.setSrcObject(stream);
                System.out.println("Appending video to dom");
                rtc.append(video);
                System.out.println("Video successfully appended");
            }).onCatch(error-> {
                System.out.println("Failed to get user media: "+error.getMessage());
                Log.e(error);
            });
            hi.add(BorderLayout.CENTER, rtc.getVideoComponent());
            hi.revalidate();
        });
    }

    @Override
    public void close() throws Exception {
        if (rtc != null) {
            rtc.close();
            rtc = null;
        }
    }
}
