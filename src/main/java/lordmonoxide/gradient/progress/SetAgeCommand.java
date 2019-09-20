package lordmonoxide.gradient.progress;

import lordmonoxide.gradient.advancements.AdvancementTriggers;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
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

    AdvancementTriggers.CHANGE_AGE.trigger((EntityPlayerMP)target);
    PacketUpdatePlayerProgress.send((EntityPlayerMP)target);

    target.sendMessage(new TextComponentTranslation("commands.setage.set", age.getDisplayName()));

    if(sender != target) {
      sender.sendMessage(new TextComponentTranslation("commands.setage.set_other", target.getDisplayName(), age.getDisplayName()));
    }
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public boolean isUsernameIndex(final String[] args, final int index) {
    return index == 1;
  }
}
