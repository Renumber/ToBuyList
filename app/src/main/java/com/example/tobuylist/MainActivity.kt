package com.example.tobuylist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.email
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    val realm = Realm.getDefaultInstance()
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val realmResult = realm.where<Todo>().findAll().sort("imp")
        val adapter = TodoListAdapter(realmResult)
        listView.adapter = adapter

        realmResult.addChangeListener { _ -> adapter.notifyDataSetChanged() }

        listView.setOnItemClickListener{parent, view, position, id->
            startActivity<EditActivity>("id" to id)
        }


        fab.setOnClickListener {
            startActivity<EditActivity>()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onResume() {
        super.onResume()
        val priceFormatter = DecimalFormat("###,###")
        val totalPrice = priceFormatter.format(realm.where<Todo>().sum("price")) + "원"
        val actionBar = supportActionBar
        actionBar!!.title = "필요한 돈 : $totalPrice"
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when(item?.itemId){
            R.id.action_settings ->{
                count++
                if(10 > count && count >= 5) {
                    toast("그만 눌러" + "!".repeat(count-4))
                }else if(count >= 10){
                    startActivity<GameActivity>()
                    count = 0
                }else{
                    toast("아직 안만듬ㅋ")
                }
                return true
            }
            R.id.action_contact ->{
                email("renumber20@gmail.com", "Your App is Awesome!", "ㅈㄱㄴ")
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
