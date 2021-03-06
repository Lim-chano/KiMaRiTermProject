package com.example.kimali.Mission

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kimali.R
import com.google.firebase.database.*
import java.util.*

class DetailMission : AppCompatActivity() {
    private lateinit var mDatabase: DatabaseReference

    lateinit var userId: String
    lateinit var who: String
    lateinit var name: String
    lateinit var topic: String


    lateinit var missionName : String
    lateinit var deadline : String
    lateinit var mission_message: String
    lateinit var money: String
    lateinit var pcTime: String
    var total_money = 0
    var total_pcTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        userId = intent.getStringExtra("id")
        who = intent.getStringExtra("who")
        name = intent.getStringExtra("name")
        topic = intent.getStringExtra("topic")

        missionName = intent.getStringExtra("missionName")
        deadline = intent.getStringExtra("deadline")
        mission_message = intent.getStringExtra("mission_message")
        money = intent.getStringExtra("money")
        pcTime = intent.getStringExtra("pcTime")

        mDatabase = FirebaseDatabase.getInstance().reference

        Log.d("sangmee", topic)
        setTitle(name)


        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_NoActionBar)
        setContentView(R.layout.activity_detail_mission)


        val okButton=findViewById<Button>(R.id.ok_mission_button)
        val modifyButton=findViewById<Button>(R.id.modify_mission_button)
        val deleteButton=findViewById<Button>(R.id.delete_mission_button)

        val deadLineText=findViewById<TextView>(R.id.deadline_textview)
        val moneyText=findViewById<TextView>(R.id.moneyText)
        val pcTimeText=findViewById<TextView>(R.id.pcTimeText)

        val missionMessage=findViewById<TextView>(R.id.mission_Message_textView)
        val missionName_text=findViewById<TextView>(R.id.mission_Name_TextView)

        missionName_text.setText(missionName)
        missionMessage.setText(mission_message)
        moneyText.setText(money)
        pcTimeText.setText(pcTime)

        mDatabase.child("mission").child(topic).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) { // Get user value


                        total_money = dataSnapshot.child("total_money").child("moneys").value.toString().toInt()
                        total_pcTime = dataSnapshot.child("total_pcTime").child("pcTimes").value.toString().toInt()
                    Log.d("sangmeeTotalMoney", total_money.toString())
                    Log.d("sangmeeTatalPCTime", total_pcTime.toString())


                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        val c= Calendar.getInstance()

        var dateString=""
        dateString+=Integer.toString(c.get(Calendar.YEAR))+"-"
        dateString+=Integer.toString(c.get(Calendar.MONTH)+1)+"-"
        dateString+=Integer.toString(c.get(Calendar.DAY_OF_MONTH))
        deadLineText.setText(dateString+" ~ "+deadline)


        okButton.setOnClickListener { view->
            total_money+=money.toInt()
            total_pcTime += pcTime.toInt()
            writeChild()
            val intent = Intent(this, MissionList::class.java)
            intent.putExtra("id", userId)
            intent.putExtra("who", who)
            intent.putExtra("name", name)
            intent.putExtra("topic", topic)

            //여기에 파이어베이스에서 현재 자녀의 이름 가지고오는 코드가 들어가야함..//
            this.startActivity(intent)
            finish()
        }


    }

    private fun writeChild() {
        val post1 = Moneys_DB(total_money.toString())
        val post2 = PcTimes_DB(total_pcTime.toString())
        val postValues1 = post1.toMap()
        val postValues2 = post2.toMap()
        val childUpdates1 = HashMap<String, Any>()
        val childUpdates2 = HashMap<String, Any>()
        val childUpdates3 = HashMap<String, Any>()
        childUpdates1["/mission/$topic/total_money"] = postValues1
        childUpdates2["/mission/$topic/total_pcTime"] = postValues2
        mDatabase.child("mission/$topic/detailmission/$missionName").setValue(null)

        mDatabase.updateChildren(childUpdates1)
        mDatabase.updateChildren(childUpdates2)
    }

}