package lordmonoxide.gradient.entities;

import lordmonoxide.gradient.GradientEntities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityPebble extends EntityThrowable {
  public EntityPebble(final World world) {
    super(GradientEntities.PEBBLE, world);
  }

  public EntityPebble(final World world, final EntityLivingBase thrower) {
    super(GradientEntities.PEBBLE, thrower, world);
  }

  public EntityPebble(final World world, final double x, final double y, final double z) {
    super(GradientEntities.PEBBLE, x, y, z, world);
  }

  @Override
  protected void onImpact(final RayTraceResult result) {
    if(result.entity != null) {
      result.entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.5f);
    }

    if(!this.world.isRemote) {
      this.world.setEntityState(this, (byte)3);
      this.remove();
    }
  }
}
