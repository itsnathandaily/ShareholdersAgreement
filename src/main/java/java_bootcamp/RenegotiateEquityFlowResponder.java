package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.transactions.LedgerTransaction;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import org.jetbrains.annotations.NotNull;

import java.security.SignatureException;
import java.util.List;

@InitiatedBy(RenegotiateEquityFlow.class)
public class RenegotiateEquityFlowResponder extends FlowLogic<SignedTransaction> {
    private final FlowSession counterpartySession;

    public RenegotiateEquityFlowResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        class MySignTransactionFlow extends SignTransactionFlow {
            MySignTransactionFlow(FlowSession otherSideSession, ProgressTracker progressTracker) {
                super(otherSideSession, progressTracker);
            }

            @Override
            protected void checkTransaction(SignedTransaction stx) throws FlowException {
//                try {
//                    LedgerTransaction tx = stx.toLedgerTransaction(getServiceHub());
//
//                    TokenState input = tx.inputsOfType(TokenState.class).get(0);
//                    TokenState output = tx.outputsOfType(TokenState.class).get(0);
//
//                    if (output.getEquity2() < input.getEquity2()) {
//                        throw new FlowException("I won't accept a fall in my equity.");
//                    }
//
//                } catch (SignatureException e) {
//                    e.printStackTrace();
//                }
            }
        }

        return subFlow(new MySignTransactionFlow(counterpartySession, SignTransactionFlow.Companion.tracker()));
    }
}