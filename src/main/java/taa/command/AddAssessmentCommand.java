package taa.command;

import taa.Ui;
import taa.assessment.Assessment;
import taa.exception.TaaException;
import taa.module.Module;
import taa.module.ModuleList;
import taa.util.Util;

public class AddAssessmentCommand extends Command {
    private static final String KEY_MODULE_CODE = "c";
    private static final String KEY_ASSESSMENT_NAME = "n";
    private static final String KEY_WEIGHTAGE = "w";
    private static final String[] ADD_ASSESSMENT_ARGUMENT_KEYS = {KEY_MODULE_CODE, KEY_ASSESSMENT_NAME, KEY_WEIGHTAGE};

    private static final String MESSAGE_FAIL_TO_ADD = "Fail to add assessment.";
    private static final String MESSAGE_INVALID_WEIGHTAGE = "Invalid weightage.";

    private static final String MESSAGE_FORMAT_ADD_ASSESSMENT_USAGE = "Usage: %s "
            + "%s/<MODULE_CODE> %s/<ASSESSMENT_NAME> %s/<WEIGHTAGE>";
    private static final String MESSAGE_FORMAT_ASSESSMENT_ADDED =
            "Assessment added:\n  %s\nThere are %d assessments in the %s.";

    public AddAssessmentCommand(String argument) {
        super(argument, ADD_ASSESSMENT_ARGUMENT_KEYS);
    }

    /**
     * Adds an assessment to a particular module.
     *
     * @param moduleList The list of modules.
     * @param ui      The ui instance to handle interactions with the user.
     * @throws TaaException If the user inputs an invalid command.
     */
    @Override
    public void execute(ModuleList moduleList, Ui ui) throws TaaException {
        if (argument.isEmpty()) {
            throw new TaaException(getUsageMessage());
        }

        if (!checkArgumentMap()) {
            throw new TaaException(getMissingArgumentMessage());
        }

        String moduleCode = argumentMap.get(KEY_MODULE_CODE);
        Module module = moduleList.getModule(moduleCode);
        if (module == null) {
            throw new TaaException(MESSAGE_MODULE_NOT_FOUND);
        }

        String weightageString = argumentMap.get(KEY_WEIGHTAGE);
        if (!Util.isStringDouble(weightageString)) {
            throw new TaaException(MESSAGE_INVALID_WEIGHTAGE);
        }
        double assessmentWeightage = Double.parseDouble(weightageString);

        String assessmentName = argumentMap.get(KEY_ASSESSMENT_NAME);
        Assessment assessment = new Assessment(assessmentName, assessmentWeightage);
        boolean isSuccessful = module.addAssessment(assessment);
        if (!isSuccessful) {
            throw new TaaException(MESSAGE_FAIL_TO_ADD);
        }

        ui.printMessage(String.format(MESSAGE_FORMAT_ASSESSMENT_ADDED,
                assessment, module.getAssessmentsCount(), module));
    }

    @Override
    protected String getUsageMessage() {
        return String.format(
                MESSAGE_FORMAT_ADD_ASSESSMENT_USAGE,
                COMMAND_ADD_ASSESSMENT,
                KEY_MODULE_CODE,
                KEY_ASSESSMENT_NAME,
                KEY_WEIGHTAGE
        );
    }
}
