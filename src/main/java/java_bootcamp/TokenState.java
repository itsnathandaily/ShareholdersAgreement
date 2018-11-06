package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TokenState implements ContractState {
    private final Party spv;
    private final Party shareholder1;
    private final Party shareholder2;
    private final Double equity1;
    private final Double equity2;
    private final int projectRevenue;
    private final int projectCost;

    public TokenState(Party spv, Party shareholder1, Party shareholder2, Double equity1, Double equity2, int projectRevenue, int projectCost) {
        this.spv = spv;
        this.shareholder1 = shareholder1;
        this.shareholder2 = shareholder2;
        this.equity1 = equity1;
        this.equity2 = equity2;
        this.projectRevenue = projectRevenue;
        this.projectCost = projectCost;
    }

    public Party getSpv() {
        return spv;
    }

    public Party getShareholder1() {
        return shareholder1;
    }

    public Party getShareholder2() {
        return shareholder2;
    }

    public Double getEquity1() {
        return equity1;
    }

    public Double getEquity2() {
        return equity2;
    }

    public int getProjectRevenue() {
        return projectRevenue;
    }

    public int getProjectCost() {
        return projectCost;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(spv, shareholder1, shareholder2);
    }
}