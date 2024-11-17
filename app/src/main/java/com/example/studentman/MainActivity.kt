package com.example.studentman

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var students: MutableList<StudentModel>
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val students = mutableListOf(
            StudentModel("Nguyễn Văn An", "SV001"),
            StudentModel("Trần Thị Bảo", "SV002"),
            StudentModel("Lê Hoàng Cường", "SV003"),
            StudentModel("Phạm Thị Dung", "SV004"),
            StudentModel("Đỗ Minh Đức", "SV005"),
            StudentModel("Vũ Thị Hoa", "SV006"),
            StudentModel("Hoàng Văn Hải", "SV007"),
            StudentModel("Bùi Thị Hạnh", "SV008"),
            StudentModel("Đinh Văn Hùng", "SV009"),
            StudentModel("Nguyễn Thị Linh", "SV010"),
            StudentModel("Phạm Văn Long", "SV011"),
            StudentModel("Trần Thị Mai", "SV012"),
            StudentModel("Lê Thị Ngọc", "SV013"),
            StudentModel("Vũ Văn Nam", "SV014"),
            StudentModel("Hoàng Thị Phương", "SV015"),
            StudentModel("Đỗ Văn Quân", "SV016"),
            StudentModel("Nguyễn Thị Thu", "SV017"),
            StudentModel("Trần Văn Tài", "SV018"),
            StudentModel("Phạm Thị Tuyết", "SV019"),
            StudentModel("Lê Văn Vũ", "SV020")
        )

        studentAdapter = StudentAdapter(
            students,
            onEditClick = { student, position ->
                editStudent(students, studentAdapter, student, position)
            },
            onDeleteClick = { student, position ->
                confirmDeleteStudent(students, studentAdapter, student, position)
            }
        )

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            showAddStudentDialog(students, studentAdapter)
        }
    }

    // Show the Add Student dialog
    private fun showAddStudentDialog(students: MutableList<StudentModel>, studentAdapter: StudentAdapter) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_new_student)

        val editFullName = dialog.findViewById<EditText>(R.id.edit_fullname)
        val editMSSV = dialog.findViewById<EditText>(R.id.edit_mssv)

        dialog.findViewById<Button>(R.id.button_ok).setOnClickListener {
            val fullName = editFullName.text.toString().trim()
            val mssv = editMSSV.text.toString().trim()

            if (fullName.isNotBlank() && mssv.isNotBlank()) {
                students.add(StudentModel(fullName, mssv))
                studentAdapter.notifyItemInserted(students.size - 1)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    // Edit student info in a dialog
    private fun editStudent(
        students: MutableList<StudentModel>,
        studentAdapter: StudentAdapter,
        student: StudentModel,
        position: Int
    ) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_new_student)

        val editFullName = dialog.findViewById<EditText>(R.id.edit_fullname)
        val editMSSV = dialog.findViewById<EditText>(R.id.edit_mssv)

        // Prefill existing student data
        editFullName.setText(student.studentName)
        editMSSV.setText(student.studentId)

        dialog.findViewById<Button>(R.id.button_ok).setOnClickListener {
            val newName = editFullName.text.toString().trim()
            val newMSSV = editMSSV.text.toString().trim()

            if (newName.isNotBlank() && newMSSV.isNotBlank()) {
                students[position] = StudentModel(newName, newMSSV)
                studentAdapter.notifyItemChanged(position)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    // Confirm delete student and show Snackbar with Undo option
    private fun confirmDeleteStudent(
        students: MutableList<StudentModel>,
        studentAdapter: StudentAdapter,
        student: StudentModel,
        position: Int
    ) {
        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.studentName}?")
            .setPositiveButton("Delete") { _, _ ->
                // Remove the student from the list
                students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)

                // Show a Snackbar with Undo action
                Snackbar.make(
                    findViewById(R.id.recycler_view_students),  // Use RecyclerView as root view
                    "${student.studentName} has been deleted",
                    Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                    students.add(position, student)
                    studentAdapter.notifyItemInserted(position)
                }.show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
