package com.apps.daftarteman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(){
    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1

    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout.setOnClickListener{logout()}
        save.setOnClickListener{simpan()}
        show_data.setOnClickListener{lihat_data()}

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Admin")

    }

    private fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener{
                intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
        }
    }

    private fun simpan() {
        val getUserID = auth!!.currentUser!!.uid
        val database = FirebaseDatabase.getInstance()

        val getNama: String = nama.getText().toString()
        val getAlamat: String = alamat.getText().toString()
        val getNoHp: String = no_hp.getText().toString()

        val data = datateman(getNama, getAlamat, getNoHp)
        val userId = ref.push().key.toString()

        val getReference: DatabaseReference
        getReference = database.reference


        if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNoHp)) {
            Toast.makeText(this@MainActivity, "Data Anda Kosong!",
            Toast.LENGTH_SHORT).show()
        } else {
            getReference.child("Admin").child(getUserID).child("DataTeman").push()
                .setValue(datateman(getNama, getAlamat, getNoHp))
                .addOnCompleteListener(this) {
                    nama.setText("")
                    alamat.setText("")
                    no_hp.setText("")

                    Toast.makeText(this@MainActivity, "Data Tersimpan pak ekoooo!!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun lihat_data() {
        startActivity(Intent(this@MainActivity, MyListData::class.java))
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }


}