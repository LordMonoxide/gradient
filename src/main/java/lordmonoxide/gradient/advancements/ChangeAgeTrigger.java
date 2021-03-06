package lordmonoxide.gradient.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChangeAgeTrigger implements ICriterionTrigger<ChangeAgeTrigger.Instance> {
  private static final ResourceLocation ID = GradientMod.resource("changed_age");
  private final Map<PlayerAdvancements, ChangeAgeTrigger.Listeners> listeners = Maps.newHashMap();

  @Override
  public ResourceLocation getId() {
    return ID;
  }

  @Override
  public void addListener(final PlayerAdvancements playerAdvancementsIn, final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener) {
    ChangeAgeTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

    if(listeners == null) {
      listeners = new ChangeAgeTrigger.Listeners(playerAdvancementsIn);
      this.listeners.put(playerAdvancementsIn, listeners);
    }

    listeners.add(listener);
  }

  @Override
  public void removeListener(final PlayerAdvancements playerAdvancementsIn, final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener) {
    final ChangeAgeTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

    if(listeners != null) {
      listeners.remove(listener);

      if(listeners.isEmpty()) {
        this.listeners.remove(playerAdvancementsIn);
      }
    }
  }

  @Override
  public void removeAllListeners(final PlayerAdvancements playerAdvancementsIn) {
    this.listeners.remove(playerAdvancementsIn);
  }

  /**
   * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
   */
  @Override
  public ChangeAgeTrigger.Instance deserializeInstance(final JsonObject json, final JsonDeserializationContext context) {
    final Age age = Age.get(JsonUtils.getInt(json, "age"));
    return new ChangeAgeTrigger.Instance(age);
  }

  public void trigger(final EntityPlayerMP player) {
    final ChangeAgeTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());

    if(listeners != null) {
      listeners.trigger(player);
    }
  }

  public static class Instance extends AbstractCriterionInstance {
    private final Age age;

    public Instance(final Age age) {
      super(ChangeAgeTrigger.ID);
      this.age = age;
    }

    public boolean test(final EntityPlayer player) {
      return AgeUtils.playerMeetsAgeRequirement(player, this.age);
    }
  }

  static class Listeners {
    private final PlayerAdvancements playerAdvancements;
    private final Set<ICriterionTrigger.Listener<ChangeAgeTrigger.Instance>> listeners = Sets.newHashSet();

    public Listeners(final PlayerAdvancements playerAdvancementsIn) {
      this.playerAdvancements = playerAdvancementsIn;
    }

    public boolean isEmpty() {
      return this.listeners.isEmpty();
    }

    public void add(final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener) {
      this.listeners.add(listener);
    }

    public void remove(final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener) {
      this.listeners.remove(listener);
    }

    public void trigger(final EntityPlayer player) {
      List<ICriterionTrigger.Listener<ChangeAgeTrigger.Instance>> list = null;

      for(final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener : this.listeners) {
        if(listener.getCriterionInstance().test(player)) {
          if(list == null) {
            list = Lists.newArrayList();
          }

          list.add(listener);
        }
      }

      if(list != null) {
        for(final ICriterionTrigger.Listener<ChangeAgeTrigger.Instance> listener1 : list) {
          listener1.grantCriterion(this.playerAdvancements);
        }
      }
    }
  }
}
