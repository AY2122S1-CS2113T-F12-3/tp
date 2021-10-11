package taa.module;

import taa.assessment.AssessmentList;
import taa.student.StudentList;

public class Module {
    private String code;
    private String name;
    private int lessonCount;
    private final StudentList studentList;
    private final AssessmentList assessmentList;

    public Module(String code, String name) {
        this.code = code;
        this.name = name;
        this.studentList = new StudentList();
        this.assessmentList = new AssessmentList();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }

    public StudentList getStudentList() {
        return studentList;
    }

    public AssessmentList getAssessmentList() {
        return assessmentList;
    }

    @Override
    public String toString() {
        if (name.isEmpty()) {
            return code;
        }

        return String.format("%s - %s", code, name);
    }
}
