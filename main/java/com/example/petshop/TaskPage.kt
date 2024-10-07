package com.example.petshop

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petshop.Adapters.TaskAdapter
import com.example.petshop.taskModel.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskPage : AppCompatActivity() {

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<Task>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_page)
        supportActionBar?.hide()

        requestNotificationPermission()

        createNotificationChannel()

        taskRecyclerView = findViewById(R.id.taskRecycleView)
        taskAdapter = TaskAdapter(tasks)
        taskRecyclerView.adapter = taskAdapter
        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        loadTasks()
        setUpSwipe()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            addNewTask(null, -1)
        }

        val petTimer  = findViewById<Button>(R.id.petTimer)
        petTimer.setOnClickListener{
            val intent = Intent(this, Timer::class.java)
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if (!canScheduleExactAlarms()){
                requestExactAlarmPermission()
            }
        }
    }

    private fun requestNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                 != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1
                )
            }
        }
    }

    private fun addNewTask(task: Task?, position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.new_task, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        val addTaskDescription = view.findViewById<EditText>(R.id.newTaskText)
        val addTaskTime = view.findViewById<EditText>(R.id.newTaskTime)
        val addTaskDate = view.findViewById<EditText>(R.id.newTaskDate)
        val taskSaveBtn = view.findViewById<Button>(R.id.taskSaveBtn)

        task?.let {
            addTaskDescription.setText(it.description)
            addTaskTime.setText(it.time)
            addTaskDate.setText(it.date)
        }

        taskSaveBtn.setOnClickListener {
            val description = addTaskDescription.text.toString()
            val time = addTaskTime.text.toString()
            val date = addTaskDate.text.toString()

            if (description.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
                val timeInMillis = convertToMillis(date, time)

                if (timeInMillis != 0L) {
                    if (position == -1) {
                        tasks.add(Task(description, time, date, false))
                        taskAdapter.notifyItemInserted(tasks.size - 1)
                    } else {
                        tasks[position].description = description
                        tasks[position].time = time
                        tasks[position].date = date
                        taskAdapter.notifyItemChanged(position)
                    }

                    scheduleNotification(timeInMillis, description)
                    saveTask()
                    bottomSheetDialog.dismiss()
                } else {
                    Toast.makeText(this, "Invalid date or time format", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter both description and time", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun convertToMillis(date: String, time: String): Long {
        val format = SimpleDateFormat("M/d/yyyy h:mm a", Locale.getDefault())
        return try {
            val dateTime = "$date $time"
            val parsedDate = format.parse(dateTime)
            val calendar = Calendar.getInstance()
            if (parsedDate != null) {
                calendar.time = parsedDate
                calendar.timeInMillis
            } else {
                0L
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            0L
        }
    }

    private fun scheduleNotification(timeInMillis: Long, taskDescription: String) {
        val intent = Intent(this, NotificationReceiver::class.java)
        intent.putExtra("task_description", taskDescription)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            taskDescription.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }

        Log.d("TaskPage", "Scheduling task: $taskDescription at $timeInMillis")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_channel",
                "Task Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for task notifications"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun saveTask() {
        val sharedPreferences = getSharedPreferences("task_pref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val updatedJson = gson.toJson(tasks)
        editor.putString("task_lists", updatedJson)
        editor.apply()
    }

    private fun loadTasks() {
        val sharedPreferences = getSharedPreferences("task_pref", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("task_lists", null)
        val type = object : TypeToken<MutableList<Task>>() {}.type

        if (json != null) {
            val savedTasks: MutableList<Task> = gson.fromJson(json, type)
            tasks.addAll(savedTasks)
            taskAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpSwipe() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> editTask(position)
                    ItemTouchHelper.LEFT -> deleteTask(position)
                }
            }

            override fun onChildDrawOver(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder?,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(this@TaskPage, R.color.button))
                    .addSwipeRightActionIcon(R.drawable.baseline_edit_24)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@TaskPage, R.color.delete))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_24)
                    .create()
                    .decorate()

                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val itemTouch = ItemTouchHelper(itemTouchHelperCallback)
        itemTouch.attachToRecyclerView(taskRecyclerView)
    }

    private fun editTask(position: Int) {
        val task = tasks[position]
        addNewTask(task, position)
    }

    private fun deleteTask(position: Int) {
        tasks.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        saveTask()
    }

    private fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.ALARM_SERVICE) as AlarmManager).canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }
}




