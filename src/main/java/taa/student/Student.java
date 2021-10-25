package taa.student;

//@@author hozhenhong99
import taa.ClassChecker;
import taa.assessment.Assessment;
import taa.assessment.AssessmentList;
import taa.attendance.AttendanceList;

import java.util.HashMap;

/**
 * Represents students.
 */
public class Student implements ClassChecker {
    private String id;
    private String name;
    private final AttendanceList attendanceList;
    private final HashMap<String, Double> results;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.attendanceList = new AttendanceList();
        this.results = new HashMap<>();
    }

    /**
     * Gets the AttendanceList object associated to the student.
     *
     * @return An AttendanceList object.
     */
    public AttendanceList getAttendanceList() {
        return attendanceList;
    }

    /**
     * Sets the name of the student.
     *
     * @param name The name of the student
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the student.
     *
     * @return The name of the student
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the student ID of the student.
     *
     * @param id the student ID of the student
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * returns the student ID of the student.
     *
     * @return the student ID of the student
     */
    public String getId() {
        return this.id;
    }

    /**
     * Adds a key,value pair to the hashmap.
     *
     * @param assessmentName key of the hashmap
     * @param marks          value to be stored under the key given
     */
    public void setMarks(String assessmentName, double marks) {
        results.put(assessmentName, marks);
    }

    /**
     * Deletes the marks for the given assessment.
     *
     * @param assessmentName Assessment whose marks are to be deleted.
     */
    public void deleteMark(String assessmentName) {
        results.remove(assessmentName);
    }

    /**
     * Gets the marks for the given assessment.
     *
     * @param assessmentName Assessment to get marks for.
     * @return The marks for the inputted assessment if it exists, else -1.
     */
    public double getMarks(String assessmentName) {
        if (!results.containsKey(assessmentName)) {
            return -1;
        }

        return results.get(assessmentName);
    }

    /**
     * Gets all assessments and their marks in a hashmap.
     *
     * @return Hashmap containing assessment names as keys and the marks as values.
     */
    public HashMap<String, Double> getResults() {
        return new HashMap<>(results);
    }

    /**
     * Overrides default toString method with the custom print message.
     *
     * @return the custom print message
     */
    @Override
    public String toString() {
        return String.format("%s (%s)", id, name);
    }

    /**
     * Returns true if marks have been inputted for a given assessment.
     *
     * @param assessmentName Assessment to be checked.
     * @return True if marks have been inputted. Returns false otherwise.
     */
    public boolean marksExist(String assessmentName) {
        return results.containsKey(assessmentName);
    }

    @Override
    public boolean verify() {
        if (id.isEmpty() || name.isEmpty()) {
            return false;
        }

        /*//do this in module instead
        for (String assessmentName : results.keySet()) {
            if (!isMarksWithinRange(results.get(assessmentName))) {
                return false;
            }
        }*/

        return true;
    }
}
