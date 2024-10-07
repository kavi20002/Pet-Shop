package com.example.petshop.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petshop.R
import com.example.petshop.taskModel.Task

class TaskAdapter(
    private var taskList: List<Task>,
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

     class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = itemView.findViewById(R.id.taskDescription)
        val time: TextView = itemView.findViewById(R.id.taskTime)
         val date: TextView = itemView.findViewById(R.id.taskDate)
        val completeButton: ImageButton = itemView.findViewById(R.id.completeButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.description.text = task.description
        holder.time.text = task.time
        holder.date.text = task.date

        if(task.isComplete){
            holder.completeButton.setImageResource(R.drawable.baseline_radio_button_checked_24)
        } else {
            holder.completeButton.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
        }

        holder.completeButton.setOnClickListener{
            task.isComplete = !task.isComplete
            notifyItemChanged(position)

        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }


}
