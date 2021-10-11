package taa.student;

import taa.ClassChecker;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents students.
 */
public class Student implements ClassChecker {
    private static final double[] MARKS_RANGE = {0, 100};

    private String id;
    private String name;
    private final ArrayList<Attendance> attendances = new ArrayList<>();
    private final HashMap<String, Double> results = new HashMap<>();

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static boolean isMarksWithinRange(double marks) {
        return (marks >= MARKS_RANGE[0] && marks <= MARKS_RANGE[1]);
    }

    public static double[] getMarksRange() {
        return MARKS_RANGE;
    }

    /**
     * Marks the student as present for a particular lesson.
     *
     * @param lessonIndex The lesson index the student is present for
     */
    public void markAttendance(int lessonIndex) {
        // TODO
    }

    /**
     * Returns the attendance status of the student for a particular lesson.
     *
     * @param lessonIndex The lesson index
     * @return The attendance status of the student for the lesson
     */
    public boolean isPresent(int lessonIndex) {
        // TODO
        return false;
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
     * Overrides default toString method with the custom print message.
     *
     * @return the custom print message
     */
    @Override
    public String toString() {
        return String.format("%s - %s", id, name);
    }

    /**
     * Adds a key,value pair to the hashmap.
     *
     * @param assessmentName key of the hashmap
     * @param marks value to be stored under the key given
     */
    public void setMarks(String assessmentName, double marks) {
        results.put(assessmentName, marks);
    }

    /**
     * Gets the marks for the given assessment.
     *
     * @param assessmentName Assessment to get marks for.
     * @return Marks for the inputted assessment.
     */
    public double getMarks(String assessmentName) {
        return results.get(assessmentName);
    }

    /**
     * Gets all assessments and their marks in a hashmap.
     *
     * @return Hashmap containing assessment names as keys and the marks as values.
     */
    public HashMap<String, Double> getAllMarks() {
        return results;
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

        for (Attendance attendance : attendances) {
            if (!attendance.verify()) {
                return false;
            }
        }

        for (String assessmentName : results.keySet()) {
            if (!isMarksWithinRange(results.get(assessmentName))) {
                return false;
            }
        }

        return true;
    }
}
