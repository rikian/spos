package com.gulali.spos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatEditText

class AddTransaction: ComponentActivity() {
    // on below line we are
    // creating variables for listview
    lateinit var programmingLanguagesLV: ListView

    // creating array adapter for listview
    lateinit var listAdapter: ArrayAdapter<String>

    // creating array list for listview
    lateinit var programmingLanguagesList: ArrayList<String>;

    // creating variable for searchview
    lateinit var searchView: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_transaction)

        // initializing variables of list view with their ids.
        programmingLanguagesLV = findViewById(R.id.idLVProgrammingLanguages)
        searchView = findViewById(R.id.idSV)

        // initializing list and adding data to list
        programmingLanguagesList = ArrayList()
        programmingLanguagesList.add("C")
        programmingLanguagesList.add("C#")
        programmingLanguagesList.add("Java")
        programmingLanguagesList.add("Javascript")
        programmingLanguagesList.add("Python")
        programmingLanguagesList.add("Dart")
        programmingLanguagesList.add("Kotlin")
        programmingLanguagesList.add("Typescript")

        // initializing list adapter and setting layout
        // for each list view item and adding array list to it.
        listAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, programmingLanguagesList)

        // on below line setting list
        // adapter to our list view.
        programmingLanguagesLV.adapter = listAdapter

        // on below line we are adding on query
        // listener for our search view.
        var cursorPositionSearch = 0
        var query: String = ""
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cursorPositionSearch = searchView.selectionStart
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    if (query == s.toString()) {
                        return
                    }
                    query = s.toString()
                    if (!programmingLanguagesList.contains(query)) {
                        return Toast.makeText(applicationContext, "No Language found..", Toast.LENGTH_LONG).show()
                    }
                    return listAdapter.filter.filter(query)
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // on below line we are checking
//                // if query exist or not.
//                if (programmingLanguagesList.contains(query)) {
//                    // if query exist within list we
//                    // are filtering our list adapter.
//                    listAdapter.filter.filter(query)
//                } else {
//                    // if query is not present we are displaying
//                    // a toast message as no  data found..
//                    Toast.makeText(applicationContext, "No Language found..", Toast.LENGTH_LONG).show()
//                }
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                // if query text is change in that case we
//                // are filtering our adapter with
//                // new text on below line.
//                listAdapter.filter.filter(newText)
//                return false
//            }
//        })
    }
}