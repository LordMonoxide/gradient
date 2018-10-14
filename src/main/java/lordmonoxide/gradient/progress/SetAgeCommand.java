package lordmonoxide.gradient.progress;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class SetAgeCommand extends CommandBase {
  @Override
  public String getName() {
    return "setage";
  }

  @Override
  public String getUsage(final ICommandSender sender) {
    return "setusage";
  }

  @Override
  public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
    if(sender.getCommandSenderEntity() == null) {
      return;
    }

    final PlayerProgress progress = sender.getCommandSenderEntity().getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

    if(progress == null) {
      return;
    }

    progress.setAge(Age.values()[(progress.getAge().ordinal() + 1) % 4]);
    sender.sendMessage(new TextComponentString("Set age to " + progress.getAge()));
  }
}
