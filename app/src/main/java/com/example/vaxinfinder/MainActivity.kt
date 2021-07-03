package com.example.vaxinfinder

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var searchButton: Button
    lateinit var pinCodeEdt: EditText
    lateinit var centersRV: RecyclerView
    lateinit var centerRVAdapter: CenterRVAdaptor
    lateinit var centerList: List<CenterRVModal>
    lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton=findViewById(R.id.btnsearch)
        pinCodeEdt=findViewById(R.id.enterpincode)
        centersRV=findViewById(R.id.Rview)
        loadingPB=findViewById(R.id.Pbar)
        centerList=ArrayList<CenterRVModal>()

        searchButton.setOnClickListener() {
            val pincode = pinCodeEdt.text.toString()

            if (pincode.length != 6) {
                Toast.makeText(this, "Enter Valid Pincode", Toast.LENGTH_LONG).show()
            } else {
                (centerList as ArrayList<CenterRVModal>).clear()
                val cd = Calendar.getInstance()
                val year = cd.get(Calendar.YEAR)
                val mon = cd.get(Calendar.MONTH)
                val day = cd.get(Calendar.DAY_OF_MONTH)
                val datepicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    loadingPB.setVisibility(View.VISIBLE)
                    val dtstr: String = """$dayOfMonth-${month + 1}-$year"""
                    getAppointment(pincode,dtstr)

                },
                        year,
                        mon,
                        day
                )
                datepicker.show()
            }

        }
    }

    private fun getAppointment(pincode: String,date: String){
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+pincode+"&date="+date
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            loadingPB.setVisibility(View.GONE)
            try{
                val centerArray = response.getJSONArray("centers")
                if (centerArray.length().equals(0)) {
                    Toast.makeText(this, "No Center Found", Toast.LENGTH_SHORT).show()
                }
                for(i in 0 until centerArray.length()) {
                    val centerObj = centerArray.getJSONObject(i)

                    val centerName: String = centerObj.getString("name")
                    val centerAddress: String = centerObj.getString("address")
                    val centerFromTime: String = centerObj.getString("from")
                    val centerToTime: String = centerObj.getString("to")
                    val fee_type: String = centerObj.getString("fee_type")

                    val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                    val ageLimit: Int = sessionObj.getInt("min_age_limit")
                    val vaccineName: String = sessionObj.getString("vaccine")
                    val avaliableCapacity: Int = sessionObj.getInt("available_capacity")

                    val center = CenterRVModal(
                            centerName,centerAddress,centerFromTime,centerToTime,
                            vaccineName,fee_type,ageLimit,avaliableCapacity
                    )
                    centerList = centerList+center
                }

                centerRVAdapter=CenterRVAdaptor(centerList)
                centersRV.layoutManager=LinearLayoutManager(this)
                centersRV.adapter=centerRVAdapter

                }catch (e:JSONException){
                loadingPB.setVisibility(View.GONE)
                e.printStackTrace()
            }
        },{
            error ->
            loadingPB.setVisibility(View.GONE)
            Toast.makeText(this,"Failed to get data at this moment",Toast.LENGTH_SHORT).show()
        })
        queue.add(request)

    }
    
}
