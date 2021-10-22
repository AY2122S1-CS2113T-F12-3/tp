package taa.command;

import taa.storage.Storage;
import taa.Ui;
import taa.assessment.Assessment;
import taa.assessment.AssessmentList;
import taa.exception.TaaException;
import taa.module.Module;
import taa.module.ModuleList;
import taa.student.Student;
import taa.student.StudentList;
import taa.util.Util;

/**
 * Class that deals with the command for the setting of marks.
 */
public class SetMarksCommand extends Command {
    private static final String KEY_MODULE_CODE = "c";
    private static final String KEY_STUDENT_INDEX = "s";
    private static final String KEY_ASSESSMENT_NAME = "a";
    private static final String KEY_MARKS = "m";
    private static final String[] SET_MARKS_ARGUMENT_KEYS = {
        KEY_MODULE_CODE,
        KEY_STUDENT_INDEX,
        KEY_ASSESSMENT_NAME,
        KEY_MARKS
    };

    private static final String MESSAGE_FORMAT_SET_MARKS_USAGE = "%s %s/<MODULE_CODE> %s/<STUDENT_INDEX> "
        + "%s/<ASSESSMENT_NAME> %s/<MARKS>";
    private static final String MESSAGE_FORMAT_INVALID_MARKS = "Invalid Marks. "
        + "Marks must be between %d and %,.2f (inclusive)";
    private static final String MESSAGE_FORMAT_MARKS_ADDED = "Marks set for %s: %,.2f for %s";

    public SetMarksCommand(String argument) {
        super(argument, SET_MARKS_ARGUMENT_KEYS);
    }

    /**
     * Executes the set_marks command and sets the marks of a student's assessment.
     *
     * @param moduleList The list of modules.
     * @param ui         The ui instance to handle interactions with the user.
     * @param storage    The storage instance to handle saving.
     * @throws TaaException If the user inputs an invalid command or has missing/invalid argument(s).
     */
    @Override
    public void execute(ModuleList moduleList, Ui ui, Storage storage) throws TaaException {
        if (argument.isEmpty()) {
            throw new TaaException(getUsageMessage());
        }
        if (!hasAllArguments()) {
            throw new TaaException(getMissingArgumentMessage());
        }
        String moduleCode = argumentMap.get(KEY_MODULE_CODE);
        Module module = moduleList.getModule(moduleCode);
        if (module == null) {
            throw new TaaException(MESSAGE_MODULE_NOT_FOUND);
        }
        String studentIndexInput = argumentMap.get(KEY_STUDENT_INDEX);
        int studentIndex = Integer.parseInt(studentIndexInput) - 1;
        StudentList studentList = module.getStudentList();
        Student student = studentList.getStudentAt(studentIndex);
        if (student == null || !Util.isStringInteger(studentIndexInput)) {
            throw new TaaException(MESSAGE_INVALID_STUDENT_INDEX);
        }
        AssessmentList assessmentList = module.getAssessmentList();
        String assessmentName = argumentMap.get(KEY_ASSESSMENT_NAME);
        Assessment assessment = assessmentList.getAssessment(assessmentName);
        if (assessment == null) {
            throw new TaaException(MESSAGE_INVALID_ASSESSMENT_NAME);
        }
        if (student.marksExist(assessmentName)) {
            throw new TaaException(MESSAGE_ALREADY_MARKED);
        }
        String marksInput = argumentMap.get(KEY_MARKS);
        double marks = Double.parseDouble(marksInput);
        double maximumMarks = assessment.getMaximumMarks();
        if ((!Util.isStringDouble(marksInput)) || !(assessment.isMarksValid(marks))) {
            throw new TaaException(String.format(MESSAGE_FORMAT_INVALID_MARKS, 0, maximumMarks));
        }
        setMarks(ui, student, assessmentName, marks);
        storage.save(moduleList);
    }

    /**
     * Sets the marks for a student's assessment.
     *
     * @param ui         The ui instance to handle interactions with the user.
     * @param student    The student instance to set the mark for.
     * @param assessmentName The name of the assessment to be marked.
     * @param marks      The marks to set for the assessment.
     */
    private void setMarks(Ui ui, Student student, String assessmentName, double marks) {
        assert marks >= 0;
        student.setMarks(assessmentName, marks);
        ui.printMessage(String.format(MESSAGE_FORMAT_MARKS_ADDED, student, marks, assessmentName));
    }

    /**
     * Returns the usage message of the set marks command.
     *
     * @return String which contains the usage message.
     */
    @Override
    protected String getUsage() {
        return String.format(
            MESSAGE_FORMAT_SET_MARKS_USAGE,
            COMMAND_SET_MARKS,
            KEY_MODULE_CODE,
            KEY_STUDENT_INDEX,
            KEY_ASSESSMENT_NAME,
            KEY_MARKS
        );
    }
}
