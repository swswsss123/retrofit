package com.example.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.retrofit.api.CatJson
import com.example.retrofit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


const val BASE_URL = "https://cat-fact.herokuapp.com"
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var progresBar:ProgressBar
    private var TAG ="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        getCurrntdata()

        binding.layoutGenerateNewFact.setOnClickListener {
            getCurrntdata()
        }
    }

    private fun getCurrntdata() {
        binding.tvTextView.visibility = View.INVISIBLE
        binding.tvTimeStamp.visibility = View.INVISIBLE


        val api: ApiRequests = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequests::class.java)

        GlobalScope.launch (Dispatchers.IO){
            try {


            val responce:Response<CatJson> = api.getCatFacts().awaitResponse()
            if (responce.isSuccessful){
                var data:CatJson = responce.body()!!
                Log.d(TAG,data.text)

                withContext(Dispatchers.Main){
                    binding.tvTextView.visibility = View.VISIBLE
                    binding.tvTimeStamp.visibility = View.VISIBLE



                    binding.tvTextView.text = data.text
                    binding.tvTimeStamp.text = data.createdAt
                }
            }
        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(applicationContext,"Error for Internet...",Toast.LENGTH_LONG).show()
            }

            }
        }

    }
}