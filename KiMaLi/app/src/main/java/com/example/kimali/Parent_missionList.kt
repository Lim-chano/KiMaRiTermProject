package com.example.kimali

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_parent_mission_list.*
import java.util.*

class Parent_missionList : AppCompatActivity() {

    lateinit var text : String
    lateinit var who : String
    lateinit var name : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_parent_mission_list)

        val textView1 = findViewById(R.id.year_select) as TextView
        val textView2 = findViewById(R.id.month_select) as TextView
        val textView3 = findViewById(R.id.day_select) as TextView

        val c=Calendar.getInstance()
        textView1.setText(Integer.toString(c.get(Calendar.YEAR)))
        textView2.setText(Integer.toString(c.get(Calendar.MONTH)+1))
        textView3.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)))

        val addButton=findViewById(R.id.addButton) as Button
        addButton.setOnClickListener { view->
            val intent = Intent(this, parent_setting_add_activity::class.java)
            intent.putExtra("who",who)
            intent.putExtra("name",name)
            intent.putExtra("selectedString", text)

            //intent.putExtra("selectedString", selectedItem)
            this.startActivity(intent)
        }


        if (intent.hasExtra("selectedString")) {
            text = intent.getStringExtra("selectedString")
            who = intent.getStringExtra("who")
            name = intent.getStringExtra("name")

            setTitle(name)
            /* "nameKey"라는 이름의 key에 저장된 값이 있다면
               textView의 내용을 "nameKey" key에서 꺼내온 값으로 바꾼다 */

        } else {
            Toast.makeText(this, "전달된 이름이 없습니다", Toast.LENGTH_SHORT).show()
        }
        if(who.equals("보호자")){
            addButton.setEnabled(true);
            addButton.setVisibility(Button.VISIBLE);
        }else {
            addButton.setEnabled(false);
            addButton.setVisibility(Button.INVISIBLE);
        }

        val array: Array<String> = arrayOf("청소기돌리기","설거지하기","빨래하기")
        val array2: Array<Int> = arrayOf(5000,3000,7000)

        //baseAdapter로 생성
        mission_list.adapter = HBaseAdapter(this, array, array2)
        mission_list.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            Toast.makeText(this, "Clicked item :"+" "+selectedItem, Toast.LENGTH_SHORT).show()
            if(who=="보호자") {
                val intent = Intent(this, parent_listview_activity::class.java)
                intent.putExtra("who", who)
                intent.putExtra("position", position)
                intent.putExtra("selectedString", text)
                this.startActivity(intent)
            }
            else {
                val intent = Intent(this, child_listview_activity::class.java)
                intent.putExtra("who", who)
                intent.putExtra("position", position)
                intent.putExtra("selectedString", text)
                this.startActivity(intent)
            }
        }



    }


}
