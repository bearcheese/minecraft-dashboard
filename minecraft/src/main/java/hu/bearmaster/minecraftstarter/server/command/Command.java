package hu.bearmaster.minecraftstarter.server.command;

import hu.bearmaster.minecraftstarter.server.model.Action;
import hu.bearmaster.minecraftstarter.server.model.CommandDetails;
import hu.bearmaster.minecraftstarter.server.model.ExecutionResponse;

public interface Command {

    ExecutionResponse execute(CommandDetails commandDetails);

    boolean isExecutedOn(Action action);
}
