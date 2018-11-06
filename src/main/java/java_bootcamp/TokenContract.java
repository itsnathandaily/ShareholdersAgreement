package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import java.util.List;

public class TokenContract implements Contract {
    public static String ID = "java_bootcamp.TokenContract";

    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        List<Command<Commands>> cmds = tx.commandsOfType(Commands.class);

        if (cmds.size() != 1) throw new IllegalArgumentException("Can't agree and renegotiate at once.");

        Command<Commands> cmd = cmds.get(0);

        if (cmd.getValue() instanceof Commands.CreateAgreement) {

            if (tx.getInputStates().size() != 0)
                throw new IllegalArgumentException("When creating a project, there should be no inputs.");

        } else if (cmd.getValue() instanceof Commands.RenegotiateEquity) {

        } else throw new IllegalArgumentException("Unrecognised command.");

    }

    public interface Commands extends CommandData {
        class CreateAgreement implements Commands {}
        class RenegotiateEquity implements Commands {}
    }
}