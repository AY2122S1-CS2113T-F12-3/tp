package taa.command;

import taa.Ui;
import taa.exception.TaaException;
import taa.module.Module;
import taa.module.ModuleList;

public class ListAssessmentsCommand extends Command {
    private static final String KEY_MODULE_CODE = "c";
    private static final String[] LIST_ASSESSMENTS_ARGUMENT_KEYS = {KEY_MODULE_CODE};

    private static final String MESSAGE_LIST_EMPTY = "There are no assessments in the module.";

    private static final String MESSAGE_FORMAT_LIST_ASSESSMENTS_USAGE = "Usage: %s %s/<MODULE_CODE>";

    public ListAssessmentsCommand(String argument) {
        super(argument, LIST_ASSESSMENTS_ARGUMENT_KEYS);
    }

    /**
     * Lists all the assessments of a particular module.
     *
     * @param modules The list of modules.
     * @param ui The ui instance to handle interactions with the user.
     * @throws TaaException If the user inputs an invalid command.
     */
    @Override
    public void execute(ModuleList modules, Ui ui) throws TaaException {
        if (!argument.isEmpty()) {
            throw new TaaException(getUsageMessage());
        }

        String moduleCode = argumentMap.get("c");
        Module module = modules.getModule(moduleCode);
        if (module.getAssessmentsCount() == 0) {
            ui.printMessage(MESSAGE_LIST_EMPTY);
        } else {
            ui.printMessage(module.getAssessmentList().toString());
        }
    }

    @Override
    protected String getUsageMessage() {
        return String.format(
                MESSAGE_FORMAT_LIST_ASSESSMENTS_USAGE,
                COMMAND_LIST_ASSESSMENTS,
                KEY_MODULE_CODE
        );
    }
}
