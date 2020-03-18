package com.example.tobuylist

import android.annotation.SuppressLint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val actionBar = supportActionBar
        actionBar!!.title = "물품 추가하기"

        val id = intent.getLongExtra("id", -1L)
        if(id == -1L){
            insertMode()
        }else{
            updateMode(id)
        }
        toBuyEditText.addTextChangedListener(object : TextWatcher{
            var title : String = ""
            override fun afterTextChanged(s: Editable?) {
                title = toBuyEditText.text.toString().replace(" ","")
                urlEditText.setText("https://search.shopping.naver.com/search/all.nhn?query=$title&cat_id=&frm=NVSHATC")
                Log.v("Edit", "after")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.v("Edit", "before")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.v("Edit", "on")
            }

        })
        searchFab.setOnClickListener {
            urlEditText.text.toString().trim()?.let {
                if(it.contains("http")){
                    browse(it)
                    toast("해당 주소로 이동합니다.")
                }else{
                    browse("https://"+it)
                    //toast("http 붙여주라")
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun insertMode(){
        deleteFab.visibility = View.GONE

        doneFab.setOnClickListener {
            insertTodo()
        }
    }

    private fun updateMode(id: Long){
        val todo = realm.where<Todo>().equalTo("id", id).findFirst()!!
        toBuyEditText.setText(todo.name)
        urlEditText.setText(todo.url)
        priceEditText.setText(todo.price.toString())
        impEditText.setText(todo.imp.toString())

        doneFab.setOnClickListener {
            updateTodo(id)
        }
        deleteFab.setOnClickListener {
            deleteTodo(id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun editIsEmpty() : Boolean{
        Log.v("editIsEmpty", "나 불렀어?")
        if(priceEditText.text.toString().trim().isEmpty() && impEditText.text.toString().trim().isEmpty()){
            Log.v("editIsEmpty", "값이 없음")
            toast("값을 입력해 주세요.")
            return true
        }
        return false
    }

    private fun insertTodo(){
        if (!editIsEmpty()){
            realm.beginTransaction()

            val newItem = realm.createObject<Todo>(nextId())
            newItem.name = toBuyEditText.text.toString()
            newItem.url = urlEditText.text.toString()
            newItem.price = priceEditText.text.toString().toInt()
            newItem.imp = impEditText.text.toString().toInt()

            realm.commitTransaction()

            toast("추가 완료.")
            finish()
        }
    }
    private fun updateTodo(id: Long){
        realm.beginTransaction()

        val updateItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        updateItem.name = toBuyEditText.text.toString()
        updateItem.url = urlEditText.text.toString()
        updateItem.price = priceEditText.text.toString().toInt()
        updateItem.imp = impEditText.text.toString().toInt()

        realm.commitTransaction()
        toast("변경 완료.")
        finish()
    }
    private fun deleteTodo(id: Long){
        realm.beginTransaction()
        val deleteItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        deleteItem.deleteFromRealm()
        realm.commitTransaction()

        toast("삭제 완료.")
        finish()
    }
    private fun nextId(): Int{
        val maxId = realm.where<Todo>().max("id")
        if(maxId != null){
            return maxId.toInt() +1
        }
        return 0
    }
}
