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

public class DeleteMarkCommand extends Command {
    private static final String KEY_MODULE_CODE = "c";
    private static final String KEY_STUDENT_INDEX = "s";
    private static final String KEY_ASSESSMENT_NAME = "a";
    private static final String[] DELETE_MARK_ARGUMENT_KEYS = {
        KEY_MODULE_CODE,
        KEY_STUDENT_INDEX,
        KEY_ASSESSMENT_NAME
    };
    private static final String MESSAGE_FORMAT_DELETE_MARKS_USAGE = "%s %s/<MODULE_CODE> %s/<STUDENT_INDEX> "
        + "%s/<ASSESSMENT_NAME>";
    private static final String MESSAGE_FORMAT_MARKS_DELETED = "Marks for student '%s' have been deleted for"
        + " assessment '%s'";

    public DeleteMarkCommand(String argument) {
        super(argument, DELETE_MARK_ARGUMENT_KEYS);
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

        if (student.getResults().get(assessmentName) == null) {
            throw new TaaException(MESSAGE_NO_MARKS);
        }

        deleteMark(ui, student, assessmentName);
        storage.save(moduleList);
    }

    private void deleteMark(Ui ui, Student student, String assessmentName) {
        student.deleteMark(assessmentName);
        ui.printMessage(String.format(MESSAGE_FORMAT_MARKS_DELETED, student, assessmentName));
    }


    @Override
    protected String getUsage() {
        return String.format(
            MESSAGE_FORMAT_DELETE_MARKS_USAGE,
            COMMAND_DELETE_MARK,
            KEY_MODULE_CODE,
            KEY_STUDENT_INDEX,
            KEY_ASSESSMENT_NAME
        );
    }
}
