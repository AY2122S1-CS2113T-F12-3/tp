package taa.student;

import java.util.ArrayList;

public class StudentList {
    private static String MESSAGE_LIST_STUDENT_HEADER = "Student List:";

    private ArrayList<Student> students;

    /**
     * Gets a list of students in the ArrayList. Note that this returns a new ArrayList instance.
     *
     * @return A new ArrayList containing all the students.
     */
    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public int getSize() {
        return students.size();
    }

    private boolean isValidStudentIndex(int index) {
        return (index >= 0 && index < getSize());
    }

    public Student getStudentAt(int index) {
        if (isValidStudentIndex(index)) {
            return students.get(index);
        }

        return null;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Deletes a student from the student list.
     *
     * @param index The index of the student.
     * @return A Student object if successfully deleted, else null.
     */
    public Student deleteStudent(int index) {
        if (isValidStudentIndex(index)) {
            return students.remove(index);
        }

        return null;
    }

    public ArrayList<Student> findStudents(String keyword) {
        String keywordLower = keyword.toLowerCase();

        ArrayList<Student> studentsFound = new ArrayList<>();
        for (Student student : students) {
            String id = student.getId();
            String name = student.getName();

            if (id.toLowerCase().contains(keywordLower) || name.toLowerCase().contains(keywordLower)) {
                studentsFound.add(student);
            }
        }

        return studentsFound;
    }

    @Override
    public String toString() {
        String header = String.format(MESSAGE_LIST_STUDENT_HEADER);
        StringBuilder stringBuilder = new StringBuilder(header);
        for (int i = 0; i < students.size(); i += 1) {
            stringBuilder.append("\n");
            stringBuilder.append(i + 1);
            stringBuilder.append(". ");
            stringBuilder.append(students.get(i));
        }

        return stringBuilder.toString();
    }
}
