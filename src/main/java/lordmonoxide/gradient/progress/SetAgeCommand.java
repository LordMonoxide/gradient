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

import java.util.Arrays;

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
    System.out.println(sender);
    System.out.println(sender.getCommandSenderEntity());
    System.out.println(Arrays.toString(args));

    if(args.length < 1) {
      throw new WrongUsageException("commands.setage.usage");
    }

    System.out.println("2");
    final Age age = Age.get(CommandBase.parseInt(args[0], 1, Age.values().length));
    System.out.println(age);

    final Entity target;

    if(args.length == 2) {
      System.out.println("3");
      target = CommandBase.getEntity(server, sender, args[1]);
    } else {
      System.out.println("4");
      if(sender.getCommandSenderEntity() == null) {
        throw new WrongUsageException("commands.setage.usage");
      }
      System.out.println("5");

      target = sender.getCommandSenderEntity();
    }
    System.out.println(target);

    final PlayerProgress progress = target.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);
    System.out.println(progress);

    if(progress == null) {
      return;
    }

    progress.setAge(age);
    System.out.println("set");

    AdvancementTriggers.CHANGE_AGE.trigger((EntityPlayerMP)target);
    PacketUpdatePlayerProgress.send((EntityPlayerMP)target);

    target.sendMessage(new TextComponentTranslation("commands.setage.set", age.getDisplayName()));

    if(sender != target) {
      sender.sendMessage(new TextComponentTranslation("commands.setage.set_other", target.getDisplayName(), age.getDisplayName()));
    }
  }
}
