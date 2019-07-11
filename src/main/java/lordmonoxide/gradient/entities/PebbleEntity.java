package lordmonoxide.gradient.entities;

import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class PebbleEntity extends ProjectileItemEntity {
  public PebbleEntity(final EntityType<? extends PebbleEntity> type, final World world) {
    super(type, world);
  }

  public PebbleEntity(final World world, final LivingEntity thrower) {
    super(GradientEntities.PEBBLE, thrower, world);
  }

  public PebbleEntity(final World world, final double x, final double y, final double z) {
    super(GradientEntities.PEBBLE, x, y, z, world);
  }

  @Override
  protected Item func_213885_i() {
    return GradientItems.PEBBLE;
  }

  @Override
  protected void onImpact(final RayTraceResult result) {
    if (result.getType() == RayTraceResult.Type.ENTITY) {
      final Entity entity = ((EntityRayTraceResult)result).getEntity();
      entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.5f);
    }

    if(!this.world.isRemote) {
      this.world.setEntityState(this, (byte)3);
      this.remove();
    }
  }
}
