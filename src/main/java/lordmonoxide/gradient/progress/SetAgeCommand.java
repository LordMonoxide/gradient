package lordmonoxide.gradient.progress;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class SetAgeCommand extends CommandBase {
  @Override
  public String getName() {
    return "setage";
  }

  @Override
  public String getUsage(final ICommandSender sender) {
    return "commands.setage.usage";
  }

  @Override
  public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
    if(args.length < 1) {
      throw new WrongUsageException("commands.setage.usage");
    }

    final Age age = Age.get(CommandBase.parseInt(args[0], 1, Age.values().length));

    final Entity target;

    if(args.length == 2) {
      target = CommandBase.getEntity(server, sender, args[1]);
    } else {
      if(sender.getCommandSenderEntity() == null) {
        throw new WrongUsageException("commands.setage.usage");
      }

      target = sender.getCommandSenderEntity();
    }

    final PlayerProgress progress = target.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

    if(progress == null) {
      return;
    }

    progress.setAge(age);

    sender.sendMessage(new TextComponentTranslation("commands.setage.set", target.getDisplayName(), age));

    if(sender != target) {
      sender.sendMessage(new TextComponentTranslation("commands.setage.set", age));
    }
  }
}
