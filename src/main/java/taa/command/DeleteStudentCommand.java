package taa.command;

import taa.exception.TaaException;
import taa.Ui;
import taa.module.Module;
import taa.module.ModuleList;
import taa.student.Student;
import taa.util.Util;

public class DeleteStudentCommand extends Command {
    private static final String KEY_MODULE_CODE = "c";
    private static final String KEY_STUDENT_INDEX = "s";
    private static final String[] DELETE_STUDENT_ARGUMENT_KEYS = {KEY_MODULE_CODE, KEY_STUDENT_INDEX};

    private static final String MESSAGE_FORMAT_DELETE_STUDENT_USAGE = "Usage: %s "
            + "%s/<MODULE_CODE> %s/<STUDENT_INDEX>";
    private static final String MESSAGE_FORMAT_STUDENT_DELETED = "Student removed from %s:\n  %s";

    public DeleteStudentCommand(String argument) {
        super(argument, DELETE_STUDENT_ARGUMENT_KEYS);
    }

    /**
     * Deletes a student from a module.
     *
     * @param modules The list of modules
     * @param ui The ui instance to handle interactions with the user
     * @throws TaaException If the user inputs an invalid command
     */
    @Override
    public void execute(ModuleList modules, Ui ui) throws TaaException {
        if (argument.isEmpty()) {
            throw new TaaException(getUsageMessage());
        }

        if (!checkArgumentMap()) {
            throw new TaaException(getMissingArgumentMessage());
        }

        String moduleCode = argumentMap.get(KEY_MODULE_CODE);
        Module module = modules.getModule(moduleCode);
        if (module == null) {
            throw new TaaException(MESSAGE_MODULE_NOT_FOUND);
        }

        String studentIndexInput = argumentMap.get(KEY_STUDENT_INDEX);
        if (!Util.isInteger(studentIndexInput)) {
            throw new TaaException(MESSAGE_INVALID_STUDENT_INDEX);
        }
        int studentIndex = Integer.parseInt(studentIndexInput) - 1;

        Student student = module.deleteStudent(studentIndex);
        if (student == null) {
            ui.printMessage(MESSAGE_INVALID_STUDENT_INDEX);
        }

        ui.printMessage(String.format(MESSAGE_FORMAT_STUDENT_DELETED, moduleCode, student));
    }

    @Override
    protected String getUsageMessage() {
        return String.format(
                MESSAGE_FORMAT_DELETE_STUDENT_USAGE,
                COMMAND_DELETE_STUDENT,
                KEY_MODULE_CODE,
                KEY_STUDENT_INDEX
        );
    }
}
