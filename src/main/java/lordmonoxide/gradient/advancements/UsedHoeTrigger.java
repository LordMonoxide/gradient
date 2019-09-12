package lordmonoxide.gradient.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class UsedHoeTrigger implements ICriterionTrigger<UsedHoeTrigger.Instance> {
  private static final ResourceLocation ID = GradientMod.resource("used_hoe");

  private final Map<PlayerAdvancements, UsedHoeTrigger.Listeners> listeners = Maps.newHashMap();

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public void addListener(final PlayerAdvancements advancements, final ICriterionTrigger.Listener<UsedHoeTrigger.Instance> listener) {
    UsedHoeTrigger.Listeners listeners = this.listeners.get(advancements);

    if(listeners == null) {
      listeners = new UsedHoeTrigger.Listeners(advancements);
      this.listeners.put(advancements, listeners);
    }

    listeners.add(listener);
  }

  @Override
  public void removeListener(final PlayerAdvancements advancements, final ICriterionTrigger.Listener<UsedHoeTrigger.Instance> listener) {
    final UsedHoeTrigger.Listeners listeners = this.listeners.get(advancements);

    if(listeners != null) {
      listeners.remove(listener);

      if(listeners.isEmpty()) {
        this.listeners.remove(advancements);
      }
    }
  }

  @Override
  public void removeAllListeners(final PlayerAdvancements advancements) {
    this.listeners.remove(advancements);
  }

  @Override
  public UsedHoeTrigger.Instance deserializeInstance(final JsonObject json, final JsonDeserializationContext context) {
    return new UsedHoeTrigger.Instance();
  }

  public void trigger(final EntityPlayerMP player) {
    final UsedHoeTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());

    if(listeners != null) {
      listeners.trigger();
    }
  }

  static class Listeners {
    private final PlayerAdvancements advancements;
    private final Set<ICriterionTrigger.Listener<UsedHoeTrigger.Instance>> listeners = Sets.newHashSet();

    public Listeners(final PlayerAdvancements advancements) {
      this.advancements = advancements;
    }

    public boolean isEmpty() {
      return this.listeners.isEmpty();
    }

    public void add(final ICriterionTrigger.Listener<UsedHoeTrigger.Instance> listener) {
      this.listeners.add(listener);
    }

    public void remove(final ICriterionTrigger.Listener<UsedHoeTrigger.Instance> listener) {
      this.listeners.remove(listener);
    }

    public void trigger() {
      for(final Listener<Instance> listener : Lists.newArrayList(this.listeners)) {
        listener.grantCriterion(this.advancements);
      }
    }
  }

  public static class Instance extends AbstractCriterionInstance {
    public Instance() {
      super(UsedHoeTrigger.ID);
    }
  }
}
