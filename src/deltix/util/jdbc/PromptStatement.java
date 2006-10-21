package deltix.util.jdbc;

import java.sql.SQLException;


class PromptStatement implements ScriptStatement {
    private String          mPrompt;

    public PromptStatement (String prompt) {
        mPrompt = prompt;
    }

    public void execute (ScriptExecutionEnvironment env)
        throws SQLException, InterruptedException
    {
        env.getLogger ().println (mPrompt);
    }
}