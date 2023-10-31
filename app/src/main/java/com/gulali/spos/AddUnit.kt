package com.gulali.spos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.gulali.spos.database.SposDB
import com.gulali.spos.database.SposDao
import com.gulali.spos.database.UnitEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddUnit: ComponentActivity() {
    // view object
    private lateinit var unitName: EditText
    private lateinit var btnSaveUnit: Button

    // database unit
    private lateinit var unitEntity: UnitEntity
    private lateinit var sposDao: SposDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_new_unit)

        // initial view object
        unitName = findViewById(R.id.unit_name)
        btnSaveUnit = findViewById(R.id.save_unit)

        // initial unit dao
        sposDao = SposDB.getSposDatabase(applicationContext).sposDAO()

        btnSaveUnit.setOnClickListener{
            try {
                unitEntity = UnitEntity(
                    name = unitName.text.toString(),
                    createdAt = "",
                    updatedAt = ""
                )

                // Get the current date and time
                val calendar = Calendar.getInstance()
                val currentDateAndTime = calendar.time
                // Define the desired date and time format
                val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                // Format the current date and time to the desired format
                unitEntity.createdAt = dateFormat.format(currentDateAndTime)
                unitEntity.updatedAt = dateFormat.format(currentDateAndTime)
                sposDao.insertUnit(unitEntity)
                finish()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "failed insert unit to db\nerr: ${e.message.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    }
}