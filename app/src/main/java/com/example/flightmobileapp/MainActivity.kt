package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView();
        cache = DataCache(5, applicationContext)
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
        }
    }

    private fun setUrlButtonsListen() {
        for (button in buttons) {
            button.setOnClickListener {
                if (button.text.isNotEmpty()) {
                    user_url.text = button.text
                }
            }
        }
    }

    private fun saveURL(url: String) {
        cache.insert(url)
//        val ref = FirebaseDatabase.getInstance().getReference("URLs")
//        val id = ref.push().key
//        val obj = id?.let { UserURL(it, url) }
//        if (id != null) {
//            ref.child(id).setValue(obj).addOnCompleteListener {
//                Toast.makeText(applicationContext, "Saved!", Toast.LENGTH_LONG).show()
//            }
//        }
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

}