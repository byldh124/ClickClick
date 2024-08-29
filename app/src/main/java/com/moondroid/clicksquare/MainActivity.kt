package com.moondroid.clicksquare

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.moondroid.clicksquare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mContext by lazy { this }
    private val adapter = NumberAdapter {
        clickBox(it)
    }

    private var myBaseTime = 0L
    private var myPauseTime = 0L

    val myTimer: Handler = object : Handler(Looper.getMainLooper()) {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            binding.tvTimer.text = getTimeOut()
            sendEmptyMessage(0)
        }
    }

    fun getTimeOut(): String {
        val now = System.currentTimeMillis()
        val outTime: Long = now - myBaseTime
        return String.format(
            "%02d:%02d:%02d",
            outTime / 1000 / 60,
            outTime / 1000 % 60,
            outTime % 1000 / 10
        )
    }

    private var stage = 1
    private var num = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        myTimer.removeMessages(0)
        _binding = null
    }

    private fun initView() {
        binding.container.adapter = adapter
        binding.btnStart.setOnClickListener {
            start()
        }
        binding.btnReset.setOnClickListener {
            pauseGame()
        }
        binding.btnFinish.setOnClickListener {
            binding.btnFinish.visible(false)
            binding.btnStart.visible(true)
            binding.tvTimer.visible(false)
        }
    }

    private fun start() {
        stage = 1
        binding.btnStart.visible(false)
        binding.btnFinish.visible(false)
        binding.container.visible(true)
        binding.btnReset.visible(true)
        binding.tvStage.visible(true)
        binding.tvTimer.visible(true)
        myBaseTime = System.currentTimeMillis()
        myTimer.sendEmptyMessage(0)

        setBox()
    }

    private var array = emptyArray<Int>()

    @SuppressLint("SetTextI18n")
    private fun setBox() {
        num = 1
        binding.tvStage.text = "$stage 단계 - 클릭 : $num "
        array = Array((stage + 1) * (stage + 1)) { it + 1 }
        array.shuffle()
        binding.container.layoutManager = GridLayoutManager(mContext, stage + 1)
        adapter.updateList(array.asList())
    }

    private fun clickBox(position: Int) {
        if (num == array[position]) {
            binding.container.getChildAt(position).visibility = View.INVISIBLE
            num++
            binding.tvStage.text = "$stage 단계 - 클릭 : $num "
        }
        if (num > array.size) {
            if (stage == 5) {
                gameOver()
            } else {
                stage++
                setBox()
            }
        }
    }

    private fun pauseGame() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("처음으로 돌아갈까요?")
        myTimer.removeMessages(0)
        myPauseTime = SystemClock.elapsedRealtime()
        builder.setPositiveButton("네") { dialog, which ->
            gameOver()
            binding.btnFinish.visible(false)
            binding.btnStart.visible(true)
            binding.tvTimer.visible(false)
        }

        builder.setNegativeButton("아니요") { dialog, which ->
            val now = SystemClock.elapsedRealtime()
            myTimer.sendEmptyMessage(0)
            myBaseTime += now - myPauseTime
        }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun gameOver() {
        binding.btnFinish.visible(true)
        binding.container.visible(false)
        binding.btnReset.visible(false)
        binding.tvStage.visible(false)
        myTimer.removeMessages(0)
    }

}

fun View.visible(b: Boolean) {
    visibility = if (b) View.VISIBLE else View.GONE
}