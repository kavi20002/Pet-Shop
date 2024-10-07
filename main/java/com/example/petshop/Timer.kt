package com.example.petshop

import android.app.Dialog
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class Timer : AppCompatActivity() {

    private var timeSelected: Int = 0
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffSet: Long = 0
    private var isStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        loadTimeFromSharedPrefs()

        val addBtn: ImageButton = findViewById(R.id.btnAdd)
        addBtn.setOnClickListener {
            setTimeFunction()
        }

        val startBtn: Button = findViewById(R.id.btnPlayPause)
        startBtn.setOnClickListener {
            startTimerSetup()
        }

        val resetBtn: ImageButton = findViewById(R.id.btnReset)
        resetBtn.setOnClickListener {
            resetTime()
        }

        val addTimeTv: TextView = findViewById(R.id.tv_addTime)
        addTimeTv.setOnClickListener {
            addExtraTime()
        }
    }

    private fun addExtraTime() {
        val progressBar: ProgressBar = findViewById(R.id.pbTimer)
        if (timeSelected != 0) {
            timeSelected += 15
            progressBar.max = timeSelected
            timePause()
            startTimer(pauseOffSet)
            Toast.makeText(this, "15 sec added", Toast.LENGTH_LONG).show()

            saveTimeInSharedPrefs(formatTime(timeSelected))
        }
    }

    private fun startTimer(pauseOffSetL: Long) {
        val progressBar: ProgressBar = findViewById(R.id.pbTimer)
        progressBar.progress = timeProgress
        timeCountDown = object : CountDownTimer(
            (timeSelected * 1000).toLong() - pauseOffSetL * 1000, 1000
        ) {
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffSet = timeSelected.toLong() - p0 / 1000
                progressBar.progress = timeSelected - timeProgress
                findViewById<TextView>(R.id.tvTimeLeft).text = formatTime(timeSelected - timeProgress)
            }

            override fun onFinish() {
                resetTime()
                vibrateDevice()
                playRingtone()
                Toast.makeText(this@Timer, getString(R.string.time_up), Toast.LENGTH_LONG).show()
            }
        }.start()
    }

    private fun vibrateDevice(){
        val vibrator = getSystemService(Vibrator::class.java)
        if (vibrator != null && vibrator.hasVibrator()){
            Log.d("Timer", "Vibrator found, proceeding to vibrate.")
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        }else {
            Log.d("Timer", "No vibrator found on this device>")
            Toast.makeText(this, "Vibration not supported on this device", Toast.LENGTH_LONG).show()
        }

    }

    private fun startTimerSetup() {
        val btnStart: Button = findViewById(R.id.btnPlayPause)
        if (isStart) {
            btnStart.text = "Pause"
            startTimer(pauseOffSet)
            isStart = false
        } else {
            btnStart.text = "Resume"
            timePause()
            isStart = true
        }
    }

    private fun timePause() {
        timeCountDown?.cancel()
    }

    private fun resetTime() {
        val progressBar: ProgressBar = findViewById(R.id.pbTimer)
        timeCountDown?.cancel()
        timeProgress = 0
        pauseOffSet = 0
        progressBar.progress = timeProgress
        findViewById<TextView>(R.id.tvTimeLeft).text = formatTime(timeSelected)
        findViewById<Button>(R.id.btnPlayPause).text = "Start"
    }

    private fun setTimeFunction() {
        val timeDialog = Dialog(this)
        timeDialog.setContentView(R.layout.add_time)

        val hoursInput = timeDialog.findViewById<EditText>(R.id.etHours)
        val minutesInput = timeDialog.findViewById<EditText>(R.id.etMinutes)
        val secondInput = timeDialog.findViewById<EditText>(R.id.etSeconds)

        val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
        val addTimeTv: TextView = findViewById(R.id.tv_addTime)
        val btnStart: Button = findViewById(R.id.btnPlayPause)
        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)

        timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            val hours = hoursInput.text.toString().ifEmpty { "0" }.toInt()
            val minutes = minutesInput.text.toString().ifEmpty { "0" }.toInt()
            val seconds = secondInput.text.toString().ifEmpty { "0" }.toInt()

            timeSelected = hours * 3600 + minutes * 60 + seconds

            if (timeSelected > 0){
                val formattedTime = formatTime(timeSelected)
                saveTimeInSharedPrefs(formattedTime)

                addTimeTv.text = formattedTime
                timeLeftTv.text = formattedTime
                btnStart.text = "Start"
                progressBar.max = timeSelected

                resetTime()
            }else {
                Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_LONG).show()
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, secs)
    }

    private fun saveTimeInSharedPrefs(time: String) {
        val sharedPreferences = getSharedPreferences("timerPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("savedTime", time)
        editor.apply()
    }

    private fun loadTimeFromSharedPrefs() {
        val sharedPreferences = getSharedPreferences("timerPrefs", MODE_PRIVATE)
        val savedTime = sharedPreferences.getString("savedTime", "00:00:00")
        val addTimeTv: TextView = findViewById(R.id.tv_addTime)

        if (savedTime == null) {
            addTimeTv.text = "00:00:00"
        } else {
            addTimeTv.text = savedTime
        }
    }

    private fun playRingtone(){
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)
        ringtone.play()

        val timer  = object :  CountDownTimer(10000, 100) {
            override fun onTick(millisUntilFinished : Long) {}

            override fun onFinish() {
                ringtone.stop()
            }

        }
        timer.start()
    }

}
