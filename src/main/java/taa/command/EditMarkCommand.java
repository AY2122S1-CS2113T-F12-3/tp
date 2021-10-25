package taa.command;

import taa.Ui;
import taa.assessment.Assessment;
import taa.assessment.AssessmentList;
import taa.exception.TaaException;
import taa.module.Module;
import taa.module.ModuleList;
import taa.storage.Storage;
import taa.student.Student;
import taa.student.StudentList;
import taa.util.Util;

public class EditMarkCommand extends Command {
    private static final String KEY_MODULE_CODE = "c";
    private static final String KEY_STUDENT_INDEX = "s";
    private static final String KEY_ASSESSMENT_NAME = "a";
    private static final String KEY_NEW_MARKS = "m";
    private static final String[] EDIT_MARKS_ARGUMENT_KEYS = {
        KEY_MODULE_CODE,
        KEY_STUDENT_INDEX,
        KEY_ASSESSMENT_NAME,
        KEY_NEW_MARKS
    };

    private static final String MESSAGE_FORMAT_EDIT_MARKS_USAGE = "%s %s/<MODULE_CODE> %s/<STUDENT_INDEX> "
        + "%s/<ASSESSMENT_NAME> %s/<NEW_MARKS>";
    private static final String MESSAGE_FORMAT_INVALID_MARKS = "Invalid Marks. "
        + "Marks must be between %d and %,.2f (inclusive)";
    private static final String MESSAGE_FORMAT_MARKS_EDITED = "Marks edited for %s: from %,.2f to %,.2f for %s";

    public EditMarkCommand(String argument) {
        super(argument, EDIT_MARKS_ARGUMENT_KEYS);
    }

    @Override
    public void checkArgument() throws TaaException {
        if (argument.isEmpty()) {
            throw new TaaException(getUsageMessage());
        }

        if (!hasAllArguments()) {
            throw new TaaException(getMissingArgumentMessage());
        }

        String studentIndexInput = argumentMap.get(KEY_STUDENT_INDEX);
        if (!Util.isStringInteger(studentIndexInput)) {
            throw new TaaException(MESSAGE_INVALID_STUDENT_INDEX);
        }

        String marksInput = argumentMap.get(KEY_NEW_MARKS);
        if (!Util.isStringDouble(marksInput)) {
            throw new TaaException(MESSAGE_INVALID_MARKS);
        }
    }

    @Override
    public void execute(ModuleList moduleList, Ui ui, Storage storage) throws TaaException {
        String moduleCode = argumentMap.get(KEY_MODULE_CODE);
        Module module = moduleList.getModuleWithCode(moduleCode);
        if (module == null) {
            throw new TaaException(MESSAGE_MODULE_NOT_FOUND);
        }

        String studentIndexInput = argumentMap.get(KEY_STUDENT_INDEX);
        assert Util.isStringInteger(studentIndexInput);
        int studentIndex = Integer.parseInt(studentIndexInput) - 1;
        StudentList studentList = module.getStudentList();
        Student student = studentList.getStudentAt(studentIndex);
        if (student == null) {
            throw new TaaException(MESSAGE_INVALID_STUDENT_INDEX);
        }

        AssessmentList assessmentList = module.getAssessmentList();
        String assessmentName = argumentMap.get(KEY_ASSESSMENT_NAME);
        Assessment assessment = assessmentList.getAssessment(assessmentName);
        if (assessment == null) {
            throw new TaaException(MESSAGE_INVALID_ASSESSMENT_NAME);
        }

        String marksInput = argumentMap.get(KEY_NEW_MARKS);
        assert Util.isStringDouble(marksInput);
        double marks = Double.parseDouble(marksInput);
        double maximumMarks = assessment.getMaximumMarks();
        if (!(assessment.isMarksValid(marks))) {
            throw new TaaException(String.format(MESSAGE_FORMAT_INVALID_MARKS, 0, maximumMarks));
        }

        if (student.getResults().get(assessmentName) == null) {
            throw new TaaException(MESSAGE_NO_MARKS);
        }

        editMark(ui, student, assessmentName, marks);
        storage.save(moduleList);
    }

    private void editMark(Ui ui, Student student, String assessmentName, double newMarks) {
        double previousMark = student.getMarks(assessmentName);
        student.deleteMark(assessmentName);
        student.setMarks(assessmentName, newMarks);
        ui.printMessage(String.format(MESSAGE_FORMAT_MARKS_EDITED, student, previousMark, newMarks, assessmentName));
    }

    @Override
    protected String getUsage() {
        return String.format(
            MESSAGE_FORMAT_EDIT_MARKS_USAGE,
            COMMAND_EDIT_MARK,
            KEY_MODULE_CODE,
            KEY_STUDENT_INDEX,
            KEY_ASSESSMENT_NAME,
            KEY_NEW_MARKS
        );
    }
}
