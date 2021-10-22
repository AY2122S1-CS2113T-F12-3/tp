package taa.command;

import taa.storage.Storage;
import taa.Ui;
import taa.assessment.Assessment;
import taa.assessment.AssessmentList;
import taa.exception.TaaException;
import taa.module.Module;
import taa.module.ModuleList;
import taa.util.Util;

public class AddAssessmentCommand extends Command {
    private static final String KEY_MODULE_CODE = "c";
    private static final String KEY_ASSESSMENT_NAME = "n";
    private static final String KEY_WEIGHTAGE = "w";
    private static final String KEY_MAXIMUM_MARKS = "m";
    private static final String[] ADD_ASSESSMENT_ARGUMENT_KEYS = {
        KEY_MODULE_CODE,
        KEY_ASSESSMENT_NAME,
        KEY_MAXIMUM_MARKS,
        KEY_WEIGHTAGE
    };

    private static final String MESSAGE_FAIL_TO_ADD = "Fail to add assessment.";

    private static final String MESSAGE_FORMAT_ADD_ASSESSMENT_USAGE = "%s %s/<MODULE_CODE> %s/<ASSESSMENT_NAME> "
            + "%s/<MAXIMUM_MARKS> %s/<WEIGHTAGE>";
    private static final String MESSAGE_FORMAT_INVALID_WEIGHTAGE = "Invalid weightage. "
            + "Weightage must be between %,.2f and %,.2f (inclusive)";
    private static final String MESSAGE_FORMAT_INVALID_MAXIMUM_MARKS = "Invalid maximum marks. "
            + "Maximum marks must be larger than %d (inclusive)";
    private static final String MESSAGE_FORMAT_ASSESSMENT_ADDED = "Assessment added to %s:\n"
            + "  %s\nThere are %d assessments in the %s.";

    public AddAssessmentCommand(String argument) {
        super(argument, ADD_ASSESSMENT_ARGUMENT_KEYS);
    }

    /**
     * Executes the add_assessment command and adds an assessment to a particular module.
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

        String maximumMarksString = argumentMap.get(KEY_MAXIMUM_MARKS);
        if (!Util.isStringInteger(maximumMarksString)) {
            int maximumMarksRange = Assessment.getMaximumMarksRange();
            throw new TaaException(String.format(
                    MESSAGE_FORMAT_INVALID_MAXIMUM_MARKS,
                    maximumMarksRange)
            );
        }

        int maximumMarks = Integer.parseInt(maximumMarksString);
        if (!Assessment.isMaximumMarksWithinRange(maximumMarks)) {
            int maximumMarksRange = Assessment.getMaximumMarksRange();
            throw new TaaException(String.format(
                    MESSAGE_FORMAT_INVALID_MAXIMUM_MARKS,
                    maximumMarksRange)
            );
        }

        String weightageString = argumentMap.get(KEY_WEIGHTAGE);
        if (!Util.isStringDouble(weightageString)) {
            double[] weightageRange = Assessment.getWeightageRange();
            throw new TaaException(String.format(
                    MESSAGE_FORMAT_INVALID_WEIGHTAGE,
                    weightageRange[0],
                    weightageRange[1])
            );
        }

        double weightage = Double.parseDouble(weightageString);
        if (!Assessment.isWeightageWithinRange(weightage)) {
            double[] weightageRange = Assessment.getWeightageRange();
            throw new TaaException(String.format(
                    MESSAGE_FORMAT_INVALID_WEIGHTAGE,
                    weightageRange[0],
                    weightageRange[1])
            );
        }

        String name = argumentMap.get(KEY_ASSESSMENT_NAME);
        Assessment assessment = new Assessment(name, maximumMarks, weightage);

        AssessmentList assessmentList = module.getAssessmentList();
        assert assessmentList != null : "assessment list should exist.";
        boolean isSuccessful = assessmentList.addAssessment(assessment);
        if (!isSuccessful) {
            throw new TaaException(MESSAGE_FAIL_TO_ADD);
        }

        assert storage != null : "storage should exist.";
        storage.save(moduleList);

        assert ui != null : "ui should exist.";
        ui.printMessage(String.format(MESSAGE_FORMAT_ASSESSMENT_ADDED, moduleCode,
                assessment, assessmentList.getSize(), module));
    }

    @Override
    protected String getUsage() {
        return String.format(
                MESSAGE_FORMAT_ADD_ASSESSMENT_USAGE,
                COMMAND_ADD_ASSESSMENT,
                KEY_MODULE_CODE,
                KEY_ASSESSMENT_NAME,
                KEY_MAXIMUM_MARKS,
                KEY_WEIGHTAGE
        );
    }
}
