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

    lateinit var button1: Button;
    lateinit var button2: Button;
    lateinit var button3: Button;
    lateinit var button4: Button;
    lateinit var button5: Button;
    lateinit var buttons: Array<Button>;
    lateinit var connect_button: Button;
    lateinit var user_url: TextView;
    lateinit var cache: DataCache;
    private var controlManager = ControlManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView();
        cache = DataCache(5, applicationContext)
        if (!cache.isEmpty()) {
            showOnButtons()
        }
        setButtonsListeners()
    }

    private fun setButtonsListeners() {
        setConnectListen()
        setUrlButtonsListen()
    }

    private fun setConnectListen() {
        connect_button.setOnClickListener {
            val url = user_url.text.toString().trim()
            user_url.hint = "Type URL..."
            user_url.text = ""
            saveURL(url)
            showOnButtons()
            val intent = Intent(this, Control::class.java)
            startActivity(intent)
        }
    }

    private fun setUrlButtonsListen() {
        for (button in buttons) {
            button.setOnClickListener {
                if (button.text.isNotEmpty()) {
                    user_url.text = button.text
                    cache.insert(button.text.toString())
                    showOnButtons()
                }
            }
        }
    }

    private fun saveURL(url: String) {
        cache.insert(url)
    }

    private fun initView() {
        button1 = findViewById(R.id.url_button1);
        button2 = findViewById(R.id.url_button2);
        button3 = findViewById(R.id.url_button3);
        button4 = findViewById(R.id.url_button4);
        button5 = findViewById(R.id.url_button5);
        connect_button = findViewById(R.id.connect_button);
        user_url = findViewById(R.id.user_url);
        buttons = arrayOf(button1, button2, button3, button4, button5);

        setButtonsVisibility();
    }

    private fun showOnButtons() {
        var i = 0
        while (i < cache.physize) {
            buttons[i].text = cache.data[i]
            buttons[i].visibility = View.VISIBLE
            i++
        }
    }

    private fun setButtonsVisibility() {
        for (button in buttons) {
            button.visibility = View.GONE
        }
    }


    fun controlActivity(view: View) {
        // connect to server with the given url
        view.id
        if (user_url.text.toString() == "") {
            controlManager.setNotification("insert url")
            return
        }
        val url = user_url.text.toString()
        // insert to cache ????

        val connectionSucceed = controlManager.connect(url)
        if (!connectionSucceed) {
            controlManager.setNotification("connection failed")
            user_url.setText("")
        } else {
            val api = RetrofitBuilder.getApi()
            if (api == null) {
                // problem with server
                controlManager.setNotification("can't connect to server")
            }
            api?.getScreenshot()?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response:
                Response<ResponseBody>) {
                    if (response.code() == 404) {
                        controlManager.setNotification("connection failed")
                        user_url.setText("")
                        return
                    }
                    val intent = Intent(this@MainActivity, Control::class.java)
                    intent.putExtra("url", url)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    controlManager.setNotification("connection failed")
                    user_url.setText("")
                    return
                }
            })
        }
    }



}