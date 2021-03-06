package com.example.flightmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var buttons: Array<Button>
    private lateinit var connectButton: Button
    private lateinit var input: TextView
    private lateinit var cache: DataCache
    private lateinit var db: UserRoomDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        db = UserRoomDatabase.getInstance(applicationContext)
        cache = DataCache(5, applicationContext)
        val dao = db.urlDao()
        var list: Array<UserURL> = dao.getAll()
        for (item in list.toList().asReversed()) {
            saveURL(item, true)
        }
        if (!cache.isEmpty()) {
            showOnButtons()
        }
        setButtonsListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        var arr = cache.data
        val dao = db.urlDao()
        dao.insert(arr.toList())
    }

    private fun setButtonsListeners() {
        setConnectListen()
        setUrlButtonsListen()
    }

    private fun setConnectListen() {
        connectButton.setOnClickListener {
            val url = input.text.toString().trim()
            input.hint = "Type URL..."
            saveURL(UserURL(url), false)
            showOnButtons()
            controlActivity()
        }
    }

    private fun setUrlButtonsListen() {
        for (button in buttons) {
            button.setOnClickListener {
                if (button.text.isNotEmpty()) {
                    input.text = button.text
                }
            }
        }
    }

    private fun saveURL(url: UserURL, isFirst: Boolean) {
        cache.insert(url, isFirst)
    }

    private fun initView() {
        button1 = findViewById(R.id.url_button1);
        button2 = findViewById(R.id.url_button2);
        button3 = findViewById(R.id.url_button3);
        button4 = findViewById(R.id.url_button4);
        button5 = findViewById(R.id.url_button5);
        connectButton = findViewById(R.id.connect_button);
        input = findViewById(R.id.user_url);
        buttons = arrayOf(button1, button2, button3, button4, button5);

        setButtonsVisibility();
    }

    private fun showOnButtons() {
        var i = 0
        while (i < cache.physize) {
            buttons[i].text = cache.data[i]?.url
            buttons[i].visibility = View.VISIBLE
            i++
        }
    }

    private fun setButtonsVisibility() {
        for (button in buttons) {
            button.visibility = View.GONE
        }
    }

    fun controlActivity() {
        var controlManager = ControlManager(this)

        if (input.text.toString() == "") {
            controlManager.setNotification("insert url")
            return
        }
        val url = input.text.toString()
        input.text = ""

        val connectionSucceed = controlManager.connect(url)
        if (!connectionSucceed) {
            controlManager.setNotification("connection failed")
        } else {
            val api = RetrofitBuilder.getApi(url)

            api.getScreenshot().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response:
                    Response<ResponseBody>
                ) {
                    if (response.code() == 404) {
                        controlManager.setNotification("connection failed")
                        return
                    }

                    val intent = Intent(this@MainActivity, Control::class.java)
                    intent.putExtra("url", url)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    controlManager.setNotification(t.message.toString())
                    return
                }
            })
        }
    }


}