package com.example.mymediaplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var list_names= ArrayList<String>()
        var list_songs_images = ArrayList<Int>()
        var list_songs = ArrayList<Int>()

        list_names.add("Dheere dheere se")
        list_names.add("Dusk till dawn")
        list_names.add("I like me better wen")
        list_names.add("Let me down slowly")

        list_songs_images.add(R.drawable.dheere_dheere_se)
        list_songs_images.add(R.drawable.dusk_till_dawn)
        list_songs_images.add(R.drawable.i_like_me_better)
        list_songs_images.add(R.drawable.let_me_down_slowly)

        list_songs.add(R.raw.dheere)
        list_songs.add(R.raw.dusk_till_dawn)
        list_songs.add(R.raw.i_like_me_better)
        list_songs.add(R.raw.let_me_down_slowly)



        var i=0
        var mp = MediaPlayer.create(this,list_songs[i])
        var n = (list_songs.size-1)
        fun onCompletion(mp:MediaPlayer){
            mp.setOnCompletionListener {
                next.callOnClick()
            }
        }

        play_btn.setOnClickListener {
            if (!mp.isPlaying) {

                songName.text = list_names[i]
                songImage.setImageResource(list_songs_images[i])
                seek_bar.progress = 0
                seek_bar.max = mp.duration
                mp.start()
                play_btn.setImageResource(R.drawable.pause)

            } else {
                mp.pause()
                play_btn.setImageResource(R.drawable.play)
            }

            onCompletion(mp)
        }


        prev.setOnClickListener{
            mp.pause()
            if(i==0) {
                seek_bar.progress=0
                seek_bar.max=mp.duration
                mp.seekTo(0)
                mp.start()

            }

            else if(i>0){
                mp = MediaPlayer.create(this,list_songs[--i])
                seek_bar.progress=0
                seek_bar.max = mp.duration
                songName.text = list_names[i]
                songImage.setImageResource(list_songs_images[i])
                mp.start()
            }
            onCompletion(mp)
        }

        next.setOnClickListener{

            mp.pause()

            if(i==n){
                seek_bar.progress=0
                seek_bar.max=mp.duration
                mp.seekTo(0)
                mp.start()
            }

            else if(i < n) {
                mp = MediaPlayer.create(this, list_songs[++i])
                seek_bar.progress = 0
                seek_bar.max = mp.duration
                songName.text = list_names[i]
                songImage.setImageResource(list_songs_images[i])
                mp.start()
            }
            onCompletion(mp)
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                if(fromUser){

                    mp.seekTo(progress)
                }
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        Thread(Runnable {
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: Exception) {
                }
            }
        }).start()


    }
    var handler=object : Handler(){
        override fun handleMessage(msg: Message) {
            var totalTime:Int = seek_bar.max
            var currentPosition = msg.what
            seek_bar.progress=currentPosition

            var elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = "-$remainingTime"

        }
    }
    fun createTimeLabel(time:Int):String{
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if(sec < 10)    timeLabel+="0"
        timeLabel +=sec

        return timeLabel
    }

}